import com.sun.source.tree.Tree;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void printGenericType(Class<?> clazz) throws NoSuchFieldException {
        Class<?> testClass = clazz;

        Field[] stringListField = testClass.getDeclaredFields();
        Arrays.stream(stringListField).forEach(System.out::println);
//        ParameterizedType stringListType = (ParameterizedType) stringListField.getGenericType();
//        Class<?> stringListClass = (Class<?>) stringListType.getActualTypeArguments()[0];
//        System.out.println(stringListClass); // class java.lang.String
    }


    public static String convertStringToReadableFormat(String input) {
        Pattern pattern = Pattern.compile("([\\w.]+)<([\\w.,\\s<>]+)>");
        Matcher matcher = pattern.matcher(input);

        if (matcher.matches()) {
            String className = matcher.group(1);
            String genericTypes = matcher.group(2);

            String simpleClassName = getSimpleTypeName(className);
            StringBuilder result = new StringBuilder(simpleClassName + "<");

            String[] typeTokens = genericTypes.split("\\s*,\\s*");

            for (int i = 0; i < typeTokens.length; i++) {
                result.append(getSimpleTypeName(typeTokens[i]));
                if (i < typeTokens.length - 1) {
                    result.append(", ");
                }
            }
            result.append(">");
            return result.toString();
        } else {
            return input; // Return input unchanged if it doesn't match the expected pattern
        }
    }

    private static String getSimpleTypeName(String typeName) {
        int lastDotIndex = typeName.lastIndexOf('.');
        if (lastDotIndex != -1) {
            return typeName.substring(lastDotIndex + 1);
        } else {
            return typeName;
        }
    }

    public static List<String> func(List<LinkedList<String>> list, System s) {
        return null;
    }


    public static void main(String[] args) {
//        try {
//            App.run();
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
        try {

            Method m = Arrays.stream(Main.class.getDeclaredMethods()).reduce((m1, m2) -> m1.getName() == "func" ? m1 : m2).get();
            System.out.println("Java:");
            System.out.println("return:\n\t" + m.getGenericReturnType().getTypeName());
            System.out.println("params:");
            Arrays.stream(m.getGenericParameterTypes()).forEach(p -> {
                System.out.println("\t" + p.getTypeName());
            });
            System.out.println();
            System.out.println("Typescript:");
            System.out.println("return:\n\t" + TypeConverter.convert(m.getGenericReturnType()));
            System.out.println("params:");
            Arrays.stream(m.getGenericParameterTypes()).forEach(p -> {
                System.out.println("\t" + TypeConverter.convert(p));
            });

//            Arrays.stream(m.getGenericParameterTypes()).forEach(p -> System.out.println("\t" + p));
//            Arrays.stream(m.getParameters()).forEach(p -> {
//                if (p.getType().getSimpleName().equals("List")
//                        || p.getType().getSimpleName().equals("Set")
//                        || p.getType().getSimpleName().equals("Map"))
//                {
//
//                    String[] s = p.getParameterizedType().getTypeName().split("<", 2);
//                    System.out.println(s[0]);
//                    System.out.println(s[1]);
//                    String types = s[1].substring(0, s[1].length()-1);
//                    Arrays.stream(types.split(", ", 2)).forEach(ss -> System.out.println(ss));
////                    String ss = s[1].substring(0, s[1].length()-2);
////                    Arrays.stream(ss.split(",", 1)).forEach(System.out::println);
////                    System.out.println(convertStringToReadableFormat(p.getParameterizedType().getTypeName()));
//                } else {
//                    System.out.println(p.getType().getSimpleName());
//                }
//            });

        } catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
