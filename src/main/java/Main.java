import classes.Custom;
import classes.TestClass;

import java.util.List;

public class Main {
    public static List<String> func(List<Custom> list, System s) {
        return null;
    }

    public static void main(String[] args) {
//        try {
//            App.run();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        Javatron jt = new Javatron();
        jt.setSize(700, 700);
        jt.setTitle("My first Javatron app!");
        jt.setURL("https://youtube.com");

        TestClass t = new TestClass();
        Custom c = new Custom();

        jt.bind(c, t);
        jt.addBeforeStartCallback(() -> System.out.println("Started app..."));
        jt.addOnCloseCallback(() -> System.out.println("Closed the app!"));

        jt.run();
    }
}
