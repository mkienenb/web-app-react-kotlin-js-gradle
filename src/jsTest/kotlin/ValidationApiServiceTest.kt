import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

// Kotest test using a fake request function.
class ApiAdapterFactoryTest : ShouldSpec({
    should("return successful response for provided iban") {
        val iban = "x"

        // A mutable list to record the call parameters.
        val calls = mutableListOf<Pair<String, RequestOptions>>()

        // A fake request function that records its parameters and returns a fake response.
        val fakeRequest: suspend (String, RequestOptions) -> Response = { url, options ->
            calls.add(url to options)
            Response(ok = true) {
                ValidationResponse(iban, flags = emptyList(), bank = null)
            }
        }

        // Create the adapter with the fakeRequest in the config.
        val adapter = createIbanValidationApiAdapter(iban, RequestConfig(request = fakeRequest))
        val result = adapter()

        // Assert that the adapter returns the expected response.
        result shouldBe ValidationResponse(iban, flags = emptyList(), bank = null)

        // Verify that the fake request was called exactly once with the expected URL and headers.
        calls.size shouldBe 1
        val (calledUrl, calledOptions) = calls.first()
        calledUrl shouldBe "http://localhost:9080/validate?iban=x"
        calledOptions.headers["Content-Type"] shouldBe "application/json"
    }
})
