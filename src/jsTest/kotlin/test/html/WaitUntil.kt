package test.html

import kotlinx.coroutines.delay
import web.html.HTMLElement
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

private suspend fun waitUntil(
    timeout: Duration = 5000.milliseconds,
    interval: Duration = 50.milliseconds,
    condition: () -> Boolean
) {
    val startTime = js("Date.now()").unsafeCast<Double>()
    while (!condition()) {
        delay(interval)
        val now = js("Date.now()").unsafeCast<Double>()
        if (now - startTime > timeout.inWholeMilliseconds) {
            throw AssertionError("Timed out after $timeout waiting for condition to be true")
        }
    }
}

suspend fun HTMLElement.waitUntilElementGone(
    selector: String,
    timeout: Duration = 5000.milliseconds
) {
    waitUntil(timeout) {
        querySelector(selector) == null
    }
}

suspend fun HTMLElement.waitUntilElementExists(
    selector: String,
    timeout: Duration = 5000.milliseconds
) {
    waitUntil(timeout) {
        querySelector(selector) != null
    }
}