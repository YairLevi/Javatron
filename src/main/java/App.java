import dev.webview.Webview;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface App {
    String ClientURL = "frontend/index.html";//"http://localhost:5173";

    static void run() throws IOException {
        // init code
        FileManager.createNewFile(TypeConverter.TYPES_FILE_PATH);

        Custom c = new Custom();
        TestClass t = new TestClass();

        TypeConverter.generateTypes(
                c, t
        );
        MethodBinder.createJavascriptFunctions(TestClass.class);
        MethodBinder.createTypescriptDeclarations(TestClass.class);

        Webview wv = new Webview(true);
        wv.bind("echo", jsonElements -> {
            System.out.println(jsonElements);
            return jsonElements;
        });

        List<Handler> hs = MethodBinder.createHandlers(t);
        for (Handler h : hs) {
            if (h == null) continue;
            wv.bind(h.name, WebviewCallbackWrapper.getHandler(h));
        }
        wv.bind("test_eval", jsonElements -> {
            wv.eval("setState(c => c+1)");
            return jsonElements;
        });

        wv.setSize(1000, 600);
        wv.setTitle("Javatron test");
        wv.loadURL(ClientURL);
        wv.run();
    }
}
