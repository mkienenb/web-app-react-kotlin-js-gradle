[![official JetBrains project](https://jb.gg/badges/official.svg)](https://confluence.jetbrains.com/display/ALL/JetBrains+on+GitHub)
[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](https://www.apache.org/licenses/LICENSE-2.0)

# Building Web Applications with React and Kotlin JS Hands-On Lab

This repository is the code corresponding to the hands-on lab Building Web Applications with React and Kotlin JS.

**You can find the code of the lab after each step outlined in the tutorial in the corresponding branch.**

## Goal: Provide starting template to develop Kotlin React apps using TDD

* Our team is committed to developing React-Kotlin/js apps using a TDD approach. 
  To this end, we are developing a starting project template to facilitate such development
  building off the Kotlin JS Hands-On Lab tutorial

## Enhancement: all dependencies updated

* All dependencies have been updated to the latest version of kotlin, kotlin-wrappers, gradle, kotest, etc.

## Enhancement: Cucumber BDD tests

* This example has been enhanced with the changes needed to run cucumber tests against the React app 
  - cucumber tests run in jvmTest rather than jsTest
  - ReactApp is started on demand during a scenario and is shut down after after each scenario

## Enhancement: react-testing-library and user-event-testing-library jsNodeTests

* Added support for using react-testing-library and user-event-testing-library kotlin wrappers from https://github.com/robertfmurdock/jsmints/
* React component tests no longer have to be run using jsBrowserTest/karma but can now run using jsNodeTest/mocha
* Running tests in mocha provides a faster and improved experience than using jsBrowserTest/karma

## Enhancement: kotlin-react-dom-test-utils-js React Component jsBrowserTests

* Added support for using kotlin-react-dom-test-utils-js
* Only run in `jsBrowserTest` task and not in `jsNodeTest`
* Downloads Chrome before running `jsBrowserTest`
  * Chrome is removed on task `clean`
* Note: this support is still here but no longer the recommended approach
