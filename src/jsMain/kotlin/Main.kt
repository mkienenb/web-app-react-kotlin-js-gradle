import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.w3c.fetch.Response
import react.FC
import react.Props
import react.create
import react.dom.client.createRoot
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.ul
import react.useEffectOnce
import react.useState
import testsupport.dataCodeElementHandleAttribute
import web.dom.document

fun main() {
    val container = document.createElement("div")
    document.body.appendChild(container)

    createRoot(container).render(root.create())
}

object Env {
    val serviceVideoUrl: String
        get() = js("process.env.SERVICE_VIDEO_URL") as String
}

@Serializable
data class Video(val id: String, val videoUrl: String, val title: String, val speaker: String)

val root = FC<Props> {
    +"service video url is ${Env.serviceVideoUrl}"

    val (videoList, setVideoList) = useState<List<Video>>(emptyList())
    val (errorMessage, setErrorMessage) = useState<String?>(null)

    useEffectOnce {
        MainScope().launch {
            try {
                val fetchedVideos = listOf("1", "2", "3").mapNotNull { id ->
                    try {
                        fetchVideo(id)
                    } catch (e: Throwable) {
                        console.error("Failed to fetch video $id", e)
                        null // Skip individual failures
                    }
                }
                setVideoList(fetchedVideos)
            } catch (e: Throwable) {
                console.error("Failed to fetch videos", e)
                setErrorMessage(e.message ?: "Unknown error")
            }
        }
    }

    if (errorMessage != null) {
        li {
            dataCodeElementHandleAttribute = "error"
            +"Error: $errorMessage"
        }
    }

    ul {
        if (videoList.isEmpty()) {
            li {
                dataCodeElementHandleAttribute = "unwatchedVideo"
                +"Loading..."
            }
        } else {
            videoList.forEach { video ->
                li {
                    dataCodeElementHandleAttribute = "unwatchedVideo"
                    +video.title
                }
            }
        }
    }
}

private suspend fun fetchVideo(id: String): Video {
    val response = try {
        window.fetch("${Env.serviceVideoUrl}/$id").await()
    } catch (e: Throwable) {
        throw RuntimeException("Network error while fetching video $id: ${e.message}", e)
    }

    if (!response.ok) {
        throw RuntimeException("HTTP error ${response.status} (${response.statusText}) for video $id")
    }

    val json = try {
        response.text().await()
    } catch (e: Throwable) {
        throw RuntimeException("Failed to read response body for video $id: ${e.message}", e)
    }

    return try {
        Json.decodeFromString<Video>(json)
    } catch (e: Throwable) {
        throw RuntimeException("Failed to parse video JSON for video $id: ${e.message}", e)
    }
}
