package com.example.berlingo.settings.data_privacy

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.example.berlingo.R
import com.example.berlingo.common.logger.BaseLogger
import com.example.berlingo.common.logger.FactoryLogger
import com.example.berlingo.ui.theme.DarkGray
import com.example.berlingo.ui.theme.LightGray
import com.example.berlingo.ui.theme.isDarkMode

private val logger: BaseLogger = FactoryLogger.getLoggerCompose("DataPrivacySettingsScreen()")

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("CoroutineCreationDuringComposition", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DataPrivacySettingsScreen(navController: NavHostController) {
    val context = LocalContext.current
    val textColor = if (isDarkMode()) LightGray else DarkGray
    val backgroundColor = if (isDarkMode()) DarkGray else LightGray
    Scaffold(topBar = { SetTopAppBar(navController, backgroundColor) }) {
        Surface(color = backgroundColor, modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
            ) {
                WebViewWithDeepLink(context = context, url = "https://www.vbb.de/app/datenschutz/")
            }
        }
    }
}

@Composable
fun WebViewWithDeepLink(context: Context, url: String) {
    AndroidView(factory = { ctx ->
        WebView(ctx).apply {
            webViewClient = object : WebViewClient() {
                @Deprecated("Deprecated in Java")
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    url?.let {
                        if (it.startsWith("https://")) {
                            // Load the URL inside the WebView for standard http/https links
                            view?.loadUrl(it)
                        } else {
                            // Handle deep link
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                            context.startActivity(intent)
                        }
                    }
                    return true
                }
            }
            loadUrl(url)
        }
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SetTopAppBar(navController: NavHostController, backgroundColor: Color) {
    TopAppBar(
        title = { Text(stringResource(R.string.data_privacy)) },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(backgroundColor),
    )
}
