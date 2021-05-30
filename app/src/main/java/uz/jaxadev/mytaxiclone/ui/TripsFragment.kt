package uz.jaxadev.mytaxiclone.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import timber.log.Timber
import uz.jaxadev.mytaxiclone.R
import uz.jaxadev.mytaxiclone.adapter.TripsRecyclerViewAdapter
import uz.jaxadev.mytaxiclone.database.TripDatabase
import uz.jaxadev.mytaxiclone.databinding.FragmentTripsBinding
import uz.jaxadev.mytaxiclone.model.TripModel
import uz.jaxadev.mytaxiclone.viewmodel.TripViewModel
import uz.jaxadev.mytaxiclone.viewmodel.TripViewModelFactory

class TripsFragment : Fragment() {

    private lateinit var binding: FragmentTripsBinding
    private lateinit var tripsRecyclerViewAdapter: TripsRecyclerViewAdapter
    private lateinit var trips: List<TripModel>
    private lateinit var tripViewModel: TripViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = TripDatabase.getInstance(requireActivity())

        val tripDao = database.tripDao()

        val viewModelFactory = TripViewModelFactory(tripDao)

        tripViewModel = ViewModelProvider(this, viewModelFactory).get(TripViewModel::class.java)

        trips = ArrayList()


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_trips, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tripsRecyclerViewAdapter = TripsRecyclerViewAdapter(
            itemTripCollBack = TripsRecyclerViewAdapter.ItemTripCallBack { trip ->

                val directions = TripsFragmentDirections.actionTripsFragmentToMapsFragment(

                    id = trip.id

                )

                Timber.d("$trip")

                findNavController().navigate(directions)

            })



        tripViewModel.allTrip.observe(requireActivity(), Observer {
            tripsRecyclerViewAdapter.submitList(it)
            Timber.d("idlar nima bovotti $it")
        })




        binding.recyclerView.apply {

            layoutManager = LinearLayoutManager(requireActivity())
            adapter = tripsRecyclerViewAdapter

        }


    }


}