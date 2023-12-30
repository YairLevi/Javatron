import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import dev.webview.Webview;
import java.io.IOException;
import java.util.List;

public class Main {
    private static final String ClientURL = "http://localhost:5173";

    public static void main(String[] args) throws IOException {
        TypeGenerator.createTypeFromClass(TestClass.class);
        String s = "[1,{\"age\":1,\"name\":[1,2]}]";

        Webview wv = new Webview(true);
        wv.bind("echo", jsonElements -> {
            System.out.println(jsonElements);
            return jsonElements;
        });

        wv.setSize(1000, 600);
        wv.setTitle("Javatron test");
        wv.loadURL(ClientURL);
        wv.run();
    }
}
