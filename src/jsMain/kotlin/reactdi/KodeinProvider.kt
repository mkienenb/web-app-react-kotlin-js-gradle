package reactdi

import org.kodein.di.DI
import react.FC
import react.PropsWithChildren
import react.createContext

val DIContext = createContext<DI>()

external interface KodeinProviderProps : PropsWithChildren {
    var di : DI
}

val KodeinProvider = FC<KodeinProviderProps> { props ->
    DIContext.Provider {
        value = props.di
        +props.children
    }
}