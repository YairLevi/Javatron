import annotations.BindMethod;
import annotations.BindType;

import java.util.ArrayList;
import java.util.List;

@BindType
public class TestClass {
    int age;
    String name;
    float weight;
    int counter = 0;

    List<Custom> list;

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
    public void incrementAndPrint() {
        this.counter++;
        System.out.println("counter is now: " + this.counter);
    }

    @BindMethod
    public void funcWithClassParam(Custom c) {

    }

    @BindMethod
    public static List<String> joinStrings(String s1, String s2) {
        List<String> list = new ArrayList<>();
        list.add("hello");
        list.add(s1 + ":" + s2);
        return list;
    }

    @BindMethod
    public void yes(List<String> list) {

    }
}
