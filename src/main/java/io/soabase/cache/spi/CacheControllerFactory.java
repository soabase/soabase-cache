package io.soabase.cache.spi;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.dropwizard.setup.Environment;
import io.soabase.cache.memory.MemoryCacheControllerFactory;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", defaultImpl = MemoryCacheControllerFactory.class)
public interface CacheControllerFactory
{
    CacheController build(Environment environment);
}
