import co.casterlabs.rakurai.json.Rson;
import co.casterlabs.rakurai.json.element.JsonArray;
import co.casterlabs.rakurai.json.element.JsonElement;
import dev.webview.ConsumingProducer;

/**
 * The current implementation of the `webview` package requires that methods passed to
 * `webview.bind()` receive a JsonArray and return JsonElement of the casterlabs package.
 * So this is a wrapper to make things more intuitive.
 */
public final class WebviewCallbackWrapper {
    private static final Rson r = new Rson.Builder().build();

    /**
     *
     * @param handler a method that receives a json string of the arguments from the frontend,
     *                and returns some java object in return.
     * @return a valid method for `webview.bind()` that essentially does what `handler()` does.
     */
    public static ConsumingProducer<JsonArray, JsonElement> wrap(Handler handler) {
        return jsonArray -> {
            try {
                String jsonArrayAsString = String.valueOf(jsonArray);
                Object result = handler.callback.apply(jsonArrayAsString);
                return r.toJson(result);
            } catch (Exception e) {
                System.out.println("Failed to execute " + handler.name + ". Returned `null` instead.");
                System.out.println("Error: " + e.getMessage());
                return null;
            }
        };
    }
}
