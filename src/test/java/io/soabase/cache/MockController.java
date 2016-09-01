package io.soabase.cache;

import io.soabase.cache.annotations.CacheKey;
import io.soabase.cache.annotations.Cache;
import io.soabase.cache.annotations.CacheClear;

public interface MockController
{
    @Cache
    long getValue();

    @Cache
    long makeValue(@CacheKey int plus);

    @CacheClear
    void clear();
}
