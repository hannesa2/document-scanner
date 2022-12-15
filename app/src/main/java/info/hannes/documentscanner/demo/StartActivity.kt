package info.hannes.documentscanner.demo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import info.hannes.documentscanner.demo.databinding.ActivityStartBinding
import info.hannes.github.AppUpdateHelper
import info.hannes.logcat.ui.BothLogActivity

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
        binding.buttonCrash.setOnClickListener {
            Toast.makeText(this, "force crash ${info.hannes.logcat.BuildConfig.VERSIONNAME}", Toast.LENGTH_SHORT).show()
            Handler(Looper.getMainLooper()).postDelayed({ throw RuntimeException("Test Crash ${info.hannes.logcat.BuildConfig.VERSIONNAME}") }, 3000)
        }
        binding.buttonUpdate.setOnClickListener {
            AppUpdateHelper.checkWithDialog(this, BuildConfig.GIT_REPOSITORY, force = true)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.buttonSetting.setOnClickListener {
                val settingsIntent: Intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                    .putExtra(Settings.EXTRA_CHANNEL_ID, "channelAppUpdate")
                startActivity(settingsIntent)
            }
        } else
            binding.buttonSetting.visibility = View.GONE

        binding.textBuildType.text = "BuildType     : ${BuildConfig.BUILD_TYPE}"
        binding.textAppVersion.text = "App version   : ${BuildConfig.VERSION_NAME}"
        binding.textOpenCVVersion.text = "OpenCV version: ${org.opencv.BuildConfig.VERSION_NAME}"

        AppUpdateHelper.checkForNewVersion(
            this,
            BuildConfig.GIT_REPOSITORY
        )
    }

}
