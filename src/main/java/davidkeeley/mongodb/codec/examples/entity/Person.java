package davidkeeley.mongodb.codec.examples.entity;

public class Person {
    public static final String NAME = "name";
    public static final String AGE = "age";

    private String name;
    private Integer age;

    public Person(String name, Integer age) {
        if (name == null || age == null) {
            throw new IllegalArgumentException("cant have null fields");
        }
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

}
