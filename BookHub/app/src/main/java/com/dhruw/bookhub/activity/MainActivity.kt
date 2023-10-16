package com.dhruw.bookhub.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.dhruw.bookhub.R
import com.dhruw.bookhub.fragment.DashboardFragment
import com.dhruw.bookhub.fragment.FavouriteFragment
import com.dhruw.bookhub.fragment.ProfileFragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var frameLayout: FrameLayout
    lateinit var navigationView: NavigationView


    var prevMenuItem : MenuItem ? = null



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawerLayout)
        coordinatorLayout = findViewById(R.id.coordinatorLayout)
        frameLayout = findViewById(R.id.frame)
        navigationView = findViewById(R.id.navigationView)

        setUpToolbar()
        openDashboard()            // Now dashboard is a home page for app

        val actionBarDrawerToggle = ActionBarDrawerToggle(this@MainActivity,
            drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )

        drawerLayout.addDrawerListener(actionBarDrawerToggle) //hamburger
        actionBarDrawerToggle.syncState() // back arrow





        //Action listener on items

        navigationView.setNavigationItemSelectedListener {


            if (prevMenuItem != null)
            {
                prevMenuItem?.isChecked = false
            }

            it.isCheckable = true
            it.isChecked = true
            prevMenuItem  = it

            when(it.itemId)
            {
                 R.id.dashboard ->{

                      openDashboard()
                      supportActionBar?.title = "Dashboard"// Change the home title
                      drawerLayout.closeDrawers()

                 }
                 R.id.favourite ->{


                      supportFragmentManager.beginTransaction()
                          .replace(R.id.frame, FavouriteFragment())
                          .commit()

                     supportActionBar?.title = "Favourites"// Change the home title
                     drawerLayout.closeDrawers()


                 }
                 R.id.profile ->{

                      supportFragmentManager.beginTransaction()
                          .replace(R.id.frame, ProfileFragment())
                          .commit()

                     supportActionBar?.title = "Profile"   //1 Change the home title

                     drawerLayout.closeDrawers()

                 }
                 R.id.aboutApp ->{

                     Toast.makeText(this@MainActivity, "Clicked on About App", Toast.LENGTH_SHORT).show()
                 }
            }
            return@setNavigationItemSelectedListener true
        }

    }



    fun setUpToolbar()
    {
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.title = "Toolbar Title"
        supportActionBar?.setDisplayUseLogoEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // forward arrow MenuItem
    }


    // action on hamburger icon
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

         val id = item.itemId

        if(id == android.R.id.home)
        {
             drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }




    // set Dashboard as a home

    fun openDashboard()
    {
         val fragment = DashboardFragment()
         val transaction = supportFragmentManager.beginTransaction()
         transaction.replace(R.id.frame, fragment)
         transaction.commit()
        supportActionBar?.title = "Dashboard"

        navigationView.setCheckedItem(R.id.dashboard)

    }




    //For back press functionality we sue onBackPressed()
    override fun onBackPressed() {
       // super.onBackPressed()


        var frag = this.supportFragmentManager.findFragmentById(R.id.frame)

           when(frag)
           {
                   !is DashboardFragment -> openDashboard()

                   else ->super.onBackPressed()
           }
    }





}