package cucumber.ksp

import com.google.devtools.ksp.processing.*

class PageHelperProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return PageHelperProcessor(environment.codeGenerator, environment.logger)
    }
}
