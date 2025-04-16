import designsystemcomponents.*
import kotlinx.coroutines.MainScope
//import org.koin.core.component.KoinComponent
//import org.koin.core.component.get
//import org.koin.core.parameter.parametersOf
import react.FC
import react.Props
import react.create
import react.dom.events.ChangeEvent
import react.dom.html.ReactHTML.form
import react.useState
import web.html.HTMLInputElement
import web.html.InputType

//object AppDeps : KoinComponent {
//    val viewModel: IbanValidationServiceViewModel
//        get() = get { parametersOf(MainScope()) }
//}

val mainScope = MainScope()

//object AppDeps : KoinComponent {
//    val viewModel: IbanValidationServiceViewModel get() = get { parametersOf(mainScope) }
//}

val ValidationPage = FC<Props> {
    val viewModel = IbanValidationServiceViewModel(mainScope)

    data class FormValues(val iban: String)

    var (formValues, setFormValues) = useState(FormValues(iban = ""))
    var (validationResponse, setValidationResponse) = useState<ValidationResponse?>(null)

    FocusPageLayout {
        HeroTitle {
            title = "IBAN Validator"
        }

        form {
            FormField {
                button = Button.create {
                    type = InputType.submit
                    MagnfyingGlassIcon {}
                }

                TextInput {
                    dataTestAttribute = "iban-entry"
                    onChange = { event: ChangeEvent<HTMLInputElement> ->
                        val newIban = event.target.value
                        formValues = formValues.copy(iban = newIban)
                    }
                }
            }

            onSubmit = { event ->
                event.preventDefault()
                viewModel.validateIban(formValues.iban) {
                    setValidationResponse(it)
                }
            }
        }

        PositiveList {
            items = listOf("x", "y", "z")
        }

        validationResponse?.let {
            +"Validation Response: "
            +it.toString()
        }
    }
}
