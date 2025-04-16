//import io.kotest.core.spec.style.FunSpec
//import io.kotest.core.spec.style.ShouldSpec
//import io.kotest.matchers.shouldBe
//import org.w3c.fetch.Response
//import kotlin.js.Promise
//
//class ValidationApiServiceTest : FunSpec({
//
//    test("should return response").config(enabled = true) {
//
//
////class ValidationApiServiceTest : ShouldSpec({
////    should("return successful response for provided iban").config(enabled = true) {
//        val iban = "x"
//
//        val calls = mutableListOf<Pair<String, RequestOptions>>()
//
//        val fakeFetchFunction: IbanValidationFetchFunction = { url ->
//            val options = RequestOptions(headers = mapOf("Content-Type" to "application/json"))
//            calls.add(url to options)
//
//            val fakeJson = """{ "iban": "x", "flags": [] }"""
//            val response = object : Response() {
//                override fun text(): Promise<String> = Promise.resolve(fakeJson)
//            }
//            Promise.resolve(response)
//        }
//
//        val result = ValidationApiService(fakeFetchFunction).validateIban(iban)
//
//        result shouldBe ValidationResponse(iban, flags = emptyList(), bank = null)
//
//        calls.size shouldBe 1
//        val (calledUrl, calledOptions) = calls.first()
//        calledUrl shouldBe "http://localhost:9080/validate?iban=x"
//        calledOptions.headers["Content-Type"] shouldBe "application/json"
//    }
//})
