//import kotlin.test.Test
//import kotlin.test.assertEquals
//import kotlin.js.Promise
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.launch
//
//class IbanValidationServiceViewModelTest {
//
//
//    @Test
//    fun shouldReturnValidationResponse(): Promise<Unit> {
//        val viewModel = IbanValidationServiceViewModel(FakeRepo())
//
//        // Wrap in manual Promise — no coroutine APIs
//        return Promise { resolve, reject ->
//            viewModel.validateIban("DE123")
//                .then { result ->
//                    try {
//                        assertEquals("DE123", result.iban)
//                        resolve(Unit)
//                    } catch (e: Throwable) {
//                        reject(e)
//                    }
//                }
//        }
//    }
//}
