import designsystemcomponents.*
import js.promise.PromiseResult
import react.FC
import react.Props
import react.create
import react.dom.events.ChangeEvent
import react.dom.html.ReactHTML.form
import react.useState
import tanstack.query.core.QueryKey
import tanstack.react.query.useQuery
import web.html.HTMLInputElement
import web.html.InputType

//external interface ValidationPageProps : Props {
//    var isValidationAvailable : Boolean
//    var onIbanChange : (String) -> Unit
//    var onIbanSubmit: (String) -> Unit
//    var validationError : String?
//    var validationResults : String?
//}

val ValidationPage = FC<Props> { _ ->
    data class FormValues(val iban: String)

    var (formValues, setFormValues) = useState(FormValues(iban = ""))
    var (iban, setIban) = useState(formValues.iban)

    @Suppress("UnsafeCastFromDynamic")
    inline fun <T> jso(builder: T.() -> Unit): T =
        (js("{}") as T).apply(builder)

    val queryResult = useQuery<ValidationResponse, dynamic, ValidationResponse, QueryKey>(
        options = jso {
            queryKey = arrayOf("validation", iban).unsafeCast<QueryKey>()
            enabled = iban.isNotEmpty()
            // Instead of a boolean literal, supply a lambda that takes the attempt and error and returns false.
            retry = { _: Int, _: dynamic -> false }
            // Optionally, add a query function if required.

            queryFn = {
                // Here you would normally call an API or perform asynchronous work.
                // For demo purposes, we simply return a static string.
                PromiseResult(ValidationResponse(iban, flags = emptyList(), bank = null))
            }
        }
    )
    val data = queryResult.data

    FocusPageLayout {
        HeroTitle {
            title = "IBAN Validator"
        }
        form {
            FormField {
                button =
                    Button.create {
                        type = InputType.submit
                        MagnfyingGlassIcon { }
                    }

                TextInput {
                    dataTestAttribute = "iban-entry"
                    onChange = { event: ChangeEvent<HTMLInputElement> ->
                        // Read the new value from the input element.
                        val newIban = event.target.value
                        // Update state accordingly.
                        formValues = formValues.copy(iban = newIban)
                    }
                }
            }
            onSubmit = { event ->
                setIban(formValues.iban)
                event.preventDefault()
            }
        }
        PositiveList {
            items = listOf("x", "y", "z")
        }
    }
}

