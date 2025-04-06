package com.example

import io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME
import org.junit.platform.suite.api.ConfigurationParameter
import org.junit.platform.suite.api.SelectClasspathResource
import org.junit.platform.suite.api.Suite

@Suite
@SelectClasspathResource("features")
// GLUE_PROPERTY_NAME setting is optional, but clears up run-time warnings
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.example.stepdefinitions")
class RunCucumberTest