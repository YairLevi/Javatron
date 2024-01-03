import annotations.BindMethod;
import annotations.BindType;

@BindType
public class Custom {
    int age;
    String name;

    Javatron jt;

    public Custom(Javatron kt) {
        this.jt = kt;
    }

    @BindMethod
    public void invoke() {
        System.out.println("in invoke java");
        this.jt.invoke("test_invoke");
    }
}
