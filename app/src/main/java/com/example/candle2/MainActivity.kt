package com.example.candle1
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.candle2.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Math.round

class MainActivity : AppCompatActivity() {

    private lateinit var pLauncher: ActivityResultLauncher<Array<String>>
    var job: Job? = null


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        registerPermissionListener()
        checkWriteSettingsPermission()

    }



    override fun onStart() {
        super.onStart()


    window.decorView.apply {
        systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
    }

    val pattern = listOf(
        100L to 1000,
        100L to 840,
        100L to 330,
        100L to 820,
        100L to 890,
        100L to 100,
        100L to 850,
        100L to 780,
        100L to 385,
        100L to 690,
        100L to 810,
        100L to 30,
        100L to 850,
        100L to 800,
        100L to 940,
        100L to 255,
        100L to 930,
        100L to 70,
        100L to 50,
        100L to 910,
        100L to 70,
        100L to 860,
        100L to 70,
        100L to 810,
        100L to 70,
        100L to 680,
        100L to 7,
        100L to 310,
        100L to 15,
        100L to 380,
        100L to 650,
        100L to 480,
    )
    job =  lifecycleScope.launch {
        while (true) {
            pattern.forEach { value ->
                changeScreenBrightness(this@MainActivity, value.second)
                delay(value.first)
            }
        }
    }
    }

    private fun checkWriteSettingsPermission(){
        when{
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_SETTINGS)
                    == PackageManager.PERMISSION_GRANTED ->{
                Toast.makeText(this,"Camera run", Toast.LENGTH_LONG).show()
            }

            else -> {
                pLauncher.launch(arrayOf(Manifest.permission.WRITE_SETTINGS))
            }
        }
    }

    private fun registerPermissionListener(){
        pLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()){
            if(it[Manifest.permission.WRITE_SETTINGS] == true){
                Toast.makeText(this,"Write Settings run", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this,"Permission denied", Toast.LENGTH_LONG).show()
            }
        }
    }



    // Check whether this app has android write settings permission.
    @RequiresApi(Build.VERSION_CODES.M)
    private fun hasWriteSettingsPermission(context: Context): Boolean {
        var ret = true
        // Get the result from below code.
        ret = Settings.System.canWrite(context)
        return ret
    }

    // Start can modify system settings panel to let user change the write
    // settings permission.
    private fun changeWriteSettingsPermission(context: Context) {
        val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
        context.startActivity(intent)
    }

    // This function only take effect in real physical android device,
    // it can not take effect in android emulator.
    private fun changeScreenBrightness(context: Context, screenBrightnessValue: Int) {
        // Change the screen brightness change mode to manual.
        Settings.System.putInt(
            context.contentResolver,
            Settings.System.SCREEN_BRIGHTNESS_MODE,
            Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL
        )
        // Apply the screen brightness value to the system, this will change
        // the value in Settings ---> Display ---> Brightness level.
        // It will also change the screen brightness for the device.
        Settings.System.putInt(
            context.contentResolver,
            Settings.System.SCREEN_BRIGHTNESS, screenBrightnessValue
        )
    }
}