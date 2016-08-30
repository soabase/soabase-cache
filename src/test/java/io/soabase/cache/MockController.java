package io.soabase.cache;

import io.soabase.cache.annotations.Cached;
import io.soabase.cache.annotations.ClearsCache;

public interface MockController
{
    @Cached("getValue")
    long getValue();

    @Cached
    long makeValue(int plus);

    @ClearsCache("getValue")
    void clear();
}
