package reactdi

import api.URLToPromiseResponseFunction
import api.VideoService
import api.UrlProvider
import kotlinx.coroutines.CoroutineScope
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import org.w3c.fetch.Response
import org.w3c.fetch.ResponseInit
import org.w3c.files.Blob
import org.w3c.files.BlobPropertyBag
import kotlin.js.Promise

class DITestBuilder {
    var scope: CoroutineScope? = null
    var videoServiceFetchFunction: URLToPromiseResponseFunction? = null

    fun build(): DI {
        val finalScope = scope ?: error("DITestBuilder.scope must be set before build()")
        val fetchFunction = videoServiceFetchFunction ?: defaultFetchFunction()

        return DI {
            bind<URLToPromiseResponseFunction>() with singleton { fetchFunction }
            bind<UrlProvider>() with singleton {
                object : UrlProvider {
                    override fun getBaseUrl(): String = "http://localhost"
                }
            }
            bind<VideoService>() with singleton { VideoService(instance(), instance()) }
            bind<CoroutineScope>() with instance(finalScope)
        }
    }

    private fun defaultFetchFunction(): URLToPromiseResponseFunction = {
        val blob = Blob(arrayOf("{}"), BlobPropertyBag(type = "application/json"))
        val responseInit = ResponseInit(status = 404)
        Promise.resolve(Response(blob, responseInit))
    }
}
