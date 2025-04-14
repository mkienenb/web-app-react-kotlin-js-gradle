import designsystemcomponents.*
import react.FC
import react.Props
import react.create
import react.dom.events.ChangeEvent
import react.dom.html.ReactHTML.form
import react.useState
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

