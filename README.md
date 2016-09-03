[![Build Status](https://travis-ci.org/soabase/soabase-cache.svg?branch=master)](https://travis-ci.org/soabase/soabase-cache)
[![Maven Central](https://img.shields.io/maven-central/v/io.soabase/soabase-cache.svg)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22io.soabase%22%20AND%20a%3A%22soabase-cache%22)

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

## Reference

#### Annotations

##### `@Cache`
Marks that the annotated method's return value is to be cached. See below for details on how the cache key is generated.

##### `@CacheClear`
Marks that the annotated method's value is cleared from the cache. See below for details on how the cache key is generated.

##### `@CacheKey`
Controls how cache keys are generated (see below). Can be applied to Interfaces, methods and/or parameters.

#### Key Generation

A list of "key parts" is generated and then combined to create the cache key.

* *Part 0* - Part 0 is based on the Interface name. If the Interface is annotated with `@CacheKey` and the annotation value is not an empty string, that value is used as Part 0. Otherwise, The Interface's full name is used.
* *Part 1* - Part 1 is based on the Method. If the method is annotated with `@CacheKey`, Part 1 is either the annotation value or the method name if the annotation value is an empty string. If the method is _not_ annotated with `@CacheKey` then there is no Part 1.
* *Parts 1+* - The remaining parts consist of any parameters annotated with `@CacheKey`. The value is a concatenation of the annotation value and the parameter value.

The key parts are combined using the configured `KeyPartCombiner`. The `StandardKeyPartCombiner` joins all the parts separated with a `-` (dash).

#### Backing Store

The actual caching of objects is done by the configured `CacheBackingStore`. The library comes with `MemoryCacheBackingStore` which stores objects in a [Guava](https://github.com/google/guava) Cache Map. 

#### CacheKeyAccessor

To get the value of a method parameter, `String.valueOf()` is called. However, the object can implement `CacheKeyAccessor` to provide a custom key value.
