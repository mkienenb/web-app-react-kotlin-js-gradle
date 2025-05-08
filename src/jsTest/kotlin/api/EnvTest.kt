package api

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class EnvTest : ShouldSpec({
    should("return null when URL is undefined") {
        js("delete process.env.URL")
        Env.serviceVideoUrl shouldBe null
    }

    should("return null when URL is empty") {
        js("process.env.URL = ''")
        Env.serviceVideoUrl shouldBe null
    }

    should("return null when URL is blank") {
        js("process.env.URL = '   '")
        Env.serviceVideoUrl shouldBe null
    }

    should("return value when URL is non-blank") {
        js("process.env.URL = 'http://test.url'")
        Env.serviceVideoUrl shouldBe "http://test.url"
    }
})
