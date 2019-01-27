package yadav.dhananjay.presentation.ui.searchjob

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_location.view.*
import yadav.dhananjay.domain.model.Places
import yadav.dhananjay.presentation.R

class LocationSearchAdapter(private val adpaterCallback: AdapterCallback?) : ListAdapter<Places, RecyclerView.ViewHolder>(LocationDiffer()) {

    interface AdapterCallback {
        fun onLocationClick(place: Places)

        fun onOtherClick()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_location, parent, false)
        return LocationViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as LocationViewHolder).run {
            holder?.tvCityName?.text = getItem(position).name

            this.itemView.rootView.setOnClickListener {
                if (getItem(position).isOtherButton) {
                    adpaterCallback?.onOtherClick()
                } else {
                    adpaterCallback?.onLocationClick(getItem(position))
                }

            }
        }
    }

    inner class LocationViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {
        val tvCityName = view.tvCityName

    }
}

class LocationDiffer : DiffUtil.ItemCallback<Places?>() {

    override fun areItemsTheSame(oldItem: Places, newItem: Places): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Places, newItem: Places): Boolean {
        return oldItem == newItem
    }
}