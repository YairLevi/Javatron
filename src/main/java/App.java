import classes.Custom;
import classes.TestClass;
import dev.webview.Webview;

import java.io.IOException;

public interface App {
    String ClientURL = "frontend/index.html";//"http://localhost:5173";

    static void run() throws IOException {

        Custom c = new Custom();
        TestClass t = new TestClass();

        CodeGenerator.generateTypes(c, t);
        CodeGenerator.generateFunctions(TestClass.class);

        Webview wv = new Webview(true);
        wv.bind("echo", jsonElements -> {
            System.out.println(jsonElements);
            return jsonElements;
        });

        MethodBinder.bind(wv, t);
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
