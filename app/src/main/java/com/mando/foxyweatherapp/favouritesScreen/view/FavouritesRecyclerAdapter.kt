package com.mando.foxyweatherapp.favouritesScreen.view

import android.annotation.SuppressLint
import android.graphics.Insets.add
import android.view.*
import android.view.View.OnCreateContextMenuListener
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.mando.foxyweatherapp.R
import com.mando.foxyweatherapp.model.favouritesModel.FavouriteLocation
import com.mando.foxyweatherapp.model.responseModels.DailyWeather
import com.mando.foxyweatherapp.utitlity.getDayOfWeek
import com.mando.foxyweatherapp.utitlity.getIcon

class FavouritesRecyclerAdapter(private val favouriteClickListener: onFavouriteClickListener, private val favouriteDeleteListener: onFavouriteDeleteListener) :RecyclerView.Adapter<FavouritesRecyclerAdapter.ViewHolder>(){

    var favouriteLocation: List<FavouriteLocation> = arrayListOf()

    inner class ViewHolder(private val itemView : View): RecyclerView.ViewHolder(itemView) ,
        OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        val location : TextView = itemView.findViewById(R.id.location_name_tv)
        val constraintLayout: ConstraintLayout = itemView.findViewById(R.id.favourite_constraint)

        override fun onCreateContextMenu(
            menu: ContextMenu?,
            view: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            menu?.setHeaderTitle("Choose")
            val delete: MenuItem = menu!!.add(Menu.NONE, 1, 1, "Delete")
            delete.setOnMenuItemClickListener(this)
        }

        override fun onMenuItemClick(item: MenuItem?): Boolean {
            if (favouriteDeleteListener != null) {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    when (item?.itemId) {
                        1 -> {
                            favouriteDeleteListener.deleteFavourite(favouriteLocation[pos])
                            return true
                        }
                    }
                }
            }
            return false
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_item_favourites,parent,false)
        return ViewHolder(view)
    }



    override fun getItemCount(): Int = favouriteLocation.size

    override fun onBindViewHolder(viewHolder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {

        viewHolder.location.text = "${favouriteLocation[position].locationName}"

        viewHolder.constraintLayout.setOnClickListener{favouriteClickListener.openFavourite(favouriteLocation[position])}
        viewHolder.constraintLayout.setOnCreateContextMenuListener(viewHolder)
    }
}

