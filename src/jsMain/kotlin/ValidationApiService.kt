import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@Serializable
enum class Flag {
    INSTANT, POSITIVE_HISTORY, SECURITY_CLAIMS, PSD2
}

@Serializable
data class Bank(val trustScore: Int?)

@Serializable
data class ValidationResponse(val iban: String, val flags: List<Flag>, val bank: Bank?)


data class RequestOptions(val headers: Map<String, String>)
data class Response(
    val ok: Boolean,
    // A suspend function to get the parsed body.
    val json: suspend () -> ValidationResponse
)

data class RequestConfig(
    // A suspend function that takes a URL and options and returns a Response.
    val request: suspend (String, RequestOptions) -> Response
)

suspend fun defaultValidationRequest(url: String, options: RequestOptions): Response {
    val requestInit = js("{ headers: options.headers }")
    val rawResponse: org.w3c.fetch.Response = window.fetch(url, requestInit).await()
    val bodyText = rawResponse.text().await()
    val validationResponse: ValidationResponse = Json.decodeFromString(bodyText)
    return Response(ok = rawResponse.ok) {
        validationResponse
    }
}

fun createIbanValidationApiAdapter(
    iban: String, config: RequestConfig = RequestConfig(request = ::defaultValidationRequest)
): suspend () -> ValidationResponse {
    return {
        val url = "http://localhost:9080/validate?iban=$iban"
        val options = RequestOptions(headers = mapOf("Content-Type" to "application/json"))
        val response = config.request(url, options)
        response.json()
    }
}
