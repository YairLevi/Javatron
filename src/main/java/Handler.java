import java.util.function.Function;

interface WebviewHandler extends Function<String, Object> {
    @Override
    Object apply(String jsonArgs);
}

public class Handler {
    public String name;
    public WebviewHandler callback;
}
