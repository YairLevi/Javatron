import co.casterlabs.rakurai.json.Rson;
import co.casterlabs.rakurai.json.element.JsonArray;
import co.casterlabs.rakurai.json.element.JsonElement;
import dev.webview.ConsumingProducer;
import java.util.function.Function;

public interface WebviewCallbackWrapper {
    Rson r = new Rson.Builder().build();

    static ConsumingProducer<JsonArray, JsonElement> getHandler(Handler handler) {
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
