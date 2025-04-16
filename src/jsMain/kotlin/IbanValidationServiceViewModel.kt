import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class IbanValidationServiceViewModel(
    private val scope: CoroutineScope,
    private val service: ValidationApiService = ValidationApiService(),
) {
    fun validateIban(iban: String, onResult: (ValidationResponse) -> Unit) {
        scope.launch {
            val response = service.validateIban(iban)
            onResult(response)
        }
    }
}
