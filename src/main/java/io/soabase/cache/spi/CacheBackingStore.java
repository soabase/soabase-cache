package io.soabase.cache.spi;

import java.util.concurrent.Callable;

public interface CacheBackingStore
{
    <T> T get(String key, Callable<? extends T> valueLoader);

    void invalidate(String key);

    void invalidateAll();
}
