package com.example.berlingo.common.extensions

fun String.replaceWhiteSpaceWithPlus(): String {
    return this.replace(" ", "+")
}
