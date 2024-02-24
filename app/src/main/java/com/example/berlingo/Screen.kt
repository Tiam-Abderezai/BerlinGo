package com.example.berlingo

sealed class Screen(val route: String) {
    object Journeys : Screen("Journeys")
    object Settings : Screen("Settings")
    object Maps : Screen("Maps")
    object AppInfo : Screen("AppInfo")
    object Language : Screen("Language")
    object DarkMode : Screen("DarkMode")
    object DataPrivacy : Screen("DataPrivacy")
}