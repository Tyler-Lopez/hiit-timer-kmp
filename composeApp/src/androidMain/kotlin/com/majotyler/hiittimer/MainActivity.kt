package com.majotyler.hiittimer

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import com.majotyler.hiittimer.data.repository.AndroidStravaTokenStorageRepository
import com.majotyler.hiittimer.presentation.common.navigation.NavigationRoot
import com.majotyler.hiittimer.presentation.platform.AndroidUrlOpener

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val uri: Uri? = intent?.data
        val accessCode: String? = uri?.getQueryParameter("code")

        println("Strava access code: $accessCode")

        val stravaTokenStorageRepository = AndroidStravaTokenStorageRepository(context = this)

        setContent {
            MaterialTheme {
                NavigationRoot(
                    urlOpener = AndroidUrlOpener(context = this),
                    stravaAccessCode = accessCode,
                    stravaTokenStorageRepository = stravaTokenStorageRepository,
                )
            }
        }
    }
}
