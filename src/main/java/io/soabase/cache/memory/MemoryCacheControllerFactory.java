package io.soabase.cache.memory;

import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.setup.Environment;
import io.soabase.cache.spi.CacheController;
import io.soabase.cache.spi.CacheControllerFactory;
import javax.validation.constraints.Min;
import java.util.concurrent.TimeUnit;

@JsonTypeName("memory")
public class MemoryCacheControllerFactory implements CacheControllerFactory
{
    @Min(1)
    public long cacheExpirationTimeMs = TimeUnit.MINUTES.toMillis(15);

    @Override
    public CacheController build(Environment environment)
    {
        return new MemoryCacheController(cacheExpirationTimeMs, TimeUnit.MILLISECONDS);
    }
}
