import annotations.BindMethod;
import annotations.BindType;

import java.util.ArrayList;
import java.util.List;

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

    @BindMethod
    public int addTwoNumbers(int a, int b) {
        return a + b;
    }

    @BindMethod
    public List<String> joinStrings(String s1, String s2) {
        List<String> list = new ArrayList<>();
        list.add("hello");
        list.add(s1 + ":" + s2);
        return list;
    }
}
