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

import io.soabase.cache.keys.KeyPart;
import io.soabase.cache.keys.KeyPartCombiner;
import io.soabase.cache.keys.StandardKeyPartCombiner;
import org.junit.Assert;
import org.junit.Test;
import java.lang.reflect.Method;
import java.util.List;

public class TestCacheKeys
{
    private static final KeyPartCombiner combiner = new StandardKeyPartCombiner();

    @Test
    public void testCacheKeys() throws NoSuchMethodException
    {
        MockController controller = new MockControllerImpl();
        Method method = MockController.class.getMethod("getSpecial", Special.class);
        List<KeyPart> keyParts = CachingControllerBuilder.getKeyParts(null, method, new Object[]{new Special()});
        Assert.assertEquals(MockController.class.getName() + "-" + "hey" + "-" + "there-something else", combiner.toKey(keyParts));
    }
}
