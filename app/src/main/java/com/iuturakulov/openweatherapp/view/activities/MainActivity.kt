package com.iuturakulov.openweatherapp.view.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.iuturakulov.openweatherapp.MyApp
import com.iuturakulov.openweatherapp.R
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber


class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navController = findNavController(R.id.nav_graph)
        setupSmoothBottomMenu()
        (application as MyApp).appComponent.inject(this)
        // Gradient animation
   /*     val animDrawable = main_menu_info.background as AnimationDrawable
        animDrawable.setEnterFadeDuration(10)
        animDrawable.setExitFadeDuration(10000)
        animDrawable.start()*/
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
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
    }

}
