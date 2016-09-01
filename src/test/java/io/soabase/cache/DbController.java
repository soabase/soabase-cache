package io.soabase.cache;

import io.soabase.cache.annotations.CacheKey;
import io.soabase.cache.annotations.Cached;
import io.soabase.cache.annotations.ClearsCache;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

public interface DbController
{
    @SqlUpdate("CREATE TABLE Test(i int primary key, s varchar(100))")
    void createDb();

    @SqlUpdate("INSERT INTO Test(i, s) VALUES (:i, :s)")
    void add(@CacheKey("key") @Bind("i") int i, @Bind("s") String s);

    @ClearsCache
    @SqlUpdate("UPDATE Test SET s = :s WHERE i = :i")
    void update(@CacheKey("key") @Bind("i") int i, @Bind("s") String s);

    @Cached
    @SqlQuery("SELECT s FROM Test WHERE i = :value")
    String get(@CacheKey("key") @Bind("value") int i);
}
