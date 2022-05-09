package com.mando.foxyweatherapp.map.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.moviesappmvvm.model.Repository
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.mando.foxyweatherapp.MainActivity
import com.mando.foxyweatherapp.R
import com.mando.foxyweatherapp.database.ConcreteLocalSource
import com.mando.foxyweatherapp.map.MapViewModel
import com.mando.foxyweatherapp.map.MapViewModelFactory
import com.mando.foxyweatherapp.network.RemoteSource
import com.mando.foxyweatherapp.utitlity.getCityText
import com.mando.foxyweatherapp.utitlity.getSharedPreferences

class MapActivity : AppCompatActivity() {
    private var lat = 30.044
    private var lon = 31.235
    private var isFavorite: Boolean = true
    private val viewModel: MapViewModel by viewModels {
        MapViewModelFactory(
            Repository.getInstance(
                RemoteSource.getInstance(), ConcreteLocalSource(this), this
            ))
    }
    private lateinit var map: SupportMapFragment
    private lateinit var btnDone: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        //handleBackButton()

        map =
            (supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?)!!
        btnDone = findViewById<Button>(R.id.btn_Done)
        isFavorite = intent.getBooleanExtra("isFavorite",false)


        map?.getMapAsync(callback)

        //handleBackButton()
        btnDone.setOnClickListener {
            if (isFavorite) {
                navigateToFavoriteScreen(lat, lon)
            } else {
                saveLocationInSharedPreferences(lon, lat)
            }
        }
    }

    private val callback = OnMapReadyCallback { googleMap ->
        val cairo = LatLng(lat, lon)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cairo, 10.0f))
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.setOnMapClickListener { location ->
            googleMap.clear()
            googleMap.addMarker(MarkerOptions().position(location))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12.0f))
            lat = location.latitude
            lon = location.longitude
            Log.e("mando","get location :lat $lat, lon $lon")

            btnDone.visibility = View.VISIBLE
        }
    }



    private fun navigateToFavoriteScreen(lat: Double, lon: Double) {
        val language = getSharedPreferences(this).getString(getString(R.string.languageSetting), "en")
        val name = getCityText(this,lat,lon,language!!)
        try {
            viewModel.setFavorite(lat, lon, name )
            finish()
        } catch (e: Exception) {
            Toast.makeText(this,"${e.message}",Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveLocationInSharedPreferences(long: Double, lat: Double) {
        Log.e("mando","get location :lat $lat, lon $long")

        val editor = getSharedPreferences(this).edit()
        editor.putFloat(getString(R.string.lat), lat.toFloat())
        editor.putFloat(getString(R.string.lon), long.toFloat())
        editor.putBoolean("firstTime", false)
        editor.putBoolean(getString(R.string.isMap), true)
        editor.apply()
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }


//    private fun handleBackButton() {
////        binding.root.isFocusableInTouchMode = true
////        binding.root.requestFocus()
////        binding.root.setOnKeyListener(View.OnKeyListener { view, keyCode, event ->
////            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
////                if (isFavorite) {
////                    startActivity(Intent(this, FavouriteActivity::class.java))
////
////                } else {
////                    startActivity(Intent(this,HomeActivity::class.java))
////                }
////                return@OnKeyListener true
////            }
////            false
////        })
//    }
}