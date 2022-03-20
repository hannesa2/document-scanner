package info.hannes.documentscanner.demo

import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import info.hannes.documentscanner.R
import info.hannes.documentscanner.exceptions.NullCorners
import info.hannes.documentscanner.presentation.BaseScannerActivity
import timber.log.Timber

class ScannerActivity : BaseScannerActivity() {
    override fun onError(throwable: Throwable) {
        when (throwable) {
            is NullCorners -> Toast.makeText(this, R.string.null_corners, Toast.LENGTH_LONG).show()
            else -> Toast.makeText(this, throwable.message, Toast.LENGTH_LONG).show()
        }
    }

    override fun onDocumentAccepted(bitmap: Bitmap) {
        Timber.d("accepted")

    }

    override fun onClose() {
        finish()
    }
}
