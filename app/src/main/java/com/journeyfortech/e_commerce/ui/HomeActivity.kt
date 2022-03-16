package com.journeyfortech.e_commerce.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.journeyfortech.e_commerce.R
import com.journeyfortech.e_commerce.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var appBarConfiguration: AppBarConfiguration


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_ECommerce)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val toolbar = binding.root.findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val bottomNavigationView =
            binding.root.findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_cart,
                R.id.nav_favourite,
                R.id.nav_user,
            )
        )

        bottomNavigationView.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    fun hideBottomNavigation() {
        binding.root.findViewById<BottomNavigationView>(R.id.bottomNavigationView).visibility =
            View.GONE
    }

    fun showBottomNavigation() {
        binding.root.findViewById<BottomNavigationView>(R.id.bottomNavigationView).visibility =
            View.VISIBLE
    }

    fun hideToolBar(){
        binding.root.findViewById<Toolbar>(R.id.toolbar).visibility = View.GONE
    }

    fun showToolBar(){
        binding.root.findViewById<Toolbar>(R.id.toolbar).visibility = View.VISIBLE
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun displayProgressBar(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.root.findViewById<ProgressBar>(R.id.loadingProgressBar).visibility = View.VISIBLE
        } else {
            binding.progressBar.root.findViewById<ProgressBar>(R.id.loadingProgressBar).visibility = View.GONE
        }
    }
}