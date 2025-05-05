@file:JsModule("react-player")
@file:JsNonModule

package confexplorer.viewvideo

import react.*

@JsName("default")
external val ReactPlayer: FC<ReactPlayerProps>

external interface ReactPlayerProps : Props {
    var url: String
    var controls: Boolean
}