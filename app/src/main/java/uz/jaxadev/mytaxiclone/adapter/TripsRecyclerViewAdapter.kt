package uz.jaxadev.mytaxiclone.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.jaxadev.mytaxiclone.databinding.TripItemBinding
import uz.jaxadev.mytaxiclone.model.TripModel

class TripsRecyclerViewAdapter(private val trips: ArrayList<TripModel>) :
    RecyclerView.Adapter<TripsRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = TripItemBinding.inflate(inflater, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(trips[position])

    }

    override fun getItemCount(): Int = trips.size

    class ViewHolder(private val binding: TripItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TripModel) {
            binding.tripItem = item
        }
    }


}