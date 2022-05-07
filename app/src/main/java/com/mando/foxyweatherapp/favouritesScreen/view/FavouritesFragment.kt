package com.mando.foxyweatherapp.favouritesScreen.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mando.foxyweatherapp.R
import com.mando.foxyweatherapp.homeScreen.view.Past24HoursForecastRecyclerAdapter
import com.mando.foxyweatherapp.homeScreen.view.Past7DaysForecastRecyclerAdapter
import com.mando.foxyweatherapp.model.favouritesModel.FavouriteLocation
import com.mando.foxyweatherapp.model.responseModels.DailyWeather
import com.mando.foxyweatherapp.model.responseModels.HourlyWeather
import com.mando.foxyweatherapp.model.responseModels.Temp
import com.mando.foxyweatherapp.model.responseModels.Weather


class FavouritesFragment : Fragment() , onFavouriteDeleteListener,onFavouriteClickListener {

    private lateinit var favouritesRecyclerAdapter: FavouritesRecyclerAdapter
    private lateinit var favouritesLayoutManger : RecyclerView.LayoutManager
    private lateinit var favouritesRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler(view)

    }

    override fun openFavourite(location: FavouriteLocation) {

        Log.e("mando", "openFavourite: ${location.locationName}")
    }

    override fun deleteFavourite(location: FavouriteLocation) {

        Log.e("mando", "openFavourite: ${location.locationName}")
    }

    private fun initRecycler(view: View){

        var favouriteLocation : List<FavouriteLocation> = listOf(FavouriteLocation(22.0,33.0,"october"),FavouriteLocation(22.0,33.0,"zzzz"),FavouriteLocation(22.0,33.0,"iiii"),FavouriteLocation(22.0,33.0,"ppppp"))

        favouritesRecyclerView = view.findViewById(R.id.favourites_recycler)
        favouritesRecyclerAdapter = FavouritesRecyclerAdapter(this,this)
        favouritesLayoutManger = LinearLayoutManager(activity,RecyclerView.VERTICAL,false)
        favouritesRecyclerView.layoutManager = favouritesLayoutManger
        favouritesRecyclerView.adapter = favouritesRecyclerAdapter
        favouritesRecyclerAdapter.favouriteLocation = favouriteLocation
        favouritesRecyclerAdapter.notifyDataSetChanged()

    }

}