###StandardLogger
A wrapper of slf4j using kotlin and lambda.
- Log objects -> toString()
- Log exceptions -> Stacktrace

####gradle:
```gradle
repositories {
    jcenter()
}
dependencies {
    compile 'com.etx.logger:standard-logger:1.0.0'
}
```
####usage:
```kotlin
class Foo {
    companion object {
        val logger = StandardLogger.getLogger(Foo::class)
    }

    fun log() {
        val list = listOf(1, 2, 3)
        logger.debug { list }
        //the same as:
        if (logger.isDebugEnabled) {
            logger.debug(list.toString())
        }
        
        //print stacktrace
        logger.error { Exception("I am Error.") }
    }
}
```