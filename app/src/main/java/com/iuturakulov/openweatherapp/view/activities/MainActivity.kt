package com.iuturakulov.openweatherapp.view.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.iuturakulov.openweatherapp.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  /*
    private lateinit var navController: NavController*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
/*        navController = findNavController(R.id.nav_graph)
        setupSmoothBottomMenu()*/
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val navHostFrag = supportFragmentManager.fragments.first() as? NavHostFragment
        navHostFrag.let {
            val fragments = it?.childFragmentManager?.fragments
            fragments?.forEach {fragment ->
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
