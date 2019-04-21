package com.hongri.sqlite.base;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import android.os.Looper;
import android.util.Log;

/**
 * @author zhongyao
 * @date 2019/4/17
 * 服务管理类
 */

public class Services implements IService {

    /**
     * Service容器
     */
    private final Map<Class<?>, IService> mCache = new ConcurrentHashMap<>();

    /**
     * ServiceFactory容器
     */
    private final Map<Class<?>, IServiceFactory> mServiceFactoryCache = new ConcurrentHashMap<>();

    /**
     * 默认的ServiceFactory
     */
    private IServiceFactory mDefaultServiceFactory = null;

    private Services() {}

    private static class LazyHolder {
        private static final Services mServices = new Services();
    }

    public static final Services getInstance() {
        return LazyHolder.mServices;
    }

    /**
     * 设置默认的ServiceFactory，当缓存，注册Factory找不到对应Service，则从默认的factory中获取
     *
     * @param factory 默认ServiceFactory
     */
    public static void setServiceFactory(IServiceFactory factory) {
        getInstance().mDefaultServiceFactory = factory;
    }

    /**
     * 向Services中注册服务
     *
     * @param clazz  服务接口类型
     * @param object 服务具体实现对象
     * @param <T>    服务泛型类
     */
    public static <T extends IService> void registerService(Class<T> clazz, T object) {
        synchronized (getInstance().mCache) {
            getInstance().mCache.put(clazz, object);
        }
    }

    /**
     * 同步获取服务实例
     *
     * @param clazz 服务Class
     * @param <T>   服务类
     * @return 服务实例
     */
    public static <T extends IService> T get(Class<T> clazz) {
        T service = (T)getInstance().mCache.get(clazz);
        // 如果已经找到服务直接退出向下的查找
        if (service != null) {
            return service;
        }
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            Log.w("library-module-api", "get service in UI thread");
        }
        // 优先从注册的ServiceFactoryCache中获取ServiceFactory
        IServiceFactory factory = getInstance().mServiceFactoryCache.get(clazz);
        // 如果无注册的ServiceFactory，则采用默认的ServiceFactory
        if (factory == null) {
            factory = getInstance().mDefaultServiceFactory;
        }
        if (factory == null) {
            return service;
        }
        //考虑多线程情况下，线程安全创建service单实例
        synchronized (getInstance().mCache) {
            //再做一次检查其它线程是否已创建service实例
            service = (T)getInstance().mCache.get(clazz);
            if (service != null) {
                return service;
            }
            service = factory.createService(clazz);
            if (service == null) {
                return service;
            }
            getInstance().mCache.put(clazz, service);
        }
        return service;
    }

    /**
     * 向Services默认工厂中反注册服务
     *
     * @param clazz 服务接口类型
     */
    public static void unregisterService(Class<?> clazz) {
        synchronized (getInstance().mCache) {
            IService service = getInstance().mCache.get(clazz);
            if (service != null) {
                service.onUnregister();
            }
            getInstance().mCache.remove(clazz);
        }
    }

    public static void unregisterAll() {
        synchronized (getInstance().mCache) {
            for (Entry<Class<?>, IService> entry : getInstance().mCache.entrySet()) {
                entry.getValue().onUnregister();
            }
            getInstance().mCache.clear();
        }
    }

    @Override
    public void onUnregister() {

    }
}
