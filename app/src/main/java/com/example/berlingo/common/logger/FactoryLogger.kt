package com.example.berlingo.common.logger

import kotlin.reflect.KClass

object FactoryLogger {
    private fun getLogger(tag: String): BaseLogger = AppLogger(tag)

    // Convenience method for getting a logger with a class name as the tag
    fun getLoggerKClass(cls: KClass<*>): BaseLogger = getLogger(cls.java.simpleName)
    fun getLoggerCompose(compose: String): BaseLogger = getLogger(compose)
}