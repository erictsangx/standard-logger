package com.etx.logger

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.PrintWriter
import java.io.StringWriter
import kotlin.reflect.KClass

class StandardLogger private constructor(l: Logger) : Logger by l {

    companion object {
        fun <T : Any> getLogger(clazz: KClass<T>): StandardLogger {
            return StandardLogger(LoggerFactory.getLogger(clazz.java))
        }

        fun <T : Any> getLogger(clazz: Class<T>): StandardLogger {
            return StandardLogger(LoggerFactory.getLogger(clazz))
        }
    }

    private fun normalize(obj: Any?): String {
        return if (obj is Exception) {
            printEx(obj)
        } else {
            obj.toString()
        }
    }

    private fun printEx(ex: Exception): String {
        val sw = StringWriter()
        ex.printStackTrace(PrintWriter(sw))
        return sw.toString()
    }


    fun trace(body: () -> Any?) {
        if (isTraceEnabled) {
            trace(normalize(body()))
        }
    }

    fun debug(body: () -> Any?) {
        if (isDebugEnabled) {
            debug(normalize(body()))
        }
    }

    fun info(body: () -> Any?) {
        if (isInfoEnabled) {
            info(normalize(body()))
        }
    }

    fun warn(body: () -> Any?) {
        if (isWarnEnabled) {
            warn(normalize(body()))
        }
    }

    fun error(body: () -> Any?) {
        if (isErrorEnabled) {
            error(normalize(body()))
        }
    }
}

