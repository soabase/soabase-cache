package io.soabase.cache.memory;

import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.setup.Environment;
import io.soabase.cache.spi.CacheBackingStore;
import io.soabase.cache.spi.CacheBackingStoreFactory;
import javax.validation.constraints.Min;
import java.util.concurrent.TimeUnit;

@JsonTypeName("memory")
public class MemoryCacheBackingStoreFactory implements CacheBackingStoreFactory
{
    @Min(1)
    public long cacheExpirationTimeMs = TimeUnit.MINUTES.toMillis(15);

    @Override
    public CacheBackingStore build(Environment environment)
    {
        return new MemoryCacheBackingStore(cacheExpirationTimeMs, TimeUnit.MILLISECONDS);
    }
}
