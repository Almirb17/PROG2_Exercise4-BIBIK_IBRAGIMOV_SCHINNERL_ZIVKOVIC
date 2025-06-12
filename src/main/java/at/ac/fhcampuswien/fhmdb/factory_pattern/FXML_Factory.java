package at.ac.fhcampuswien.fhmdb.factory_pattern;

import javafx.util.Callback;
import java.util.HashMap;
import java.util.Map;

public class FXML_Factory implements Callback<Class<?>, Object> {

    private final Map<Class<?>, Object> singletonControllers = new HashMap<>();

    @Override
    public Object call(Class<?> aClass) {
        if (singletonControllers.containsKey(aClass)) {
            System.out.println("Use old controller instance");
            return singletonControllers.get(aClass);
        }

        try {
            System.out.println("Create new controller instance");
            Object controller = aClass.getDeclaredConstructor().newInstance();
            singletonControllers.put(aClass, controller);

            return controller;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
