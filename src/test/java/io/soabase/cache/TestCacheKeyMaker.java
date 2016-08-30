package io.soabase.cache;

import org.junit.Assert;
import org.junit.Test;
import java.lang.reflect.Method;
import java.util.Date;

public class TestCacheKeyMaker
{
    public static class SpecialClass implements CacheKeyAccessor
    {
        private final String value;

        public SpecialClass(String value)
        {
            this.value = value;
        }

        @Override
        public String getCacheKey()
        {
            return value;
        }
    }

    public void method()
    {
        // dummy
    }

    private static final Method method;
    static
    {
        try
        {
            method = TestCacheKeyMaker.class.getMethod("method");
        }
        catch ( NoSuchMethodException e )
        {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCacheKeyMaker()
    {
        Date now = new Date();
        Assert.assertEquals(TestCacheKeyMaker.class.getName() + "#method()", CacheKeyMaker.makeKey("", method, null));
        Assert.assertEquals(TestCacheKeyMaker.class.getName() + "#test()", CacheKeyMaker.makeKey("test", method, null));
        Assert.assertEquals(TestCacheKeyMaker.class.getName() + "#test(1,2)", CacheKeyMaker.makeKey("test", method, new Object[]{1, 2}));
        Assert.assertEquals(TestCacheKeyMaker.class.getName() + "#test(1," + now + ")", CacheKeyMaker.makeKey("test", method, new Object[]{1, now}));
        Assert.assertEquals(TestCacheKeyMaker.class.getName() + "#test(hey,there)", CacheKeyMaker.makeKey("test", method, new Object[]{new SpecialClass("hey"), new SpecialClass("there")}));
    }
}
