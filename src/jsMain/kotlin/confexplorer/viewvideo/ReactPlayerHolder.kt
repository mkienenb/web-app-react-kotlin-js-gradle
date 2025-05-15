package confexplorer.viewvideo

import react.FC
import react.dom.html.ReactHTML

var reactPlayer: FC<ReactPlayerProps> = ReactPlayer

val ReactPlayerHolder = FC<ReactPlayerProps> { props ->
    reactPlayer {
        url = props.url
        controls = props.controls
    }
}