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
package io.soabase.cache;

import io.soabase.cache.memory.MemoryCacheBackingStore;
import io.soabase.cache.spi.CacheBackingStore;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.concurrent.TimeUnit;

public class TestCache
{
    private static final int cacheTtlMs = 100000;
    private CacheBackingStore cacheBackingStore;

    @Before
    public void setup()
    {
        cacheBackingStore = new MemoryCacheBackingStore(cacheTtlMs, TimeUnit.MILLISECONDS);
    }

    @After
    public void cleanUp()
    {
        cacheBackingStore.invalidateAll();
    }

    @Test
    public void testCache()
    {
        MockController realController = new MockControllerImpl();
        MockController controller = CachingControllerBuilder.build(cacheBackingStore, realController, MockController.class);
        long value = controller.getValue();
        long madeValue = controller.makeValue(10);
        Assert.assertEquals(value, controller.getValue());
        Assert.assertNotEquals(value, realController.getValue());
        Assert.assertEquals(madeValue, controller.makeValue(10));
        Assert.assertEquals(controller.makeValue(10), controller.makeValue(10));
        Assert.assertNotEquals(controller.makeValue(11), controller.makeValue(10));

        controller.clear();
        Assert.assertEquals(madeValue, controller.makeValue(10));
        long newValue = controller.getValue();
        Assert.assertNotEquals(value, newValue);
        Assert.assertEquals(newValue, controller.getValue());
        Assert.assertEquals(newValue, controller.getValue());
    }
}
