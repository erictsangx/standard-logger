package com.etx.logger

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.OutputStreamAppender
import org.jetbrains.spek.api.Spek
import org.slf4j.LoggerFactory
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.io.PrintWriter
import java.io.StringWriter
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class StandardLoggerSpec : Spek({

    describe("StandardLogger") {

        val stream = ByteArrayOutputStream()
        val ps = PrintStream(stream)
        val context = LoggerFactory.getILoggerFactory() as LoggerContext

        val encoder = PatternLayoutEncoder()
        encoder.setContext(context)
        encoder.setPattern("%-5level - %msg%n")
        encoder.start()

        val appender = OutputStreamAppender<ILoggingEvent>()
        appender.setName("OutputStream Appender")
        appender.setEncoder(encoder)
        appender.setContext(context)
        appender.setOutputStream(ps)

        appender.start()


        val log = LoggerFactory.getLogger(StandardLoggerSpec::class.java) as ch.qos.logback.classic.Logger
        log.addAppender(appender)


        beforeEach {
            stream.reset()
        }

        it("should trace") {

            log.level = Level.TRACE
            val subject = StandardLogger.getLogger(StandardLoggerSpec::class)

            val uuid = UUID.randomUUID().toString()
            subject.trace { uuid }
            assertTrue(subject.isTraceEnabled)
            assertTrue(subject.isDebugEnabled)
            assertTrue(subject.isInfoEnabled)
            assertTrue(subject.isWarnEnabled)
            assertTrue(subject.isErrorEnabled)
            assertEquals("${Level.TRACE} - $uuid\n", stream.toString())

        }

        it("should debug") {

            log.level = Level.DEBUG
            val subject = StandardLogger.getLogger(StandardLoggerSpec::class)

            val uuid = UUID.randomUUID().toString()
            subject.debug { uuid }
            assertFalse(subject.isTraceEnabled)
            assertTrue(subject.isDebugEnabled)
            assertTrue(subject.isInfoEnabled)
            assertTrue(subject.isWarnEnabled)
            assertTrue(subject.isErrorEnabled)
            assertEquals("${Level.DEBUG} - $uuid\n", stream.toString())

        }

        it("should info") {

            log.level = Level.INFO
            val subject = StandardLogger.getLogger(StandardLoggerSpec::class)

            val uuid = UUID.randomUUID().toString()
            subject.info { uuid }
            assertFalse(subject.isTraceEnabled)
            assertFalse(subject.isDebugEnabled)
            assertTrue(subject.isInfoEnabled)
            assertTrue(subject.isWarnEnabled)
            assertTrue(subject.isErrorEnabled)
            assertEquals("${Level.INFO}  - $uuid\n", stream.toString())

        }

        it("should warn") {

            log.level = Level.WARN
            val subject = StandardLogger.getLogger(StandardLoggerSpec::class)

            val uuid = UUID.randomUUID().toString()
            subject.warn { uuid }
            assertFalse(subject.isTraceEnabled)
            assertFalse(subject.isDebugEnabled)
            assertFalse(subject.isInfoEnabled)
            assertTrue(subject.isWarnEnabled)
            assertTrue(subject.isErrorEnabled)
            assertEquals("${Level.WARN}  - $uuid\n", stream.toString())

        }

        it("should error") {

            log.level = Level.ERROR
            val subject = StandardLogger.getLogger(StandardLoggerSpec::class)

            val uuid = UUID.randomUUID().toString()
            subject.error { uuid }
            assertFalse(subject.isTraceEnabled)
            assertFalse(subject.isDebugEnabled)
            assertFalse(subject.isInfoEnabled)
            assertFalse(subject.isWarnEnabled)
            assertTrue(subject.isErrorEnabled)
            assertEquals("${Level.ERROR} - $uuid\n", stream.toString())

        }

        it("should print object") {

            log.level = Level.DEBUG
            val subject = StandardLogger.getLogger(StandardLoggerSpec::class)

            val list = listOf("a", "b")
            subject.debug { list }

            assertEquals("${Level.DEBUG} - $list\n", stream.toString())

        }


        it("should print exception") {

            log.level = Level.DEBUG
            val subject = StandardLogger.getLogger(StandardLoggerSpec::class)

            val ex = Exception("I am Error.")
            subject.debug { ex }

            val sw = StringWriter()
            ex.printStackTrace(PrintWriter(sw))
            assertEquals("${Level.DEBUG} - ${sw.toString()}\n", stream.toString())

        }

    }

})