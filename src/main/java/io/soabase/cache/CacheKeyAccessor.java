package io.soabase.cache;

@FunctionalInterface
public interface CacheKeyAccessor
{
    String getCacheKey();
}
