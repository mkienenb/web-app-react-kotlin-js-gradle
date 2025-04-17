package cucumber.common.driver

import cucumber.common.ScenarioContext

enum class Protocol(prefix: String) {
    @Suppress("HttpUrlsUsage")
    HTTP ( "http://"),
    HTTPS ( "https://")
}

fun ScenarioContext.baseUrl(): String {
    val port = applicationPort ?: throw IllegalStateException("Application port is not set")
    return "${Protocol.HTTP}localhost:$port"
}