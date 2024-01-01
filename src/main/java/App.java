import dev.webview.Webview;

import java.io.IOException;
import java.util.List;

public interface App {
    String ClientURL = "http://localhost:5173";

    static void run() throws IOException {
        // init code
        FileManager.createNewFile(TypeConverter.TYPES_FILE_PATH);

        TypeConverter.createTypeFromClass(
                TestClass.class,
                Custom.class
        );
        MethodBinder.createJavascriptFunctions(TestClass.class);
        MethodBinder.createTypescriptDeclarations(TestClass.class);

        Webview wv = new Webview(true);
        wv.bind("echo", jsonElements -> {
            System.out.println(jsonElements);
            return jsonElements;
        });

        TestClass t = new TestClass();
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
