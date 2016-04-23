package ch.erard22.vertx.compared.common;

/**
 * @author <a href='https://twitter.com/michelerard'>michelerard</a>
 */
public class User {

    private String id;

    private String name;

    private String firstname;

    private int age;

    public User() {
    }


    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
