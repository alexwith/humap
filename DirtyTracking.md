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
on proxied objects.

Dirty tracking an entity is quite straight forward, let's use this as an example:
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

When an entity has a sub entity (another entity as a field) we do the exactly same thing
just that the path will be longer, e.g. "child.name".

When it comes to collections and maps I've had to do a lot of thinking. In a relational
database collections would mostly be represented as relationships between different tables,
this means you would be able to identify and update a dirty record. MongoDB on the other hand
would store a collection as an array containing documents, this means we would need to know the
exact index (which isn't possible with hash-based collections) or add some sort of identifier to every
single document in the collection. For the sake of simplicity and avoiding persistence issues I have
decided that collections and maps won't do any sort of dirty tracking, if the collection/map has changed
since the entity was loaded it will re-serialize the entire collection/map when the entity is saved.