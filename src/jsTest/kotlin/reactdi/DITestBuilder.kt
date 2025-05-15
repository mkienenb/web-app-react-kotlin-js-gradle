import api.URLToPromiseResponseFunction
import api.VideoService
import api.UrlProvider
    var videoServiceFetchFunction: URLToPromiseResponseFunction? = null

        val fetchFunction = videoServiceFetchFunction ?: defaultFetchFunction()

            bind<URLToPromiseResponseFunction>() with singleton { fetchFunction }
            bind<UrlProvider>() with singleton {
                object : UrlProvider {
                    override fun getBaseUrl(): String = "http://localhost"
                }
            }
            bind<VideoService>() with singleton { VideoService(instance(), instance()) }

    private fun defaultFetchFunction(): URLToPromiseResponseFunction = {
        val blob = Blob(arrayOf("{}"), BlobPropertyBag(type = "application/json"))
        val responseInit = ResponseInit(status = 404)
        Promise.resolve(Response(blob, responseInit))
    }
