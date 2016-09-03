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
