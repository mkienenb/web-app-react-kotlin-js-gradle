interface IbanValidationRepository {
    suspend fun validate(iban: String): ValidationResponse
}

class RealIbanValidationRepository(
    private val service: ValidationApiService
) : IbanValidationRepository {
    override suspend fun validate(iban: String): ValidationResponse {
        return service.validateIban(iban)
    }
}
