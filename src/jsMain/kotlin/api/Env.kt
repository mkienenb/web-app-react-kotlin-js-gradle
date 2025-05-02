package api

object Env {
    val serviceVideoUrl: String?
        get() = testServiceVideoUrl
            ?: (js("process.env.URL") as? String?)

    var testServiceVideoUrl: String? = null
}
