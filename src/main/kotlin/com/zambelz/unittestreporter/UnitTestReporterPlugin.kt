package com.zambelz.unittestreporter

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestListener
import org.gradle.api.tasks.testing.TestResult
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.api.tasks.testing.logging.TestLogging
import org.gradle.api.tasks.testing.logging.TestLoggingContainer

open class UnitTestReporterPlugin : Plugin<Project> {

    override fun apply(project: Project) {

        project.tasks.withType(Test::class.java) { test: Test ->

            test.testLogging { loggingContainer: TestLoggingContainer ->
                loggingContainer.events = setOf(
                    TestLogEvent.FAILED,
                    TestLogEvent.PASSED,
                    TestLogEvent.SKIPPED,
                    TestLogEvent.STANDARD_OUT
                )
                loggingContainer.exceptionFormat = TestExceptionFormat.FULL
                loggingContainer.showExceptions = true
                loggingContainer.showCauses = true
                loggingContainer.showStackTraces = true

                loggingContainer.debug { logging: TestLogging ->
                    logging.events = setOf(
                        TestLogEvent.STARTED,
                        TestLogEvent.FAILED,
                        TestLogEvent.PASSED,
                        TestLogEvent.SKIPPED,
                        TestLogEvent.STANDARD_ERROR,
                        TestLogEvent.STANDARD_OUT
                    )
                    logging.exceptionFormat = TestExceptionFormat.FULL
                }

                loggingContainer.info { logging: TestLogging ->
                    logging.events = loggingContainer.debug.events
                    logging.exceptionFormat = loggingContainer.debug.exceptionFormat
                }
            }

            test.addTestListener(object : TestListener {

                override fun beforeTest(testDescriptor: TestDescriptor) {}

                override fun beforeSuite(suite: TestDescriptor) {}

                override fun afterTest(testDescriptor: TestDescriptor, result: TestResult) {}

                override fun afterSuite(suite: TestDescriptor, result: TestResult) {

                    if (suite.parent == null) {
                        return
                    }

                    val output = result.run {
                        "Test Results: $resultType (" +
                                "$testCount tests, " +
                                "$successfulTestCount successes, " +
                                "$failedTestCount failures, " +
                                "$skippedTestCount skipped" +
                                ")"
                    }

                    val testResultLine = "|  $output  |"
                    val repeatLength = testResultLine.length
                    val separationLine = "-".repeat(repeatLength)

                    println(separationLine)
                    println(testResultLine)
                    println(separationLine)
                }

            })

            test.useJUnit()
        }
    }

}
