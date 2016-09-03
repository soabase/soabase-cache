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

@FunctionalInterface
/**
 * To get the value of a method parameter, String.valueOf() is called. However, the object can implement
 * CacheKeyAccessor to provide a custom key value.
 */
public interface CacheKeyAccessor
{
    /**
     * Return the current cache key part
     *
     * @return current cache key part
     */
    String getCacheKey();
}
