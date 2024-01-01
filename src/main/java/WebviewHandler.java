import java.util.function.Function;

public interface WebviewHandler extends Function<String, Object> {
    /**
     * A handler function that is bound to the DOM window.
     * @param jsonArgs the list of arguments in json format, in string.
     * @return a general Object as return type of the handler.
     */
    @Override
    Object apply(String jsonArgs);
}
