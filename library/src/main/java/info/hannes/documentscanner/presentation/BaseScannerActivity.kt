package info.hannes.documentscanner.presentation

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import info.hannes.documentscanner.R
import info.hannes.documentscanner.data.OpenCVLoader
import info.hannes.documentscanner.databinding.ActivityScannerBinding
import info.hannes.documentscanner.extensions.outputDirectory
import info.hannes.documentscanner.extensions.triggerFullscreen
import timber.log.Timber
import java.io.File

abstract class BaseScannerActivity : AppCompatActivity() {
    private lateinit var viewModel: ScannerViewModel
    private lateinit var binding: ActivityScannerBinding
    private var isCameraPermissionGranted = true
    private var isExternalStorageStatsPermissionGranted = true

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val bitmapUri = result.data?.extras?.getString("croppedPath") ?: error("invalid path")

                val image = File(bitmapUri)
                val bmOptions = BitmapFactory.Options()
                val bitmap = BitmapFactory.decodeFile(image.absolutePath, bmOptions)
                onDocumentAccepted(bitmap)

                image.delete()
            } else {
                viewModel.onViewCreated(OpenCVLoader(this), this, binding.viewFinder)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        triggerFullscreen()

        binding = ActivityScannerBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val viewModel: ScannerViewModel by viewModels()

        viewModel.isBusy.observe(this) { isBusy ->
            binding.progress.visibility = if (isBusy) {
                View.VISIBLE
            } else {
                View.INVISIBLE
            }
        }

        viewModel.lastUri.observe(this) {
            val intent = Intent(this, CropperActivity::class.java)
            intent.putExtra("lastUri", it.toString())

            resultLauncher.launch(intent)
        }

        viewModel.errors.observe(this) {
            onError(it)
            Timber.e(it.message, it)
        }

        viewModel.corners.observe(this) {
            it?.let { corners ->
                binding.hud.onCornersDetected(corners)
            } ?: run {
                binding.hud.onCornersNotDetected()
            }
        }

        viewModel.flashStatus.observe(this) { status ->
            binding.flashToggle.setImageResource(
                when (status) {
                    FlashStatus.ON -> R.drawable.ic_flash_on
                    FlashStatus.OFF -> R.drawable.ic_flash_off
                    else -> R.drawable.ic_flash_off
                }
            )
        }

        binding.flashToggle.setOnClickListener {
            viewModel.onFlashToggle()
        }

        binding.shutter.setOnClickListener {
            viewModel.onTakePicture(outputDirectory, this)
        }

        binding.closeScanner.setOnClickListener {
            closePreview()
        }
        this.viewModel = viewModel

        checkCameraPermissions()
        checkExternalStoragePermissions()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onViewCreated(OpenCVLoader(this), this, binding.viewFinder)
    }

    private fun checkCameraPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            isCameraPermissionGranted = false
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                Toast.makeText(this, "Enable camera permission from settings", Toast.LENGTH_SHORT).show()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), PERMISSIONS_REQUEST_CAMERA)
            }
        } else {
            if (!isCameraPermissionGranted) {
                // currently nothing to do
            } else {
                isCameraPermissionGranted = true
            }
        }
    }

    private fun closePreview() {
        binding.rootView.visibility = View.GONE
        viewModel.onClosePreview()
        finish()
    }

    private fun checkExternalStoragePermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            isExternalStorageStatsPermissionGranted = false
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "Enable external storage permission", Toast.LENGTH_SHORT).show()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), PERMISSIONS_REQUEST_EXTERNAL_STORAGE)
            }
        } else {
            if (!isExternalStorageStatsPermissionGranted) {
                isExternalStorageStatsPermissionGranted = true
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSIONS_REQUEST_CAMERA -> onRequestCamera(grantResults)
            PERMISSIONS_REQUEST_EXTERNAL_STORAGE -> onRequestExternalStorage(grantResults)
            else -> {
            }
        }
    }

    private fun onRequestCamera(grantResults: IntArray) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // currently nothing to do
        } else {
            Toast.makeText(this, getString(R.string.permission_denied_camera_toast), Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun onRequestExternalStorage(grantResults: IntArray) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, getString(R.string.permission_denied_external_storage_toast), Toast.LENGTH_SHORT).show()
        }
    }

    abstract fun onError(throwable: Throwable)
    abstract fun onDocumentAccepted(bitmap: Bitmap)
    abstract fun onClose()

    companion object {
        private const val PERMISSIONS_REQUEST_CAMERA = 101
        private const val PERMISSIONS_REQUEST_EXTERNAL_STORAGE = 102
    }
}
