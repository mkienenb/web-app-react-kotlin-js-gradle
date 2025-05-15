package confexplorer.viewvideo

import kotlinx.serialization.Serializable


@Serializable
data class Video(val id: Int, val title: String, val videoUrl: String = "www.youtube.com", val speaker: String = "Pav")
