package org.earthdog.clfs.loader;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Date 2024/10/17 17:47
 * @Author DZN
 * @Desc AbstractSourceLoader
 */
public abstract class AbstractSourceLoader implements SourceLoader {
    /**
     * <p>从database, file, string加载的objects存放在这里</p>
     * <p>key 是 groupName | className 当 Value 中只有一个元素时是 className</p>
     * <p> Value 中存放的是组或者类对应的所有实例 </p>
     */
    protected final Map<String, Map<String, Object>> objects = new ConcurrentHashMap<>();

    protected void saveSingleObj(String qualifiedName, Object obj) {
        Map<String, Object> map = objects.get(DEFAULT_GROUP);
        if (map == null) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put(qualifiedName, obj);
            objects.put(DEFAULT_GROUP, hashMap);
        }else {
            map.put(qualifiedName, obj);
        }
    }

    protected void saveObjs(String groupName, Map<String, Object> objectMap) {
        Map<String, Object> map = objects.get(groupName);
        if (map == null) {
            objects.put(groupName, objectMap);
        } else {
            map.putAll(objectMap);
        }
    }

    /*
    public Object getObj(String qualifiedName) {
        Map<String, Object> objectMap = objects.get(qualifiedName);
        return objectMap.get(qualifiedName);
    }
    */

    public Object getObjByGroup(String groupName, String qualifiedName) {
        Map<String, Object> objectMap = objects.get(groupName);
        return objectMap.get(qualifiedName);
    }

    public static Object loadClass(ClassLoader classLoader, String qualifiedName) {
        if (classLoader == null) {
            throw new RuntimeException();
        }
        try {
            Class<?> klass = classLoader.loadClass(qualifiedName);
            return klass.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

}
