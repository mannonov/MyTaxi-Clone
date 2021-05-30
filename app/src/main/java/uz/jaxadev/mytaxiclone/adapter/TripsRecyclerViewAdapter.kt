package uz.jaxadev.mytaxiclone.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.jaxadev.mytaxiclone.databinding.TripItemBinding
import uz.jaxadev.mytaxiclone.model.TripModel


class TripsRecyclerViewAdapter(val itemTripCollBack: ItemTripCallBack) :
    ListAdapter<TripModel, TripsRecyclerViewAdapter.ViewHolder>(TripsComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = TripItemBinding.inflate(inflater, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = getItem(position)
        holder.bind(item)

    }


    inner class ViewHolder(val binding: TripItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TripModel) {
            binding.tripItem = item
            binding.itemTripCallBack = itemTripCollBack
            binding.executePendingBindings()
        }
    }

    class TripsComparator : DiffUtil.ItemCallback<TripModel>() {


        private val TAG = "TripsRecyclerViewAdapter"

        override fun areContentsTheSame(oldItem: TripModel, newItem: TripModel): Boolean {
            Log.d(TAG, "oldItem == newItem $oldItem $newItem ")
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: TripModel, newItem: TripModel): Boolean {
            Log.d(TAG, "oldItem.tripId == newItem.tripId $oldItem $newItem ")
            return oldItem.id == newItem.id && oldItem.tripId == newItem.tripId
        }
    }

    class ItemTripCallBack(val callBack: (tripModel: TripModel) -> Unit) {
        fun onTripClick(tripModel: TripModel) = callBack(tripModel)
    }
}
