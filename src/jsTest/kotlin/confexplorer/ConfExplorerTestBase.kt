package confexplorer

import confexplorer.viewvideo.ReactPlayerProps
import confexplorer.viewvideo.reactPlayer
import react.FC
import react.dom.html.ReactHTML
import reactdi.ReactShouldSpecBase

val ReactPlayerMock = FC<ReactPlayerProps> { props ->
    ReactHTML.div {
    }
}

open class ConfExplorerTestBase: ReactShouldSpecBase() {
    init {
        beforeTest {
            if (!runningInBrowser()) {
                reactPlayer = ReactPlayerMock
            }
        }
    }
}