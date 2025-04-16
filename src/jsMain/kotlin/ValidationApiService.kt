import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.w3c.fetch.Response
import kotlin.js.Promise

@Serializable
enum class Flag {
    INSTANT, POSITIVE_HISTORY, SECURITY_CLAIMS, PSD2
}

@Serializable
data class Bank(val trustScore: Int?)

@Serializable
data class ValidationResponse(val iban: String, val flags: List<Flag>, val bank: Bank?)

typealias IbanValidationFetchFunction = (String) -> Promise<Response>

class RequestOptions(val headers: Map<String, String>) {
}

class ValidationApiService(
    private val fetch: IbanValidationFetchFunction = { url -> window.fetch(url) }
) {
    suspend fun validateIban(iban: String): ValidationResponse {
        val url = "http://localhost:9080/validate?iban=$iban"
        val response = fetch(url).await()
        val json = response.text().await()
        return Json.decodeFromString(json)
    }
}
