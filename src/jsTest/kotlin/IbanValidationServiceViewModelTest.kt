import kotlinx.coroutines.MainScope
import kotlinx.coroutines.job
import kotlinx.coroutines.test.runTest
import org.w3c.fetch.Response
import org.w3c.files.Blob
import org.w3c.files.BlobPropertyBag
import kotlin.js.Promise
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlinx.coroutines.CompletableDeferred

class IbanValidationServiceViewModelTest {


    @Test
    fun shouldReturnValidationResponse() = runTest {
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
            assertIs<ValidationResponse>(it)
            with(it) {
                assertEquals("DE123", iban)
                assertNull(bank)
                assertEquals(emptyList(), flags)
            }
            callbackCalled.complete(Unit)
        }

        callbackCalled.await()
    }
}
