package api

object Env {
    val serviceVideoUrl: String
        get() = testServiceVideoUrl
            ?: (js("process.env.URL") as? String
                 ?: error("URL is not set"))

    var testServiceVideoUrl: String? = null
}
