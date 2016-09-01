package io.soabase.cache;

import com.google.common.collect.Lists;
import com.google.common.reflect.Reflection;
import io.soabase.cache.annotations.CacheKey;
import io.soabase.cache.annotations.Cached;
import io.soabase.cache.annotations.ClearsCache;
import io.soabase.cache.keys.KeyPart;
import io.soabase.cache.keys.KeyPartCombiner;
import io.soabase.cache.keys.StandardKeyPartCombiner;
import io.soabase.cache.spi.CacheController;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CachingControllerBuilder
{
    public static <T> T build(CacheController cacheController, T parentController, Class<T> iface)
    {
        return build(cacheController, parentController, iface, StandardKeyPartCombiner.instance);
    }

    public static <T> T build(CacheController cacheController, T parentController, Class<T> iface, KeyPartCombiner combiner)
    {
        CacheKey classCacheKey = iface.getAnnotation(CacheKey.class);
        InvocationHandler handler = (proxy, method, args) -> {
            ClearsCache clearsCache = method.getAnnotation(ClearsCache.class);
            Cached cached = method.getAnnotation(Cached.class);
            if ( (clearsCache != null) || (cached != null) )
            {
                List<KeyPart> keyParts = getKeyParts(classCacheKey, method, args);
                String key = combiner.toKey(keyParts);

                if ( clearsCache != null )
                {
                    cacheController.invalidate(key);
                }

                if ( cached != null )
                {
                    return cacheController.get(key, () -> method.invoke(parentController, args));
                }
            }

            return method.invoke(parentController, args);
        };
        return Reflection.newProxy(iface, handler);
    }

    private static List<KeyPart> getKeyParts(CacheKey classCacheKey, Method method, Object[] args)
    {
        List<KeyPart> keyParts = Lists.newArrayList();
        if ( classCacheKey != null )
        {
            keyParts.add(new KeyPart(classCacheKey.value(), method.getDeclaringClass().getName(), ElementType.TYPE));
        }
        else
        {
            keyParts.add(new KeyPart("", method.getDeclaringClass().getName(), ElementType.TYPE));
        }

        CacheKey methodCacheKey = method.getAnnotation(CacheKey.class);
        if ( methodCacheKey != null )
        {
            keyParts.add(new KeyPart(methodCacheKey.value(), method.getName(), ElementType.METHOD));
        }

        int index = 0;
        for ( Annotation[] annotations : method.getParameterAnnotations() )
        {
            Optional<Annotation> firstCacheKey = Arrays.stream(annotations).filter(a -> a.annotationType().equals(CacheKey.class)).findFirst();
            if ( firstCacheKey.isPresent() )
            {
                CacheKey cacheKey = (CacheKey)firstCacheKey.get();
                keyParts.add(new KeyPart(cacheKey.value(), String.valueOf(args[index]), ElementType.PARAMETER));
            }
            ++index;
        }

        return keyParts;
    }

    private CachingControllerBuilder()
    {
    }
}
