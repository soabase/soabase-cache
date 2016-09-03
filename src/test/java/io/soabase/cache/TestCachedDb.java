package io.soabase.cache;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.ManagedDataSource;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.soabase.cache.memory.MemoryCacheBackingStore;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.skife.jdbi.v2.DBI;
import java.util.concurrent.TimeUnit;

public class TestCachedDb
{
    private static final int cacheTtlMs = 100000;
    private DBI dbi;
    private DbController controller;

    @Before
    public void setup()
    {
        Bootstrap<Configuration> bootstrap = new Bootstrap<>(null);
        DataSourceFactory configuration = new DataSourceFactory();
        configuration.setDriverClass("org.hsqldb.jdbc.JDBCDriver");
        configuration.setUrl("jdbc:hsqldb:mem:soa-jdbi;shutdown=true");
        configuration.setUser("SA");
        configuration.setValidationQuery("SELECT * FROM INFORMATION_SCHEMA.SYSTEM_TABLES");
        ManagedDataSource dataSource = configuration.build(bootstrap.getMetricRegistry(), "test");
        Environment environment = new Environment("test",
            bootstrap.getObjectMapper(),
            bootstrap.getValidatorFactory().getValidator(),
            bootstrap.getMetricRegistry(),
            bootstrap.getClassLoader(),
            bootstrap.getHealthCheckRegistry());
        dbi = new DBIFactory().build(environment, configuration, dataSource, "test");
        controller = dbi.onDemand(DbController.class);
        controller.createDb();
    }

    @After
    public void cleanUp()
    {
        dbi.close(controller);
    }

    @Test
    public void testCachedDb()
    {
        MemoryCacheBackingStore cache = new MemoryCacheBackingStore(cacheTtlMs, TimeUnit.MILLISECONDS);
        DbController cachedController = CachingControllerBuilder.build(cache, controller, DbController.class);

        cachedController.add(1, "one");
        cachedController.add(2, "two");

        Assert.assertEquals("one", cachedController.get(1));
        controller.update(1, "won");
        Assert.assertEquals("one", cachedController.get(1));
        cachedController.update(1, "won");
        Assert.assertEquals("won", cachedController.get(1));
    }
}
