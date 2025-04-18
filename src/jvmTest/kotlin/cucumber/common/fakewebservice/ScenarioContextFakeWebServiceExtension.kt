package cucumber.common.fakewebservice

import cucumber.common.ScenarioContext

fun ScenarioContext.fakeWebservice(fakeWebServiceName: String, pathToResponseMap: Map<String, String> = emptyMap()): FakeWebService {
    return _fakeWebServiceFactory.getFakeWebService(fakeWebServiceName, pathToResponseMap)
}