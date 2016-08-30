package io.soabase.cache.spi;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public interface CacheController
{
    <T> T get(Object key, Callable<? extends T> valueLoader);

    void invalidate(Object key);

    void invalidateAll();

    String makeKey(String value, Method method, Object[] args);
}
