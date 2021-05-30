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


//        tripViewModel.insert(TripModel(
//            destination = "Olmozor tumani Qorasaroy ko'cha",
//            stopAddress = "Shayxontohur tumani Sebzor ko'chasi",
//            date = "2021-05-28 08:28",
//            paid = "10,800 som",
//            carModel = "White Nexia",
//            carNumber = "01 Z 884 YA",
//            driverPhoneNumber = "+998909666335",
//            tariff = "Economy",
//            paymentType = "Cash",
//            order = "5345362",
//            startTime = "08:32",
//            endTime = "08:38",
//            tripTime = "00:05",
//            baseFare = "5,000 som",
//            rideFee = "1,752 som",
//            waitingFee = "0 som",
//            surge = "4,000 som",
//            total = "10,800 som",
//            driverName = "Timur",
//            driverRating = "4.9",
//            driverTrips = "1133"
//        ))

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
            trips,
            itemTripCollBack = TripsRecyclerViewAdapter.ItemTripCallBack { trip ->

                val directions = TripsFragmentDirections.actionTripsFragmentToMapsFragment(

                    id = trip.id

                )

                Timber.d("$trip")

                findNavController().navigate(directions)

            })



        tripViewModel.allTrip.observe(requireActivity(), Observer {
            tripsRecyclerViewAdapter.trips = it
            tripsRecyclerViewAdapter.notifyDataSetChanged()
        })




        binding.recyclerView.apply {

            layoutManager = LinearLayoutManager(requireActivity())
            adapter = tripsRecyclerViewAdapter

        }


    }


}