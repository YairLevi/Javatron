import co.casterlabs.rakurai.json.Rson;
import co.casterlabs.rakurai.json.element.JsonElement;
import com.google.gson.Gson;
import dev.webview.Webview;

import java.io.IOException;
import java.util.List;

public class Main {
    private static final String ClientURL = "http://localhost:5173";

    public static void main(String[] args) throws IOException, NoSuchMethodException {
        // init code
        FileManager.createNewFile(TypeGenerator.TYPES_FILE_PATH);

        TypeGenerator.createTypeFromClass(TestClass.class);
        MethodBinding.createJavascriptFunctions(TestClass.class);

        Webview wv = new Webview(true);
        wv.bind("echo", jsonElements -> {
            System.out.println(jsonElements);
            return jsonElements;
        });
        TestClass t = new TestClass();
        List<Handler> hs = MethodBinding.createHandlers(t);
        for (Handler h : hs) {
            if (h == null) continue;
            wv.bind(h.name, WebviewCallbackWrapper.getHandler(h));
        }

        wv.setSize(1000, 600);
        wv.setTitle("Javatron test");
        wv.loadURL(ClientURL);
        wv.run();
    }
}
