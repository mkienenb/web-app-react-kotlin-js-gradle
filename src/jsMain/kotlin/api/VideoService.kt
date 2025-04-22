package api

import confexplorer.viewvideo.Video

object VideoService {
    private var videos = emptyList<Video>()
    fun getVideos(): List<Video> {
        return videos
    }

    fun setVideos(videos: List<Video>) {
        this.videos = videos
    }
}