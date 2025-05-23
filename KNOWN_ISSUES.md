# Known Issues

## Bugs

### koin breaks testing code that references coroutines
* Tests such as these all fail with koin core installed
* For now, don't use koin
* 
```kotlin
import kotlinx.coroutines.*
import kotlin.coroutines.*
import kotlin.test.*
import kotlinx.coroutines.test.*

class SanityTest {
    private suspend fun suspendFun(): Int = 42

    @Test
    fun testSimpleSuspend() = runTest {
        suspendFun()
    }

    @Test
    fun test_GlobalScope_promise_suspendFun(): dynamic = GlobalScope.promise {
        val result = suspendFun()
        assertEquals(42, result)
    }

    @Test
    fun test_MainScope_invoked() {
        val scope = MainScope()
        scope.launch {
            console.log("✅ Coroutine started without Dispatchers")
        }
    }
}

//fun safeRunTest(
//    block: suspend TestScope.() -> Unit
//): TestResult = runTest(context = SafeTestDispatcher, testBody = block)
//
//@Test
//    fun test_suspendFun_in_SafeRunTest() = safeRunTest {
//        val result = suspendFun()
//        assertEquals(42, result)
//    }

//    private val testScope = CoroutineScope(EmptyCoroutineContext)
//    @Test
//    fun testEmptyCoroutineContext(): dynamic = testScope.promise {
//        val result = returnFortyTwo()
//        assertEquals(42, result)
//    }
    
    @Test
    fun logCoroutineContext() {
        val context = Dispatchers.Default + CoroutineName("test")
        context.fold(Unit) { _, element ->
            console.log("Context element: ${element.key} = $element")
        }
    }
```
* gradle error
```
SanityTest.testSimpleSuspend[js, browser, ChromeHeadless117.0.5938.149, Linuxx86_64] FAILED
    ClassCastException at /home/mkienenb/IdeaProjects/gvea/web-app-react-kotlin-js-gradle/compileSync/js/test/testDevelopmentExecutable/kotlin/js/runtime/hacks.kt:23
```
* test exception
```
ClassCastException
	at <global>.THROW_CCE(/home/mkienenb/IdeaProjects/gvea/web-app-react-kotlin-js-gradle/compileSync/js/test/testDevelopmentExecutable/kotlin/js/runtime/hacks.kt:23)
	at StandardTestDispatcherImpl.get_y2st91(/home/mkienenb/IdeaProjects/gvea/web-app-react-kotlin-js-gradle/compileSync/js/test/testDevelopmentExecutable/kotlin/src/kotlin/coroutines/ContinuationInterceptor.kt:60)
	at protoOf.get_y2st91(/home/mkienenb/IdeaProjects/gvea/web-app-react-kotlin-js-gradle/compileSync/js/test/testDevelopmentExecutable/kotlin/src/kotlin/coroutines/CoroutineContextImpl.kt:120)
	at <global>.CoroutineContext$plus$lambda(/home/mkienenb/IdeaProjects/gvea/web-app-react-kotlin-js-gradle/compileSync/js/test/testDevelopmentExecutable/kotlin/src/kotlin/coroutines/CoroutineContext.kt:36)
	at TestCoroutineScheduler.fold_j2vaxd(/home/mkienenb/IdeaProjects/gvea/web-app-react-kotlin-js-gradle/compileSync/js/test/testDevelopmentExecutable/kotlin/src/kotlin/coroutines/CoroutineContext.kt:70)
	at CombinedContext.plus_s13ygv(/home/mkienenb/IdeaProjects/gvea/web-app-react-kotlin-js-gradle/compileSync/js/test/testDevelopmentExecutable/kotlin/src/kotlin/coroutines/CoroutineContext.kt:32)
	at <global>.withDelaySkipping(/mnt/agent/work/44ec6e850d5c63f0/kotlinx-coroutines-test/common/src/TestScope.kt:199)
	at <global>.TestScope(/mnt/agent/work/44ec6e850d5c63f0/kotlinx-coroutines-test/common/src/TestScope.kt:163)
	at <global>.runTest(/mnt/agent/work/44ec6e850d5c63f0/kotlinx-coroutines-test/common/src/TestBuilders.kt:168)
	at protoOf.testSimpleSuspend_z8tjrc(/home/mkienenb/IdeaProjects/gvea/web-app-react-kotlin-js-gradle/src/jsTest/kotlin/SanityTest.kt:27)
```

### package lock bug in yarn? npm? breaks gradle
* What went wrong:
```
Execution failed for task ':kotlinStoreYarnLock'.
> Lock file was changed. Run the `kotlinUpgradePackageLock` task to actualize lock file
```
* error about missing or outdated package-lock.json can't be solved at present
* It's not caused by build.gradle.kts code
* These commands seem to allow recovery
```bash
rm -rf ./kotlin-js-store/
```
* Sometimes these commands have helped or assisted.   If the above command does not work, try some combination of these:
```bash
./gradlew --stop
rm -rf build/ node_modules/ package-lock.json yarn.lock .gradle ~/.gradle/npm-cache/ kotlin-js-store/ ~/.kotlin-js-node-modules/
./gradlew build --refresh-dependencies
```
* This was also suggested at one point 
```build.gradle.kts
//   Put this above kotlin {} closure:
rootProject.extensions.extraProperties["kotlin.js.experimental.forceFullNpmInstall"] = true
```
* This ignores the problem but does not solve it
```build.gradle.kts
// temporary workaround of disabling kotlinStoreYarnLock will cause problems at some point
tasks.named("kotlinStoreYarnLock") {
    enabled = false
}
```

### Unable to add better test run output for jsBrowserTest
* My best guess after trying to write/install a different reporter inside <root>/karma.config.d is that output only goes to the browser console
* https://github.com/Kotlin/kotlin-web-helpers/blob/main/mocha-kotlin-reporter.js

### jsNodeTest cannot detect and skip/ignore test that only work in jsBrowserTests

* Can't do it by setting a filter or tag as the js test frameworks ignore them
* Can't do it by using specific sourceSets due to cycles and other convolutions
* Can do it by detecting js("typeof window !== 'undefined'") when running test and then skipping test registration
  - this requires code support per test
  - Managed to encapsulate most of it in BrowserOnlyShouldSpec requiring all code be inside the init constructor
```kotlin
class ValidationPageTest : BrowserOnlyShouldSpec() {
    init {
        browserOnlyCode {
          beforeTest { }
          afterTest {  }
          should("test something") { }
        }
    }
}
```

### useChrome() useChromeHeadless() doesn't find browser

* hacky/fragile gradle task to download puppeteer resolved problem but is a poor full solution
  - is also slow as it currently re-downloads and installs after each gradle clean


### too many files watched by node
```
<i> [webpack-dev-server] Content not from webpack is served from 'kotlin, ../../../processedResources/js/main' directory
node:internal/fs/watchers:247
    const error = new UVException({
                  ^

Error: EMFILE: too many open files, watch 'kotlin'
Error: EMFILE: too many open files, watch 'kotlin'
at FSWatcher.<computed> (node:internal/fs/watchers:247:19)
at Object.watch (node:fs:2467:36)
at createFsWatchInstance (/home/mkienenb/IdeaProjects/gvea/web-app-react-kotlin-js-gradle/build/js/node_modules/chokidar/lib/nodefs-handler.js:119:15)
at setFsWatchListener (/home/mkienenb/IdeaProjects/gvea/web-app-react-kotlin-js-gradle/build/js/node_modules/chokidar/lib/nodefs-handler.js:166:15)
at NodeFsHandler._watchWithNodeFs (/home/mkienenb/IdeaProjects/gvea/web-app-react-kotlin-js-gradle/build/js/node_modules/chokidar/lib/nodefs-handler.js:331:14)
at NodeFsHandler._handleDir (/home/mkienenb/IdeaProjects/gvea/web-app-react-kotlin-js-gradle/build/js/node_modules/chokidar/lib/nodefs-handler.js:567:19)
at process.processTicksAndRejections (node:internal/process/task_queues:95:5)
at async NodeFsHandler._addToNodeFs (/home/mkienenb/IdeaProjects/gvea/web-app-react-kotlin-js-gradle/build/js/node_modules/chokidar/lib/nodefs-handler.js:617:16)
at async /home/mkienenb/IdeaProjects/gvea/web-app-react-kotlin-js-gradle/build/js/node_modules/chokidar/index.js:451:21
at async Promise.all (index 0)
Emitted 'error' event on FSWatcher instance at:
Emitted 'error' event on FSWatcher instance at:
at FSWatcher._handleError (/home/mkienenb/IdeaProjects/gvea/web-app-react-kotlin-js-gradle/build/js/node_modules/chokidar/index.js:647:10)
at NodeFsHandler._addToNodeFs (/home/mkienenb/IdeaProjects/gvea/web-app-react-kotlin-js-gradle/build/js/node_modules/chokidar/lib/nodefs-handler.js:645:18)
at process.processTicksAndRejections (node:internal/process/task_queues:95:5)
at async /home/mkienenb/IdeaProjects/gvea/web-app-react-kotlin-js-gradle/build/js/node_modules/chokidar/index.js:451:21
at async Promise.all (index 0) {
errno: -24,
syscall: 'watch',
code: 'EMFILE',
path: 'kotlin',
filename: 'kotlin'
}
```
* Fix: create config file to limit what is being watched: `webpack.config.d/watch-options.js`
```js
config.watchOptions = {
    ignored: /node_modules/,
    poll: 1000,
};
```