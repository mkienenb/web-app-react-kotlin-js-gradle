package confexplorer

object ElementLocator {
    const val UNWATCHED_VIDEO_TITLE_FOR_SELECTED_VIDEO_XPATH_EXPRESSION = """
                    //span[@data-code-element-handle='video-selection-indicator']
                    /following-sibling::span[@data-code-element-handle='unwatched-video-title']"""
}