package com.fakhrirasyids.notesapp.ui.splash

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.fakhrirasyids.notesapp.databinding.ActivitySplashBinding
import com.fakhrirasyids.notesapp.ui.main.MainActivity
import com.fakhrirasyids.notesapp.ui.settings.SettingPreferences
import com.fakhrirasyids.notesapp.ui.settings.SettingViewModel
import com.fakhrirasyids.notesapp.ui.settings.SettingViewModelFactory

class SplashActivity : AppCompatActivity() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private lateinit var settingViewModel: SettingViewModel
    private lateinit var binding: ActivitySplashBinding
    private val SPLASH_TIME_OUT: Long = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSetting()
        checkDarkMode()

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, SPLASH_TIME_OUT)
    }

    private fun setSetting() {
        val pref = SettingPreferences.getInstance(dataStore)
        settingViewModel = ViewModelProvider(
            this@SplashActivity,
            SettingViewModelFactory(pref)
        )[SettingViewModel::class.java]
    }

    private fun checkDarkMode() {
        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

}