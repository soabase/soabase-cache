package io.soabase.cache;

import com.google.common.reflect.Reflection;
import io.soabase.cache.annotations.Cached;
import io.soabase.cache.annotations.ClearsCache;
import io.soabase.cache.spi.CacheController;
import java.lang.reflect.InvocationHandler;

public class CachingControllerBuilder
{
    public static <T> T build(CacheController cacheController, T parentController, Class<T> iface)
    {
        InvocationHandler handler = (proxy, method, args) -> {
            ClearsCache clearsCache = method.getAnnotation(ClearsCache.class);
            if ( clearsCache != null )
            {
                if ( clearsCache.all() )
                {
                    cacheController.invalidateAll();
                }
                else
                {
                    String cacheKey = cacheController.makeKey(clearsCache.value(), method, args);
                    cacheController.invalidate(cacheKey);
                }
            }

            Cached cached = method.getAnnotation(Cached.class);
            if ( cached != null )
            {
                String cacheKey = cacheController.makeKey(cached.value(), method, args);
                return cacheController.get(cacheKey, () -> method.invoke(parentController, args));
            }

            return method.invoke(parentController, args);
        };
        return Reflection.newProxy(iface, handler);
    }

    private CachingControllerBuilder()
    {
    }
}
