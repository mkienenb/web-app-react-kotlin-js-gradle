package confexplorer

import api.URLToPromiseResponseFunction
import api.UrlProvider
import org.kodein.di.*
import kotlinx.browser.window

val productionModule = DI.Module("production") {
    bind<URLToPromiseResponseFunction>() with singleton {
        { url -> window.fetch(url) }
    }
    bind<UrlProvider>() with singleton {
        object : UrlProvider {
            override fun getBaseUrl(): String = "https://my-json-server.typicode.com"
        }
    }
}