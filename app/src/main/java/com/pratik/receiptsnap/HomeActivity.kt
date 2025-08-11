package com.pratik.receiptsnap

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.pratik.receiptsnap.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomBar = binding.bottomBar // direct view binding se
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        bottomBar.onTabSelected = { tab ->
            val allowedTabs = setOf(R.id.scanFragment, R.id.organizeFragment, R.id.settingsFragment)
            if (tab.id in allowedTabs && navController.currentDestination?.id != tab.id) {
                try {
                    navController.navigate(tab.id, null, navOptions {
                        launchSingleTop = true
                        popUpTo(R.id.scanFragment) { saveState = true }
                        restoreState = true
                    })
                } catch (e: IllegalArgumentException) {
                    Timber.tag("MainActivity").e(e, "Invalid tab id: ${tab.id}")
                }
            }
        }

        bottomBar.onTabReselected = { tab ->
            // pop everything above the tab's root (keeps the root fragment)
            navController.popBackStack(tab.id, false)
        }


        navController.addOnDestinationChangedListener { _, destination, _ ->
            val hideOn = setOf(R.id.splashFragment, R.id.loginFragment, R.id.signupFragment)
            if (destination.id in hideOn) {
                bottomBar.visibility = View.GONE
            } else {
                bottomBar.visibility = View.VISIBLE
                // highlight correct tab if destination is a dashboard fragment
                if (destination.id in setOf(R.id.scanFragment, R.id.organizeFragment, R.id.settingsFragment)) {
                    try {
                        bottomBar.selectTabById(destination.id)
                    } catch (_: Exception) {}
                }
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
                bottomBar.visibility = View.GONE
            } else {
                // Keyboard close → show bottom nav (only if destination allows)
                val currentDestId = navController.currentDestination?.id
                if (currentDestId != R.id.splashFragment &&
                    currentDestId != R.id.loginFragment &&
                    currentDestId != R.id.signupFragment) {
                    bottomBar.visibility = View.VISIBLE
                }
            }
        }
    }

}