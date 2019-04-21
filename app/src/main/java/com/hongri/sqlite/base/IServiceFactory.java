package com.hongri.sqlite.base;

/**
 * @author zhongyao
 * @date 2019/4/17
 */

public interface IServiceFactory {
    <T> T createService(Class<T> clazz);
}
