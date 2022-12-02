package info.hannes.documentscanner.demo

import android.annotation.SuppressLint
import android.app.Application
import info.hannes.timber.FileLoggingTree
import timber.log.Timber

class DetectApplication : Application() {

    @SuppressLint("HardwareIds")
    override fun onCreate() {
        super.onCreate()

        val oldHandler = Thread.getDefaultUncaughtExceptionHandler()

        Thread.setDefaultUncaughtExceptionHandler { t, e ->
            @Suppress("ControlFlowWithEmptyBody")
            Timber.e(e.cause?.also { } ?: run { e })
            oldHandler?.uncaughtException(t, e)
        }

        externalCacheDir?.let {
            Timber.plant(FileLoggingTree(it, this))
        }

    }
}