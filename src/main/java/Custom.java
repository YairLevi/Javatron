import annotations.BindMethod;
import annotations.BindType;

@BindType
public class Custom {
    int age;
    String name;

    @BindMethod
    public void invoke() {
        System.out.println("in invoke java");
    }
}
