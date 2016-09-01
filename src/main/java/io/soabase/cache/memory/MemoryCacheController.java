package io.soabase.cache.memory;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.soabase.cache.spi.CacheController;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class MemoryCacheController implements CacheController
{
    private final Cache<String, Object> cache;

    public MemoryCacheController(long cacheExpirationTime, TimeUnit timeUnit)
    {
        cache = CacheBuilder.newBuilder().softValues().expireAfterWrite(cacheExpirationTime, timeUnit).build();
    }

    @Override
    public <T> T get(String key, Callable<? extends T> valueLoader)
    {
        try
        {
            //noinspection unchecked
            return (T)cache.get(key, valueLoader);
        }
        catch ( ExecutionException e )
        {
            throw new RuntimeException("Could not read from cache for key: " + key, e);
        }
    }

    @Override
    public void invalidate(String key)
    {
        cache.invalidate(key);
    }

    @Override
    public void invalidateAll()
    {
        cache.invalidateAll();
    }
}