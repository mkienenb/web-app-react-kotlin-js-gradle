package confexplorer

import api.URLToPromiseResponseFunction
import api.UrlProvider
import api.VideoService
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

val productionModule = DI.Module("production") {
    bind<URLToPromiseResponseFunction>() with singleton {
        { url -> window.fetch(url) }
    }
    bind<UrlProvider>() with singleton {
        object : UrlProvider {
            override fun getBaseUrl(): String = "https://my-json-server.typicode.com"
        }
    }
    bind<CoroutineScope>() with instance(MainScope())

    bind<VideoService> {singleton { VideoService(instance(), instance()) }}

}