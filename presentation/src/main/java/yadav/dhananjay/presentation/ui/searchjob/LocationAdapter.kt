package yadav.dhananjay.presentation.ui.searchjob

import android.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import yadav.dhananjay.domain.model.Places


class LocationAdapter constructor(val mContext: Context, val locations: List<Places>, val locationCallback: LocationCallback?)
    : ArrayAdapter<Places>(mContext, yadav.dhananjay.presentation.R.layout.support_simple_spinner_dropdown_item, locations) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listItem = convertView
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.simple_dropdown_item_1line, parent, false)

        val currentMovie = locations.get(position)

        val name = listItem?.findViewById(android.R.id.text1) as TextView
        name.text = currentMovie.name

        listItem.setOnClickListener {
            locationCallback?.locationSelected(currentMovie)
        }

        return listItem
    }

    public interface LocationCallback {
        fun locationSelected(place: Places)
    }
}
