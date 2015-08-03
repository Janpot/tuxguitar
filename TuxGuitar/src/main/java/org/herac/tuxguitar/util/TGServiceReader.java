package org.herac.tuxguitar.util;

import org.herac.tuxguitar.gui.system.Service;
import org.reflections.Reflections;

import java.util.*;

public class TGServiceReader {
    public static <T> List<T> getServices(Class<T> serviceInterface) {
        Reflections r = new Reflections("", TGClassLoader.instance().getClassLoader());
        Set<Class<?>> pluginClasses = r.getTypesAnnotatedWith(Service.class);
        List<T> instances = new ArrayList<T>();
        for (Class<?> pluginClass : pluginClasses) {
            if (serviceInterface.isAssignableFrom(pluginClass)) {
                try {
                    T plugin = serviceInterface.cast(pluginClass.newInstance());
                    instances.add(plugin);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            ServiceLoader<T> loader = ServiceLoader.load(serviceInterface, TGClassLoader.instance().getClassLoader());
            for (T t : loader) {
                instances.add(t);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return instances;
    }
}
