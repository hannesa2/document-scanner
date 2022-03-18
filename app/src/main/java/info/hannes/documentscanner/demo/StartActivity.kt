package info.hannes.documentscanner.demo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import info.hannes.documentscanner.demo.databinding.ActivityStartBinding
import info.hannes.logcat.BothLogActivity

class StartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStartBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.buttonScan.setOnClickListener { startActivity(Intent(this, PreviewActivity::class.java)) }
        binding.buttonLog.setOnClickListener { startActivity(Intent(this, BothLogActivity::class.java)) }

        binding.textBuildType.text = "BuildType     : ${BuildConfig.BUILD_TYPE}"
        binding.textAppVersion.text = "App version   : ${BuildConfig.VERSION_NAME}"
        binding.textOpenCVVersion.text = "OpenCV version: ${org.opencv.BuildConfig.VERSION_NAME}"
    }

}
