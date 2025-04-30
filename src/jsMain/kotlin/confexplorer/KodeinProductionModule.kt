package confexplorer

import api.URLToPromiseResponseFunction
import api.UrlProvider
import api.VideoService
import confexplorer.viewvideo.Video
import kotlinx.browser.window
import org.kodein.di.*

val productionModule = DI.Module("production") {
    bind<URLToPromiseResponseFunction>() with singleton {
        { url -> window.fetch(url) }
    }
    bind<UrlProvider>() with singleton {
        object : UrlProvider {
            override fun getBaseUrl(): String = "https://my-json-server.typicode.com"
        }
    }

    bind<VideoService> {singleton { VideoService(instance(), instance()) }}

}