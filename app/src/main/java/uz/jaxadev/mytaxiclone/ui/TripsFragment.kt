package uz.jaxadev.mytaxiclone.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import uz.jaxadev.mytaxiclone.R
import uz.jaxadev.mytaxiclone.adapter.TripsRecyclerViewAdapter
import uz.jaxadev.mytaxiclone.databinding.FragmentTripsBinding
import uz.jaxadev.mytaxiclone.model.TripModel

class TripsFragment : Fragment() {

    private lateinit var binding: FragmentTripsBinding
    private lateinit var tripsRecyclerViewAdapter: TripsRecyclerViewAdapter
    private lateinit var trips: ArrayList<TripModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        trips = ArrayList()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_trips, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        trips.add(
            TripModel(
                destination = getString(R.string.lorem_ipsum),
                returnAddress = getString(R.string.lorem_ipsum),
                date = getString(R.string.default_time),
                paid = getString(R.string.default_sum)
            )
        )

        trips.add(
            TripModel(
                destination = getString(R.string.lorem_ipsum),
                returnAddress = getString(R.string.lorem_ipsum),
                date = getString(R.string.default_time),
                paid = getString(R.string.default_sum)
            )
        )
        trips.add(
            TripModel(
                destination = getString(R.string.lorem_ipsum),
                returnAddress = getString(R.string.lorem_ipsum),
                date = getString(R.string.default_time),
                paid = getString(R.string.default_sum)
            )
        )
        trips.add(
            TripModel(
                destination = getString(R.string.lorem_ipsum),
                returnAddress = getString(R.string.lorem_ipsum),
                date = getString(R.string.default_time),
                paid = getString(R.string.default_sum)
            )
        )
        trips.add(
            TripModel(
                destination = getString(R.string.lorem_ipsum),
                returnAddress = getString(R.string.lorem_ipsum),
                date = getString(R.string.default_time),
                paid = getString(R.string.default_sum)
            )
        )
        trips.add(
            TripModel(
                destination = getString(R.string.lorem_ipsum),
                returnAddress = getString(R.string.lorem_ipsum),
                date = getString(R.string.default_time),
                paid = getString(R.string.default_sum)
            )
        )
        trips.add(
            TripModel(
                destination = getString(R.string.lorem_ipsum),
                returnAddress = getString(R.string.lorem_ipsum),
                date = getString(R.string.default_time),
                paid = getString(R.string.default_sum)
            )
        )

        tripsRecyclerViewAdapter = TripsRecyclerViewAdapter(trips)


        binding.recyclerView.apply {

            layoutManager = LinearLayoutManager(requireActivity())
            adapter = tripsRecyclerViewAdapter

        }

    }


}