// src/jsMain/kotlin/designsystemcomponents/HTMLAttributesWithDataTest.kt
package designsystemcomponents

import react.dom.html.HTMLAttributes
import kotlin.js.asDynamic

private const val DATA_TEST = "data-test"

/**
 * Adds `data-test="…"` to *any* native HTML element in your React DSL.
 */
var HTMLAttributes<*>.dataTestAttribute: String?
    get() = asDynamic()[DATA_TEST] as? String
    set(v)  { asDynamic()[DATA_TEST] = v }
