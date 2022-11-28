package com.example.queueup

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.ui.AppBarConfiguration
import com.example.queueup.databinding.FragmentDashboardBinding
import com.example.queueup.databinding.NavDrawerBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar

class navDrawer : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: NavDrawerBinding
    private lateinit var concertbinding: FragmentDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("debug", "navDrawer")
        binding = NavDrawerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }


        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView

        //val navController = findNavController(R.id.DashboardFragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.DashboardFragment, R.id.nav_logout
            ), drawerLayout
        )

        //setupActionBarWithNavController(navController, appBarConfiguration)
        //navView.setupWithNavController(navController)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // The action bar home/up action should open or close the drawer.
        when (item.itemId) {
            android.R.id.home -> {
                val mDrawer = findViewById<DrawerLayout>(R.id.drawer_layout)
                mDrawer.openDrawer(GravityCompat.START)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //override fun onSupportNavigateUp(): Boolean {
      //  val navController = findNavController(R.id.nav_host_fragment_content_main)
      //  return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    //}
}
