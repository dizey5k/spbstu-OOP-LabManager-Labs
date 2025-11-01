package Lab3.Animal;

public abstract class Animal {
    private final String name;

    public Animal() {
        this.name = getClass().getSimpleName() + "-" + System.identityHashCode(this);
    }
    public Animal(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}