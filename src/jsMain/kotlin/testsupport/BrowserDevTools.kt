package testsupport

import react.FC
import react.Props
import react.ReactNode
import react.ChildrenBuilder

// FC() alternative that also sets the display name in the component view for React Developer Tools plugin
fun <P : Props> namedFC(
    name: String,
    render: ChildrenBuilder.(P) -> Unit
): FC<P> = FC(render).apply {
    asDynamic().displayName = name
}
