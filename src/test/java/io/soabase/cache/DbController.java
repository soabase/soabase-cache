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

import io.soabase.cache.annotations.CacheKey;
import io.soabase.cache.annotations.Cache;
import io.soabase.cache.annotations.CacheClear;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

public interface DbController
{
    @SqlUpdate("CREATE TABLE Test(i int primary key, s varchar(100))")
    void createDb();

    @SqlUpdate("INSERT INTO Test(i, s) VALUES (:i, :s)")
    void add(@CacheKey("key") @Bind("i") int i, @Bind("s") String s);

    @CacheClear
    @SqlUpdate("UPDATE Test SET s = :s WHERE i = :i")
    void update(@CacheKey("key") @Bind("i") int i, @Bind("s") String s);

    @Cache
    @SqlQuery("SELECT s FROM Test WHERE i = :value")
    String get(@CacheKey("key") @Bind("value") int i);
}
