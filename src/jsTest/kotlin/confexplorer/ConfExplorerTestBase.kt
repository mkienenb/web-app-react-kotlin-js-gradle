package confexplorer

import confexplorer.viewvideo.ReactPlayerProps
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
                val dynamicGlobal = js("globalThis")
                dynamicGlobal.ReactPlayer = ReactPlayerMock
            }
        }
    }
}