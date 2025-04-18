package cucumber.common.driver

import cucumber.common.ScenarioContext

enum class Protocol(val prefix: String) {
    @Suppress("HttpUrlsUsage")
    HTTP ( "http://"),
    HTTPS ( "https://")
}

fun ScenarioContext.baseUrl(): String {
    val port = _reactApplication.port ?: throw IllegalStateException("Application port is not set")
    return "${Protocol.HTTP.prefix}localhost:$port"
}

fun ScenarioContext.startReactApp(environmentVariables: Map<String, String> = emptyMap()) {
    _reactApplication.start(environmentVariables)
}

fun ScenarioContext.addReactAppEnvironmentVariable(envName: String, envValue: String) {
    _reactApplication.addEnvironmentVariable(envName, envValue)
}
