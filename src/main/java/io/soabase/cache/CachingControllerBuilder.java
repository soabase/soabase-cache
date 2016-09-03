/**
 * Copyright 2016 Jordan Zimmerman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.soabase.cache;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.reflect.Reflection;
import io.soabase.cache.annotations.Cache;
import io.soabase.cache.annotations.CacheClear;
import io.soabase.cache.annotations.CacheKey;
import io.soabase.cache.keys.KeyPart;
import io.soabase.cache.keys.KeyPartCombiner;
import io.soabase.cache.keys.StandardKeyPartCombiner;
import io.soabase.cache.spi.CacheBackingStore;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Utility to build cache controllers/proxies
 */
public class CachingControllerBuilder
{
    /**
     * Build the caching proxy using the {@link StandardKeyPartCombiner}
     *
     * @param cacheBackingStore the cache storage instance
     * @param parentController the actual implementation
     * @param iface the proxy interface type
     * @return the proxy/controller
     */
    public static <T> T build(CacheBackingStore cacheBackingStore, T parentController, Class<T> iface)
    {
        return build(cacheBackingStore, parentController, iface, StandardKeyPartCombiner.instance);
    }

    /**
     * Build the caching proxy using
     *
     * @param cacheBackingStoreArg the cache storage instance
     * @param parentControllerArg the actual implementation
     * @param ifaceArg the proxy interface type
     * @param combinerArg the key combiner instance
     * @return the proxy/controller
     */
    public static <T> T build(CacheBackingStore cacheBackingStoreArg, T parentControllerArg, Class<T> ifaceArg, KeyPartCombiner combinerArg)
    {
        CacheBackingStore cacheBackingStore = Preconditions.checkNotNull(cacheBackingStoreArg, "cacheBackingStore cannot be null");
        T parentController = Preconditions.checkNotNull(parentControllerArg, "parentControllerArg cannot be null");
        Class<T> iface = Preconditions.checkNotNull(ifaceArg, "ifaceArg cannot be null");
        KeyPartCombiner combiner = Preconditions.checkNotNull(combinerArg, "combinerArg cannot be null");

        CacheKey classCacheKey = iface.getAnnotation(CacheKey.class);
        InvocationHandler handler = (proxy, method, args) -> {
            CacheClear cacheClear = method.getAnnotation(CacheClear.class);
            Cache cache = method.getAnnotation(Cache.class);
            if ( (cacheClear != null) || (cache != null) )
            {
                List<KeyPart> keyParts = getKeyParts(classCacheKey, method, args);
                String key = combiner.toKey(keyParts);

                if ( cacheClear != null )
                {
                    cacheBackingStore.invalidate(key);
                }

                if ( cache != null )
                {
                    if ( method.getReturnType().equals(Void.TYPE) )
                    {
                        throw new UnsupportedOperationException("Methods marked with @Cache cannot must return a value");
                    }
                    return cacheBackingStore.get(key, () -> method.invoke(parentController, args));
                }
            }

            return method.invoke(parentController, args);
        };
        return Reflection.newProxy(iface, handler);
    }

    /**
     * Generate key parts
     *
     * @param classCacheKey the class annotation or null
     * @param method the method
     * @param args method arguments
     * @return key parts
     */
    public static List<KeyPart> getKeyParts(CacheKey classCacheKey, Method method, Object[] args)
    {
        method = Preconditions.checkNotNull(method, "method cannot be null");

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
                keyParts.add(new KeyPart(cacheKey.value(), getArgumentValue(args[index]), ElementType.PARAMETER));
            }
            ++index;
        }

        return keyParts;
    }

    private static String getArgumentValue(Object arg)
    {
        if ( arg != null )
        {
            if ( CacheKeyAccessor.class.isAssignableFrom(arg.getClass()) )
            {
                return ((CacheKeyAccessor)arg).getCacheKey();
            }
        }
        return String.valueOf(arg);
    }

    private CachingControllerBuilder()
    {
    }
}
