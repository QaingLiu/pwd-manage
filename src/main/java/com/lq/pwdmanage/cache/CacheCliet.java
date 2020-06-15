package com.lq.pwdmanage.cache;

import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 缓存
 * @author LQ
 * @date 2020/6/10 11:28
 */
@Component
public class CacheCliet {

    /**
     * 缓存数据
     */
    private static final Map<String, Object> cacheDatas = new HashMap<>();

    /**
     * 添加数据
     * @param key
     * @param value
     */
    public void put(String key, Object value) {
        cacheDatas.put(key, value);
    }

    /**
     * 是否存在key
     * @param key
     * @return
     */
    public static boolean contains(String key){
        return cacheDatas.containsKey(key);
    }

    /**
     * 获取数据
     * @param key
     * @return
     */
    public Object get(String key) {
        return get(key, null);
    }

    /**
     * 获取数据
     * @param key
     * @param defaultValue
     * @return
     */
    public Object get(String key, Object defaultValue) {
        if (cacheDatas.containsKey(key)) {
            return CloneObj(cacheDatas.get(key));
        }
        return defaultValue;
    }

    /**
     * 删除数据
     * @param key
     */
    public void remove(String key) {
        if (cacheDatas.containsKey(key)) {
            cacheDatas.remove(key);
        }
    }

    /**
     * 清除所有缓存
     */
    public void clearAll() {
        cacheDatas.clear();
    }

    /**
     * 克隆对象
     * @param obj
     * @return
     */
    private Object CloneObj(Object obj) {
        Object retobj = null;
        if (obj == null) {
            return retobj;
        }

        try {
            //写入流中
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);

            //从流中读取
            ObjectInputStream ios = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
            retobj = ios.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retobj;
    }

}
