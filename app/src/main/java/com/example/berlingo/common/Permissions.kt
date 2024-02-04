package com.example.berlingo.common

import com.example.berlingo.common.logger.BaseLogger
import com.example.berlingo.common.logger.FactoryLogger

private val logger: BaseLogger = FactoryLogger.getLoggerKClass(Permissions::class)

class Permissions {
    companion object {
        const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1234
    }
}
