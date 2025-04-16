import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import org.w3c.fetch.Response
import org.w3c.files.Blob
import org.w3c.files.BlobPropertyBag
import kotlin.js.Promise

class ValidationApiServiceTest : ShouldSpec({
    should("return successful response for provided iban").config(enabled = true) {
        val iban = "x"

        val calls = mutableListOf<Pair<String, RequestOptions>>()

        val fakeFetchFunction: IbanValidationFetchFunction = { url ->
            // TODO: this should be provided by ValidationApiService and not specified here
            // IbanValidationFetchFunction needs to take RequestOptions as a parameter
            val options = RequestOptions(headers = mapOf("Content-Type" to "application/json"))
            calls.add(url to options)

            val fakeJson = """{ "iban": "x", "flags": [], "bank": null }"""
            val blob = Blob(arrayOf(fakeJson), BlobPropertyBag(type = "application/json"))
            val response = Response(blob)

            Promise.resolve(response)
        }

        val result = ValidationApiService(fakeFetchFunction).validateIban(iban)

        result shouldBe ValidationResponse(iban, flags = emptyList(), bank = null)

        calls.size shouldBe 1
        val (calledUrl, calledOptions) = calls.first()
        calledUrl shouldBe "http://localhost:9080/validate?iban=x"
        calledOptions.headers["Content-Type"] shouldBe "application/json"
    }
})
