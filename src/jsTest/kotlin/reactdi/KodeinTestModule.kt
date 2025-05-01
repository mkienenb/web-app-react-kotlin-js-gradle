package reactdi

import api.URLToPromiseResponseFunction
import api.UrlProvider
import api.VideoService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import org.w3c.fetch.Response
import org.w3c.fetch.ResponseInit
import org.w3c.files.Blob
import org.w3c.files.BlobPropertyBag
import kotlin.js.Promise


fun nodeTestModule(
    fetchMock: URLToPromiseResponseFunction,
    baseUrl: String,
    videoService: VideoService = VideoService(fetchMock, object : UrlProvider {
        override fun getBaseUrl(): String = baseUrl
    }), scope : CoroutineScope = MainScope()
) = DI.Module("nodeTest") {
    bind<URLToPromiseResponseFunction>() with singleton { fetchMock}
    bind<UrlProvider>() with singleton {
        object : UrlProvider {
            override fun getBaseUrl(): String = baseUrl
        }
    }

    bind<VideoService> {singleton { VideoService(instance(), instance()) }}
    bind<CoroutineScope>() with instance(scope)
}


fun createTestDi(
    videoServiceFetchFunction: URLToPromiseResponseFunction = {
        val blob = Blob(arrayOf("{}"), BlobPropertyBag(type = "application/json"))
        val responseInit = ResponseInit(status = 404)
        Promise.resolve(Response(blob, responseInit))
    },
    baseUrl: String = "http://localhost",
    scope: CoroutineScope = MainScope()): DI {
        return DI {
            import(nodeTestModule(videoServiceFetchFunction, baseUrl, scope = scope))
        }
    }
