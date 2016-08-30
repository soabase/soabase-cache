package io.soabase.cache.spi;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.dropwizard.setup.Environment;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface CacheControllerFactory
{
    CacheController build(Environment environment);
}
