package api

import confexplorer.viewvideo.Video

object VideoService {
    fun getVideos(): List<Video> {
        return listOf(Video(1, "Learning Kotlin"), Video(2, "Unlearning Java"))
    }
}