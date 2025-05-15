package confexplorer

import confexplorer.ElementHandle.UNWATCHED_VIDEO_TITLE

object ElementLocator {
    val UNWATCHED_VIDEO_TITLE_FOR_SELECTED_VIDEO_CSS_SELECTOR =
        "[role='option'][aria-selected='true'] ${getCodeElementHandle(UNWATCHED_VIDEO_TITLE)}"
}