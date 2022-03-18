package info.hannes.documentscanner.presentation

import android.graphics.Bitmap
import android.widget.Toast
import info.hannes.documentscanner.R
import info.hannes.documentscanner.exceptions.NullCorners

class ScannerActivity : BaseScannerActivity() {
    override fun onError(throwable: Throwable) {
        when (throwable) {
            is NullCorners -> Toast.makeText(                this,                R.string.null_corners, Toast.LENGTH_LONG            )
                .show()
            else -> Toast.makeText(this, throwable.message, Toast.LENGTH_LONG).show()
        }
    }

    override fun onDocumentAccepted(bitmap: Bitmap) {
    }

    override fun onClose() {
        finish()
    }
}
