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
import com.iuturakulov.openweatherapp.utils.STORAGE_NAME
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreference: SharedPreferencesStorage
    private lateinit var editor: SharedPreferences.Editor
    /*
      private lateinit var navController: NavController*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPreference = SharedPreferencesStorage(this)
        editor = this.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE).edit()
        editor.commit()
        if (sharedPreference.getData("app_theme").isNotEmpty()) {
            changeLanguage()
        }
       /* OptionsSheet().show(this) {
            title("Text message")
            with(
                Option(R.drawable.ic_baseline_texture_24, getString(R.string.change_theme_text)),
                Option(R.drawable.ic_baseline_language_24, getString(R.string.language_settings)),
            )
            onPositive { index: Int, option: Option ->
                if (index == 0) {
                    sharedPreference.saveBool(
                        "USE_DARK_THEME",
                        !sharedPreference.getBool("USE_DARK_THEME")
                    )
                    editor.commit()
                    setTheme(
                        if (sharedPreference.getBool(
                                "USE_DARK_THEME"
                            )
                        ) R.style.AppTheme_Dark else R.style.AppTheme
                    )
                    setContentView(R.layout.activity_main)
                } else {
                    changeLanguage()
                    if (sharedPreference.getData("app_theme") == "RU") {
                        sharedPreference.saveData("app_theme", "en")
                    } else {
                        sharedPreference.saveData("app_theme", "ru")
                    }
                    editor.commit()
                }
            }
        }*/
/*        navController = findNavController(R.id.nav_graph)
        setupSmoothBottomMenu()*/
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

    /* override fun onCreateOptionsMenu(menu: Menu): Boolean {
         menuInflater.inflate(R.menu.main_menu, menu)
         return true
     }

     override fun onOptionsItemSelected(item: MenuItem): Boolean {
         when (item.itemId) {
             R.id.first_fragment -> {
                 showToast("Weather fragment selected")
                 Timber.i("Weather fragment selected")
             }

             R.id.second_fragment -> {
                 showToast("Settings fragment selected")
                 Timber.i("Weather fragment selected")
             }
         }
         return super.onOptionsItemSelected(item)
     }

     private fun showToast(msg: String) {
         Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
     }

     private fun setupSmoothBottomMenu() {
         val popupMenu = PopupMenu(this, null)
         popupMenu.inflate(R.menu.main_menu)
         bottomBar.setupWithNavController(popupMenu.menu, navController)
     }*/
}
