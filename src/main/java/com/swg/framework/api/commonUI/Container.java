package com.swg.framework.api.commonUI;

import com.swg.framework.enums.ElementType;
import com.swg.framework.logging.FrameworkLogger;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * Class represents abstract container on the android UI.
 * All other controls extend this class since any control might contain other(s) inside
 */
public abstract class Container {
    public String className = "";
    protected Map<String, Object> controlMap = new HashMap<>();

    /**
     * Gets the cached control from the screen.
     *
     * @param controlClass the control class
     * @param <T>          The control parameter.
     * @return the cached control.
     */
    protected <T extends Container> T getCachedControl(Class<T> controlClass, String id, ElementType type) {
        clearData();
        boolean constructorFound = false;
        Object[] objects = {id, type};
        if (controlMap.get(id) == null) {
            Constructor[] constructors = controlClass.getConstructors();
            for (Constructor constructor : constructors) {
                Class[] parameters = constructor.getParameterTypes();
                if (checkTypesMatch(parameters, objects)) {
                    try {
                        controlMap.put(id, constructor.newInstance(objects));
                        className = this.getClass().getName();
                    } catch (Exception e) {
                        FrameworkLogger.logError(String.valueOf(e));
                    }
                    constructorFound = true;
                    break;
                }
            }
            if (!constructorFound) {
                FrameworkLogger.logError(String.valueOf(new NoSuchMethodException(String.format("No constructor matching " +
                        "parameters passed to getCachedControl method found in class %s", controlClass.toString()))));
            }
        }
        return (T) controlMap.get(id);
    }

    private boolean checkTypesMatch(Class[] parameters, Object[] objects) {
        if (parameters.length != objects.length) return false;
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].isInstance(objects[i])) {
                continue;
            }
            return false;
        }
        return true;
    }

    /**
     * Clears all the cached UI controls from screen.
     * It is recommended to call that method after navigating away from the screen.
     */
    public void clearCache() {
        if (controlMap != null) {
            controlMap.clear();
        }
    }

    /**
     * Clears all the cached UI controls from screen.
     * It is recommended to call that method after navigating away from the screen.
     */
    public void clearData() {
        if (controlMap != null && !className.equals(this.getClass().getName())) {
            clearCache();
        }
    }
}
