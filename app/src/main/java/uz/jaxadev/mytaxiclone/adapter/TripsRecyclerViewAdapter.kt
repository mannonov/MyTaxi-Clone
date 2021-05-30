package uz.jaxadev.mytaxiclone.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber
import uz.jaxadev.mytaxiclone.databinding.TripItemBinding
import uz.jaxadev.mytaxiclone.model.TripModel


class TripsRecyclerViewAdapter(var trips: List<TripModel>, val itemTripCollBack: ItemTripCallBack) :
    ListAdapter<TripModel, TripsRecyclerViewAdapter.ViewHolder>(TripsComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = TripItemBinding.inflate(inflater, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(trips[position])

        Timber.d("Adapertda $trips")

    }

    override fun getItemCount(): Int = trips.size

    inner class ViewHolder(val binding: TripItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TripModel) {
            binding.tripItem = item
            binding.itemTripCallBack = itemTripCollBack
        }
    }

    class ItemTripCallBack(val callBack: (tripModel: TripModel) -> Unit) {
        fun onTripClick(tripModel: TripModel) = callBack(tripModel)
    }

    class TripsComparator : DiffUtil.ItemCallback<TripModel>() {
        override fun areItemsTheSame(oldItem: TripModel, newItem: TripModel): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: TripModel, newItem: TripModel): Boolean {
            return oldItem.id == newItem.id
        }
    }


}