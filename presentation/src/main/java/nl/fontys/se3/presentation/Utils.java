package nl.fontys.se3.presentation;

import io.javalin.http.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Utils {
    private Utils() {

    }


    /**
     * Reads given resource file as a string.
     *
     * @param fileName path to the resource file
     * @return the file's contents
     * @throws IOException if read fails for any reason
     */
    public static String getResourceFileAsString(Class clazz, String fileName) throws IOException {
        try (InputStream is = clazz.getResourceAsStream(fileName)) {
            if (is == null) return null;
            try (InputStreamReader isr = new InputStreamReader(is);
                 BufferedReader reader = new BufferedReader(isr)) {
                return reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        }
    }

    /**
     * wraps a method that can throw an exception
     *
     * @param  method method that will be wrapped
     * @return the result of the wrapped value or null if it failed
     */
    public static <T> T wrapException(Supplier<T> method) {
        try {
            return  method.get();
        }
        catch (Exception e) {
            return null;
        }
    }

    public static <T> T readForm(Context ctx, Class<T> clazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        var fields = clazz.getDeclaredFields();

        T returnValue = clazz.getConstructor().newInstance();


        for (Field field : fields) {
            try {
                String fieldName = field.getName();
                String capFieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

                var fieldType = field.getType();
                var formValue = ctx.formParam(fieldName, fieldType).getValue();

                var setter = clazz.getMethod("set" + capFieldName, fieldType);

                setter.invoke(returnValue, formValue);

            } catch (NoSuchMethodException e) {
                //field had no setter
            }
        }

        return  returnValue;
    }
}
