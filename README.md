[![official JetBrains project](https://jb.gg/badges/official.svg)](https://confluence.jetbrains.com/display/ALL/JetBrains+on+GitHub)
[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](https://www.apache.org/licenses/LICENSE-2.0)

# Building Web Applications with React and Kotlin JS Hands-On Lab

This repository is the code corresponding to the hands-on lab Building Web Applications with React and Kotlin JS.

**You can find the code of the lab after each step outlined in the tutorial in the corresponding branch.**

## Enhancement: Cucumber BDD tests

* This example has been enhanced with the changes needed to run cucumber tests against the React app 
  - cucumber tests run in jvmTest rather than jsTest
  - ReactApp is started from the commandline on a random port by a gradle task and shut down after the tests run
  - The ReactApp port is available to the cucumber tests under the System property "org.example.reactPort"
