package com.iuturakulov.openweatherapp.view.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.iuturakulov.openweatherapp.R
import com.iuturakulov.openweatherapp.model.storage.SharedPreferencesStorage
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreference: SharedPreferencesStorage
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPreference = SharedPreferencesStorage(this)
        editor = this.getSharedPreferences("OpenWeatherApp", Context.MODE_PRIVATE).edit()
        editor.commit()
        if (sharedPreference.getData("app_theme").isNotEmpty()) {
            changeLanguage()
        }
    }

    private fun changeLanguage() {
        val res: Resources = this.resources
        val dm: DisplayMetrics = res.displayMetrics
        val conf: Configuration = res.configuration
        conf.locale = Locale(if (sharedPreference.getData("app_theme") != "RU") "en" else "ru")
        res.updateConfiguration(conf, dm)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val navHostFrag = supportFragmentManager.fragments.first() as? NavHostFragment
        navHostFrag.let {
            val fragments = it?.childFragmentManager?.fragments
            fragments?.forEach { fragment ->
                fragment.onActivityResult(requestCode, resultCode, data)
            }
        }
    }
}
