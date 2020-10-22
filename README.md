# In-memory cache ![CI/CD](https://github.com/LazarevGleb/cache/workflows/CI/CD/badge.svg?branch=master)
Test task. Create an in-memory cache (for caching Objects) with configurable max size 
and eviction strategy. Two strategies should be implemented: LRU and LFU.
For this task it is assumed that only one thread will access the cache, 
so there is no need to make it thread-safe.

### Build app and get .jar and .pom in /.m2:
```
mvn clean install
```

### Run tests:
```
mvn test
```
### Get docker image:
```
docker pull lazarevgb/cache:latest
```
