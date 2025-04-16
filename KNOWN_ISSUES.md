# Known Issues

## Bugs

### koin breaks testing code that references coroutines
* Tests such as these all fail
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
./gradlew --stop
rm -rf build/ node_modules/ package-lock.json yarn.lock .gradle ~/.gradle/npm-cache/ kotlin-js-store/ ~/.kotlin-js-node-modules/
```
  - My suspicion is that it's these two: `kotlin-js-store/` and  `~/.kotlin-js-node-modules/`
* Sometimes these commands have helped or assisted
```bash
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
* 

### useChrome() useChromeHeadless() doesn't find browser

* hacky/fragile gradle task to download puppeteer resolved problem but is a poor full solution
  - may also break under windows due to paths
  - is also slow as it currently re-downloads and installs after each gradle run

## Design

### StartReactApp happens only once 
* That code needs to be moved out of a gradle task to a before-scenario and after-scenario function in jvmTest
* Ideally, configure to be per-scenario or per-feature
