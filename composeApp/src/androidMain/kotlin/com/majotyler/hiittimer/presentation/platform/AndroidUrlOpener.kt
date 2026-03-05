package com.majotyler.hiittimer.presentation.platform

import android.content.Context
import android.content.Intent
import com.majotyler.hiittimer.platform.UrlOpener
import androidx.core.net.toUri

class AndroidUrlOpener(
    private val context: Context,
) : UrlOpener {
    override fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        context.startActivity(intent)
    }
}