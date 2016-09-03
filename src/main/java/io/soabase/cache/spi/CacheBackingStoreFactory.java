package io.soabase.cache.spi;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.dropwizard.setup.Environment;
import io.soabase.cache.memory.MemoryCacheBackingStoreFactory;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", defaultImpl = MemoryCacheBackingStoreFactory.class)
public interface CacheBackingStoreFactory
{
    CacheBackingStore build(Environment environment);
}
