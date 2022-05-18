package com.mando.foxyweatherapp

import android.content.res.Configuration
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.gauravk.bubblenavigation.BubbleNavigationLinearView
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mando.foxyweatherapp.favouritesScreen.view.FavouritesScreen.FavouritesFragment
import com.mando.foxyweatherapp.homeScreen.view.HomeFragment
import com.mando.foxyweatherapp.alertsScreen.view.AlertsFragment
import com.mando.foxyweatherapp.settingsScreen.SettingsFragment
import com.mando.foxyweatherapp.utitlity.getCurrentLocale
import java.util.*

class MainActivity : AppCompatActivity() {
    var bubbleNavigationLinearView: BottomNavigationView? = null
    lateinit var constraintLayout: ConstraintLayout

    private var onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            val id = item.itemId
            var fragment: Fragment? = null
            when (id) {
                R.id.homeToggle -> {
                    fragment = HomeFragment()
                }
                R.id.favToggle -> {
                    fragment = FavouritesFragment()
                }
                R.id.notificationsToggle -> {
                    fragment = AlertsFragment()
                }
                R.id.settingsToggle -> {
                    fragment = SettingsFragment()
                }
            }
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment!!)
                .commit()
            true
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val localLang = getCurrentLocale(this)
        val languageLocale = com.mando.foxyweatherapp.utitlity.getSharedPreferences(this).getString(
            getString(R.string.languageSetting), localLang?.language) ?: localLang?.language
        setLocale(languageLocale!!)
        bubbleNavigationLinearView = findViewById(R.id.bottom_nav)
        bubbleNavigationLinearView?.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        bubbleNavigationLinearView?.setSelectedItemId(R.id.homeToggle)
        bubbleNavigationLinearView?.getMenu()?.clear();
        bubbleNavigationLinearView?.inflateMenu(R.menu.bottom_nav_menu);

        constraintLayout = findViewById(R.id.homeScreenLayout)
        constraintLayout.background = resources.getDrawable(R.drawable.day)

    }
    private fun setLocale(lang: String) {
        val myLocale = Locale(lang)
        Locale.setDefault(myLocale)
        val res: Resources = resources
        val dm: DisplayMetrics = res.displayMetrics
        val conf: Configuration = res.configuration
        conf.locale = myLocale
        conf.setLayoutDirection(myLocale)
        res.updateConfiguration(conf, dm)
    }
}