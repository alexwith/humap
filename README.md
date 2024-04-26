# 📝 Humap

Pronounced */ˈhjuːmæp/*, humap is an ODM (Object Document Mapper) built specifically for [MongoDB]("https://www.mongodb.com/").

## General
**Entity**
<br>
An entity is an in-code representation of a document in MongoDB. An entity is what you create/load, change, then save. Humap is 
therefore mostly made for stateless applications.

**Repository**
<br>
A repository is where you interface with a specific type of entity. By default you have the methods `Repository#findById`, 
`Repository#findOne` and `Repository#findAll`. In addition to these methods, can you write your own methods that will automatically be 
implemented at runtime for you.

## Example
**Entity**
```java
class User extends IdEntity<UUID> {

    @EntityId
    private UUID id;

    private String username;
    private int level;

    public User(UUID id, String username, int level) {
        this.id = id;
        this.username = username;
        this.level = level;
    }

    @Override
    public UUID getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public int getLevel() {
        return this.level;
    }

    @Modifies("level")
    public void modifyLevel(UnaryOperator<Integer> modifier) {
        this.level = modifier.apply(this.level);
    }
}
```
**Repository**
```java
interface UserRepository extends Repository<UUID, User> {

    User findByUsername(String username);

    CompletableFuture<User> findByUsernameAsync(String username);

    List<User> findByGtLevel(int level);
}
```
**Usage**
```java
User user = new User(UUID.randomUUID(), "Test", 10).proxy();
user.save();

UserRepository repository = Repository.get(UserRepository.class);
User foundUser = repository.findByUsername("Test");

assertEquals(user.getId(), foundUser.getId());
```

## License

Humap is a free and open source application. The application is released under the terms of
the [GPL-3.0 license](https://github.com/alexwith/humap/blob/main/LICENSE).