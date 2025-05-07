package confexplorer

import confexplorer.viewvideo.ReactPlayerMock
import confexplorer.viewvideo.reactPlayer
import reactdi.ReactShouldSpecBase

open class ConfExplorerTestBase: ReactShouldSpecBase() {
    init {
        beforeTest {
            if (!runningInBrowser()) {
                reactPlayer = ReactPlayerMock
            }
        }
    }
}