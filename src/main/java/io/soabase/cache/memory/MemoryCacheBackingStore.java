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
package io.soabase.cache.memory;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.soabase.cache.spi.CacheBackingStore;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class MemoryCacheBackingStore implements CacheBackingStore
{
    private final Cache<String, Object> cache;

    public MemoryCacheBackingStore(long cacheExpirationTime, TimeUnit timeUnit)
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
