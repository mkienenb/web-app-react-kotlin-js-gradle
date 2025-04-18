package cucumber.common.fakewebservice

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.runBlocking

class FakeWebService {
    val url: String
        get() {
            check(port != 0) { "FakeWebService has not been started." }
            return "http://localhost:$port"
        }

    private lateinit var server: NettyApplicationEngine

    private var pathToResponseMap: Map<String, String> = emptyMap()
    private var responseForRequestedUrlResolver: (String) -> String? = { pathToResponseMap[it] }

    /** The actual port assigned; 0 until after serveMappingsOnPort() runs. */
    var port: Int = 0
        private set

    fun setPathToResponseMappings(pathToResponseMap: Map<String, String>) {
        this.pathToResponseMap = pathToResponseMap
    }

    fun setResponseForRequestedUrlResolver(resolver: (String) -> String?) {
        responseForRequestedUrlResolver = resolver
    }

    /**
     * Starts on port=0 (random), captures the real port in [port], and returns it.
     */
    fun serveOnPort(): Int {
        // configure with port=0 → OS picks a free port
        server = embeddedServer(Netty, port = 0) {
            install(CORS) {
                anyHost()                      // allow all origins
                allowMethod(HttpMethod.Get)
                allowHeader(HttpHeaders.ContentType)
            }
            routing {
                get("/{key}") {
                    val requestedPath = call.parameters["key"]!!
                    println("FakeWebService for port $port received request for [$requestedPath]")
                    val responseText = getResponse(requestedPath)
                    println("FakeWebService for port $port responded with [$responseText]")
                    call.respondText(responseText ?: "Not found", contentType = ContentType.Application.Json.withCharset(Charsets.UTF_8))
//                    call.respondText(responseText ?: "Not found", contentType = ContentType.Application.Json)
//                    call.respondText(responseText ?: "Not found")
                }
            }
        }

        // start without blocking
        server.start(wait = false)

        // resolvedConnectors() is a suspend‑fn on NettyApplicationEngine
        port = runBlocking {
            server.resolvedConnectors().first().port
        }

        println("FakeWebService listening on port $port")
        return port
    }

    private fun getResponse(requestedPath: String): String? = responseForRequestedUrlResolver(requestedPath)

    /** Stops the server cleanly. */
    fun stop(gracePeriodMillis: Long = 1_000, timeoutMillis: Long = 2_000) {
        server.stop(gracePeriodMillis, timeoutMillis)
    }
}
