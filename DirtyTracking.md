# Dirty Tracking in Humap

One of the main goals of this project is making an actual half decent
and "fast" dirty tracking system.

Dirty tracking, in this context, is the process of keeping track of all
changes that occur within an entity, so we only serialize necessary parts
of the entity to make saving entities faster.

In this doc, I want to write down some of my thoughts while making the dirty 
tracking. Primarily I'm doing this for my own benefit, to maintain awareness
of the pitfalls and understand the reasons behind them.

## Proxies
Humap achieves dirty tracking through runtime bytecode generation using the
bytebuddy library. The tracking is specifically achieved through method interception
on proxied objects. Humap proxies three object types, `Entity`, `Collection` and `Map`.

### Entity with simple field types
Dirty tracking a simple entity is quite straight forward, let's use this as an example:
```java
class User implements Entity {
    
    @EntityId
    private long id;
    
    private int age;
    
    ...
    
    public void setAge(int age) {...}
}
```
Let's say `User#setAge` is invoked, we can intercept this method call and store
the field name `age` where we track dirty fields. When the entity is serialized
we know that the field `age` needs to be serialized as it's been marked dirty.

### Entity with HashSet
Dirty tracking an entity with a hashset can be quite complex, here's an example:
```java
class User implements Entity {
    ...
    
    private HashSet<String> entities;
    
    ...
    
    public void removeAlias(String alias) {
        this.aliases.remove(alias);
    }
}
```
Imagine `User#removeAlias` is invoked, what exactly is it that we mark dirty? The entire collection?
This would result in the entire set being serialized even if only a single element has changed, which obviously
isn't optimal, especially if the set contains more complex objects.

MongoDB has an array type where we can identify entries by an index, but a hashset doesn't have
indexes.

Maybe we could identify the entries by their hash codes? This isn't a good solution as the contract of
`Object#hashCode` only states that two equal objects must produce the same hashCode, but two unequal objects
do not need to produce distinct hash codes.

So the solution is to seperate these modifications in to two groups, collections that contain objects of the
type `Entity`, and the other group would be everything else that is mongo serializable.
- When removing an `entity` from a collection we would identify the entity using an incremental id, this means 
that all entities have an id used for dirty tracking.
- When removing a `non-entity` from a collection we would identify the non-entity through a bson value

More coming here once I've implemented more