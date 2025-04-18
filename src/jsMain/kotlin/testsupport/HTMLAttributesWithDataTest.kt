// src/jsMain/kotlin/designsystemcomponents/HTMLAttributesWithDataTest.kt
package testsupport

import react.dom.html.HTMLAttributes
import kotlin.js.asDynamic

private const val DATA_CODE_ELEMENT_HANDLE = "data-code-element-handle"

/**
 * Adds `data-code-element-handle="…"` to *any* native HTML element in your React DSL.
 */
var HTMLAttributes<*>.dataCodeElementHandleAttribute: String?
    get() = asDynamic()[DATA_CODE_ELEMENT_HANDLE] as? String
    set(v)  { asDynamic()[DATA_CODE_ELEMENT_HANDLE] = v }
