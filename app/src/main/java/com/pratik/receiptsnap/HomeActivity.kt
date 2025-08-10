package com.pratik.receiptsnap

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.pratik.receiptsnap.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNav = binding.bottomBar // direct view binding se
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Bottom Nav Navigation se link karo
        bottomNav.setupWithNavController(navController)

        // Hide on Splash/Login/Signup
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.splashFragment,
                R.id.loginFragment,
                R.id.signupFragment -> bottomNav.visibility = View.GONE
                else -> bottomNav.visibility = View.VISIBLE
            }
        }

        // Listen for keyboard open/close
        val rootView = findViewById<View>(android.R.id.content)
        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            rootView.getWindowVisibleDisplayFrame(rect)
            val screenHeight = rootView.rootView.height
            val keypadHeight = screenHeight - rect.bottom

            if (keypadHeight > screenHeight * 0.15) {
                // Keyboard open → hide bottom nav
                bottomNav.visibility = View.GONE
            } else {
                // Keyboard close → show bottom nav (only if destination allows)
                val currentDestId = navController.currentDestination?.id
                if (currentDestId != R.id.splashFragment &&
                    currentDestId != R.id.loginFragment &&
                    currentDestId != R.id.signupFragment) {
                    bottomNav.visibility = View.VISIBLE
                }
            }
        }
    }

}