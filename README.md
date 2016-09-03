# soabase-cache
Simple caching proxy that integrates with [Dropwizard](http://www.dropwizard.io/1.0.0/docs/)

## Features

* Proxies any `Interface` to cache the method return value
* Cache keys based on Class name, method name, parameter values or any combination
* Supports any backing store for cached objects - default uses in-memory hash map

TODO
