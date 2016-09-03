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
