import designsystemcomponents.*
import react.FC
import react.Props
import react.create
import react.createElement
import react.dom.html.ReactHTML.form
import web.html.InputType

//external interface ValidationPageProps : Props {
//    var isValidationAvailable : Boolean
//    var onIbanChange : (String) -> Unit
//    var onIbanSubmit: (String) -> Unit
//    var validationError : String?
//    var validationResults : String?
//}

val ValidationPage = FC<Props> { _ ->
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
                }
            }
        }
        PositiveList {
            items = listOf("x", "y", "z")
        }
    }
}

