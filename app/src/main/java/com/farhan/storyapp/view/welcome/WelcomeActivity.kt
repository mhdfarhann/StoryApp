package com.farhan.storyapp.view.welcome

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.farhan.storyapp.databinding.ActivityWelcomeBinding
import com.farhan.storyapp.di.SettingPreferences
import com.farhan.storyapp.di.dataStore
import com.farhan.storyapp.view.login.LoginActivity
import com.farhan.storyapp.view.main.MainActivity
import com.farhan.storyapp.view.register.RegisterActivity
import kotlinx.coroutines.launch

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var settingPreferences: SettingPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingPreferences = SettingPreferences.getInstance(dataStore)

        Thread.sleep(3000)
        installSplashScreen()

        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            settingPreferences.getLoginSession().collect { user ->
                if (user.isLogin) {
                    val intent = Intent(this@WelcomeActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }

        setupView()
        setupAction()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }


    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.registerButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}