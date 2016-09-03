# soabase-cache
Simple caching proxy that integrates with [Dropwizard](http://www.dropwizard.io/1.0.0/docs/)

## Features

* Proxies any `Interface` to cache the method return value
* Cache keys based on class name, method name, parameter values or any combination
* Supports any backing store for cached objects - default uses in-memory hash map

## Usage

1. Create `Interface`s that model the objects to be cached, marking cacheable methods with cache annotations
2. Create the concrete implementation of the Interface
3. Allocate a `CacheBackingStore` instance either directly or using `CacheBackingStoreFactory`
4. Create the cache proxy using `CachingControllerBuilder`

#### Simple Example

```java
...

public interface PersonContainer {
    @Cache
    Person getPerson(@CacheKey int id);
    
    @CacheClear
    void addPerson(Person p, @CacheKey int id);
}

public class PersonContainerImpl implements PersonContainer {
    ...
}

...

CacheBackingStore store = cacheBackingStoreFactory.build(environment);
PersonContainer containerImpl = new PersonContainerImpl();
PersonContainer container = CachingControllerBuilder.build(store, containerImpl, PersonContainer.class);

...

Person p = container.get(id); // next call to container.get with the same ID will be cached

container.put(newP, id);  // clears the cache entry for id
```

## Group/Artifact

Soabase Cache is available from Maven Central

| GroupId | ArtifactId |
| ------- | ---------- |
| io.soabase | soabase-cache |

## Details

#### Annotations
