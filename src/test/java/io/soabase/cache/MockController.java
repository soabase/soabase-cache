package io.soabase.cache;

import io.soabase.cache.annotations.CacheKey;
import io.soabase.cache.annotations.Cached;
import io.soabase.cache.annotations.ClearsCache;

public interface MockController
{
    @Cached
    long getValue();

    @Cached
    long makeValue(@CacheKey int plus);

    @ClearsCache
    void clear();
}
