package com.mando.foxyweatherapp.favouritesScreen.view.FavouritesScreen

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.WorkManager
import com.example.moviesappmvvm.model.Repository
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mando.foxyweatherapp.R
import com.mando.foxyweatherapp.database.ConcreteLocalSource
import com.mando.foxyweatherapp.favouritesScreen.view.DisplayScreen.DisplayFavouriteActivity
import com.mando.foxyweatherapp.favouritesScreen.viewModel.FavouritesScreen.FavouritesFragmentViewModel
import com.mando.foxyweatherapp.favouritesScreen.viewModel.FavouritesScreen.FavouritesFragmentViewModelFactory
import com.mando.foxyweatherapp.map.view.MapActivity
import com.mando.foxyweatherapp.model.favouritesModel.FavouriteLocation
import com.mando.foxyweatherapp.network.RemoteSource
import com.mando.foxyweatherapp.utitlity.broadCast.NetworkChangeReceiver


class FavouritesFragment : Fragment() , onFavouriteDeleteListener, onFavouriteClickListener ,NetworkChangeReceiver.ConnectivityReceiverListener{

    private lateinit var favouritesRecyclerAdapter: FavouritesRecyclerAdapter
    private lateinit var favouritesLayoutManger : RecyclerView.LayoutManager
    private lateinit var favouritesRecyclerView: RecyclerView
    private lateinit var favViewModel: FavouritesFragmentViewModel
    private lateinit var favFactory : FavouritesFragmentViewModelFactory
    private lateinit var addFav: FloatingActionButton
    private var flagNoConnection: Boolean = false



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

        initView(view)
        initNetworkReceiver()
        initViewModel()
        observeFavourites()

    }

    private fun initNetworkReceiver() {
        NetworkChangeReceiver.connectivityReceiverListener = this
        activity?.registerReceiver(NetworkChangeReceiver(),
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    private fun observeFavourites(){
        favViewModel.getFavouritesFromDatabase().observe(this){
            if (it !=null)
                favouritesRecyclerAdapter.favouriteLocation = it
            favouritesRecyclerAdapter.notifyDataSetChanged()
        }
    }
    private fun initViewModel(){
        favFactory = FavouritesFragmentViewModelFactory( Repository.getInstance(
            RemoteSource.getInstance(), ConcreteLocalSource(requireContext()), requireContext()
        ))
        favViewModel = ViewModelProvider(this,favFactory)[FavouritesFragmentViewModel::class.java]

    }
    private fun setListeners(){
        addFav.setOnClickListener {
                val intent = Intent(requireContext(), MapActivity::class.java)
                intent.putExtra("isFavorite",true)
                startActivity(intent)
            }
    }


    override fun openFavourite(location: FavouriteLocation) {

        if (!flagNoConnection){
            val intent = Intent(requireContext(), DisplayFavouriteActivity::class.java)
            intent.putExtra("lat",location.lat)
            intent.putExtra("lon",location.lon)
            intent.putExtra("title",location.locationName)
            startActivity(intent)
        }
        else{
            Toast.makeText(requireContext(), getString(R.string.noInternetConnection), Toast.LENGTH_SHORT).show()
        }

    }

    override fun deleteFavourite(location: FavouriteLocation) {

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.deleteConfirmation))
            .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                favViewModel.deleteFavourite(location)
                dialog.dismiss()
                Toast.makeText(requireContext(),getString(R.string.deleteSuccess),Toast.LENGTH_SHORT).show()
            }
            .show()
    }

    private fun initView(view: View){
        favouritesRecyclerView = view.findViewById(R.id.favourites_recycler)
        addFav = view.findViewById(R.id.add_fav)
        favouritesRecyclerAdapter = FavouritesRecyclerAdapter(this,this)
        favouritesLayoutManger = LinearLayoutManager(activity,RecyclerView.VERTICAL,false)
        favouritesRecyclerView.layoutManager = favouritesLayoutManger
        favouritesRecyclerView.adapter = favouritesRecyclerAdapter
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (isConnected) {
            setListeners()
            if (flagNoConnection) {
                addFav.visibility = View.VISIBLE
                flagNoConnection = false
            }
        } else {
            addFav.visibility = View.GONE
            Toast.makeText(requireContext(),getString(R.string.noInternetConnection),Toast.LENGTH_SHORT).show()
            flagNoConnection = true
        }
    }


}