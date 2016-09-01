package io.soabase.cache;

import io.soabase.cache.memory.MemoryCacheController;
import io.soabase.cache.spi.CacheController;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.concurrent.TimeUnit;

public class TestCache
{
    private static final int cacheTtlMs = 100000;
    private CacheController cacheController;

    @Before
    public void setup()
    {
        cacheController = new MemoryCacheController(cacheTtlMs, TimeUnit.MILLISECONDS);
    }

    @After
    public void cleanUp()
    {
        cacheController.invalidateAll();
    }

    @Test
    public void testCache()
    {
        MockController realController = new MockControllerImpl();
        MockController controller = CachingControllerBuilder.build(cacheController, realController, MockController.class);
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
