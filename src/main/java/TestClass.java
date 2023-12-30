import annotations.BindType;

@BindType
public class TestClass {
    int age;
    String name;
    float weight;

    public TestClass() {
        this.age = 1;
        this.name = "Mike";
        this.weight = 4.5f;
    }
}
