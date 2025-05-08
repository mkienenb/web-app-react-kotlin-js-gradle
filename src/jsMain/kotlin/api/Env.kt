package api

object Env {
    val serviceVideoUrl: String?
        get() = (js("process.env.URL") as? String).takeUnless { it.isNullOrBlank() }
}
