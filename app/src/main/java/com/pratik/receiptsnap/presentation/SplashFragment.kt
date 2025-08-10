package com.pratik.receiptsnap.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.pratik.receiptsnap.R
import com.pratik.receiptsnap.data.local.UserPreferences
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {

    private val userPrefs: UserPreferences by lazy {
        UserPreferences(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            val token = userPrefs.getToken()

            view.postDelayed({
                val fadeOut = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_out)
                fadeOut.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation) {}

                    override fun onAnimationEnd(animation: Animation) {
                        // Navigate after animation finishes
                        if (token.isNullOrEmpty()) {
                            findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                        } else {
                            findNavController().navigate(R.id.action_splashFragment_to_scanFragment)
                        }
                    }

                    override fun onAnimationRepeat(animation: Animation) {}
                })
                view.startAnimation(fadeOut)
            }, 2000)
        }
    }
}

