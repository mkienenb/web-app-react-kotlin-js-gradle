package cucumber.common.fakewebservice

import io.cucumber.java.After

class FakeWebServiceFactory {
    private val cache = mutableMapOf<String, FakeWebService>()

    fun getFakeWebService(fakeWebServiceName: String, pathToResponseMap: Map<String, String>? = null): FakeWebService {
        return cache.getOrPut(fakeWebServiceName) {
            checkNotNull(pathToResponseMap) { "pathToResponseMap must be provided when creating FakeWebService" }

            FakeWebService().apply {
                setPathToResponseMappings(pathToResponseMap)
                val port = serveOnPort()
                println("FakeWebService for $fakeWebServiceName started on port $port")
            }
        }
    }

    @After
    fun tearDown() {
        cache.values.forEach { it.stop() }
        cache.clear()
    }
}