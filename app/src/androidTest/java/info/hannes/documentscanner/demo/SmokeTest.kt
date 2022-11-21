package info.hannes.documentscanner.demo

import android.Manifest
import android.util.Log
import androidx.test.core.app.takeScreenshot
import androidx.test.core.graphics.writeToTestStorage
import androidx.test.espresso.Espresso
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.screenshot.captureToBitmap
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestName
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SmokeTest {

    @get:Rule
    val activityScenarioRule = activityScenarioRule<PreviewActivity>()

    // a handy JUnit rule that stores the method name, so it can be used to generate unique screenshot files per test method
    @get:Rule
    var nameRule = TestName()

    @get:Rule
    val grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    @Test
    fun smokeTestSimplyStart() {
        for (i in 3000L..5000L step 1000) {
            Thread.sleep(i)
            takeScreenshot()
                .writeToTestStorage("${javaClass.simpleName}_${nameRule.methodName}-S$i")
            try {
                Espresso.onView(ViewMatchers.isRoot())
                    .captureToBitmap()
                    .writeToTestStorage("${javaClass.simpleName}_${nameRule.methodName}-A$i")
            } catch (e: Exception) {
                Log.e("smokeTestSimplyStart", e.message.toString())
            }
        }
    }
}
