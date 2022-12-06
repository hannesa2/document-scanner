package info.hannes.documentscanner.demo

import android.annotation.SuppressLint
import info.hannes.logcat.LoggingApplication
import info.hannes.timber.FileLoggingTree
import timber.log.Timber

class DetectApplication : LoggingApplication() {

    @SuppressLint("HardwareIds")
    override fun onCreate() {
        super.onCreate()

        externalCacheDir?.let {
            Timber.plant(FileLoggingTree(it, this))
        }

        Crashlytic.init(applicationContext.contentResolver)
    }
}