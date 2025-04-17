import kotlinx.coroutines.CompletableDeferred
import org.w3c.fetch.Response
import org.w3c.files.Blob
import org.w3c.files.BlobPropertyBag
import kotlin.js.Promise
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.core.annotation.Tags

class IbanValidationServiceViewModelTest : ShouldSpec({

    should("shouldReturnValidationResponse") {
        val callbackCalled = CompletableDeferred<Unit>()

        val fakeFetchFunction: IbanValidationFetchFunction = {
            val fakeJson = """{ "iban": "DE123", "flags": [], "bank": null }"""
            val blob = Blob(arrayOf(fakeJson), BlobPropertyBag(type = "application/json"))
            val response = Response(blob)
            Promise.resolve(response)
        }

        val validationApiService = ValidationApiService(fakeFetchFunction)
        val viewModel = IbanValidationServiceViewModel(this, validationApiService)

        viewModel.validateIban("DE123") {
            it.shouldBeInstanceOf<ValidationResponse>()
            with(it) {
                iban shouldBe "DE123"
                bank.shouldBeNull()
                flags shouldBe emptyList()
            }
            callbackCalled.complete(Unit)
        }

        callbackCalled.await()
    }
})
