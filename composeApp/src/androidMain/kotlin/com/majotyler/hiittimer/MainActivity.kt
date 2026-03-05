package com.majotyler.hiittimer

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import com.majotyler.hiittimer.presentation.common.navigation.NavigationRoot
import com.majotyler.hiittimer.presentation.platform.AndroidUrlOpener

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val uri: Uri? = intent?.data
        val accessToken: String? = uri?.getQueryParameter("code")

        // TODO we need to use this to make a Strava Activity
        println("Access token is $accessToken")

        setContent {
            MaterialTheme {
                NavigationRoot(
                    urlOpener = AndroidUrlOpener(context = this),
                )
            }
        }
    }
}