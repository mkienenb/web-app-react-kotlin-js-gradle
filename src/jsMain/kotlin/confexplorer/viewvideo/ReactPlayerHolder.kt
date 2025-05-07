package confexplorer.viewvideo

import react.FC
import react.dom.html.ReactHTML

val ReactPlayerMock = FC<ReactPlayerProps> { props ->
    ReactHTML.div {
    }
}

var reactPlayer: FC<ReactPlayerProps>? = null

val ReactPlayerHolder = FC<ReactPlayerProps> { props ->
    if (reactPlayer == null) {
        ReactPlayer {
            url = props.url
            controls = props.controls
        }
    } else {
        ReactPlayerMock {
            url = props.url
            controls = props.controls
        }
    }
}