package com.mando.foxyweatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.gauravk.bubblenavigation.BubbleNavigationLinearView
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener
import com.mando.foxyweatherapp.favouritesScreen.view.FavouritesFragment
import com.mando.foxyweatherapp.homeScreen.view.HomeFragment
import com.mando.foxyweatherapp.notificationsScreen.view.NotificationsFragment
import com.mando.foxyweatherapp.settingsScreen.SettingsFragment

class MainActivity : AppCompatActivity() {
    var bubbleNavigationLinearView: BubbleNavigationLinearView? = null
    var fragmentTransaction: FragmentTransaction? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bubbleNavigationLinearView = findViewById(R.id.bubbleNavigationBar)
        replaceFragments(HomeFragment())

        bubbleNavigationLinearView?.setNavigationChangeListener(BubbleNavigationChangeListener { view: View?, position: Int ->
            when (position) {
                0 -> replaceFragments(HomeFragment())
                1 -> replaceFragments(FavouritesFragment())
                2 -> replaceFragments(NotificationsFragment())
                3 -> replaceFragments(SettingsFragment())
            }
        })
    }

    private fun replaceFragments(fragment: Fragment) {
        fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction!!.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction!!.commit()
    }
}