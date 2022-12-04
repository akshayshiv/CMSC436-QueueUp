package com.example.queueup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.ui.AppBarConfiguration
import com.example.queueup.databinding.NavDrawerBinding
import com.google.android.material.navigation.NavigationView


class NavDrawerActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: NavDrawerBinding
    private lateinit var navView : NavigationView
    private lateinit var drawerLayout : DrawerLayout
    private lateinit var mDrawer: NavigationView
    private lateinit var fragment: Fragment
    private var arr: MutableList<String> = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        if(savedInstanceState != null){
            arr = savedInstanceState.getSerializable("list") as MutableList<String>
        } else {
            if(arr != null) {
                //do nothing
            } else {
                arr = ArrayList()
            }
        }

        super.onCreate(savedInstanceState)
        binding = NavDrawerBinding.inflate(layoutInflater)
        Log.i("debug", "nav_drawer")
        setContentView(binding.root)

        // Set a Toolbar to replace the ActionBar.
        setSupportActionBar(binding.appBarMain.toolbar)

        // This will display an Up icon (<-), we will replace it with hamburger later
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        drawerLayout = binding.drawerLayout
        navView = binding.navView

        // Find our drawer view
        //var nvDrawer = findViewById<View>(R.id.nvView) as NavigationView
        // Setup drawer view
        //setupDrawerContent(nvDrawer)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.dashboard_template
            ), drawerLayout
        )


//        val fragmentClass: Class<*> = TicketPurchaseFragment::class.java


        binding.fragmentDashboard.concert1.setOnClickListener {

            val intent = Intent(this, TicketPurchaseActivity::class.java)
            intent.putExtra("myArg", "concert1")
            startActivity(intent)
            arr.add("Hip Hop Concert")

        }
        binding.fragmentDashboard.concert2.setOnClickListener {

            val intent = Intent(this, TicketPurchaseActivity::class.java)
            intent.putExtra("myArg", "concert2")
            startActivity(intent)
            arr.add("Rock Concert")

        }
        /* Edit back to after implementing button for account info */
//        binding.concert3.setOnClickListener {
//            findNavController().navigate(
//                R.id.action_DashboardFragment_to_concert3
//            )
//            arr.add("Country Concert")
//
//        }

        binding.fragmentDashboard.concert3.setOnClickListener {

            val intent = Intent(this, TicketPurchaseActivity::class.java)
            intent.putExtra("myArg", "concert3")
            startActivity(intent)
            arr.add("Country Concert")

        }


    }

    override fun onResume() {
        super.onResume()

        update()
    }

    private fun update(){
        Log.i("dash", "Dashboard-update")
        if(arr.size == 0){
            return
        }
        val text: TextView = binding.fragmentDashboard.root.findViewById(R.id.MyEvents)
        text.text = "\n " + arr[arr.size - 1]

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



    override fun onStart() {
        super.onStart()
        // Find our drawer view
        //var nvDrawer = findViewById<View>(R.id.nvView) as NavigationView
        // Setup drawer view
        //setupDrawerContent(nvDrawer)

        setContentView(binding.root)

        // Set a Toolbar to replace the ActionBar.
        setSupportActionBar(binding.appBarMain.toolbar)

//        val navHostFragment =
//            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
//        val navController = navHostFragment.navController



//        navController.navigate(
//            R.id.fragment_dashboard // change this to something else
//            )

        binding.root.findViewById<NavigationView>(R.id.nav_view).getHeaderView(0).findViewById<Button>(R.id.account)
            .setOnClickListener {

            val intent = Intent(this, AccountInfoActivity::class.java)
            startActivity(intent)

//            supportFragmentManager.beginTransaction().add(android.R.id.content, AccountInfo::class.java.newInstance()).commit()


        }

        navView.setNavigationItemSelectedListener {
            Log.i("nav", "it_val" + it.itemId.toString())
            Log.i("nav", "nav_logout" + R.id.logout.toString())

            when(it.itemId) {


                R.id.logout -> {
                    finish()
                }

                R.id.home -> {
//                    Toast.makeText(
//                        (applicationContext),
//                        "HOME!",
//                        Toast.LENGTH_LONG
//                    ).show()
                    binding.root.findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawers()
                }

            }

            true
        }

    }
    private fun setupDrawerContent(navigationView: NavigationView) {
        navigationView.setNavigationItemSelectedListener { menuItem ->
            selectDrawerItem(menuItem)
            true
        }
    }

    fun selectDrawerItem(menuItem: MenuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
//        var fragment: Fragment? = null
//        val fragmentClass: Class<*>
//        Log.i("debug", "clicked")
//        fragmentClass = when (menuItem.itemId) {
//            R.id.nav_dashboard -> Dashboard::class.java
//            R.id.nav_test -> LoginFragment::class.java
//            else -> Dashboard::class.java
//        }
//        try {
//            fragment = fragmentClass.newInstance() as Fragment
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//
//        // Insert the fragment by replacing any existing fragment
//        val fragmentManager: FragmentManager = supportFragmentManager
//        if (fragment != null) {
//            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit()
//        }

        // Highlight the selected item has been done by NavigationView
        menuItem.isChecked = true
        // Set action bar title
        title = menuItem.title
        // Close the navigation drawer
        //mDrawer.closeDrawer()
    }
}
