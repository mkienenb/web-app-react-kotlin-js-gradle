package confexplorer.viewvideo

import kotlinx.serialization.Serializable


@Serializable
data class Video(val id: Int, val title: String, val videoUrl: String = "unspecified", val speaker: String = "unspecified") {}
