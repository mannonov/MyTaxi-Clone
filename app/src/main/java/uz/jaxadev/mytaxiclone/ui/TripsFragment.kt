package uz.jaxadev.mytaxiclone.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import timber.log.Timber
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
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_trips, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        trips.add(
            TripModel(
                destination = "Olmozor tumani Qorasaroy ko'cha",
                stopAddress = "Shayxontohur tumani Sebzor ko'chasi",
                date = "2021-05-28 08:28",
                paid = "10,800 som",
                carModel = "White Nexia",
                carNumber = "01 Z 884 YA",
                driverPhoneNumber = "+998909666335",
                tariff = "Economy",
                paymentType = "Cash",
                order = "5345362",
                startTime = "08:32",
                endTime = "08:38",
                tripTime = "00:05",
                baseFare = "5,000 som",
                rideFee = "1,752 som",
                waitingFee = "0 som",
                surge = "4,000 som",
                total = "10,800 som",
                driverName = "Timur",
                driverRating = "4.9",
                driverTrips = "1133"
            )
        )
        trips.add(
            TripModel(
                destination = "Olmozor Nurafshon aylanma ko'chasi 48",
                stopAddress = "Shayxontohur, Xadra",
                date = "2021-02-24 15:04",
                paid = "7,900 som",
                carModel = "White Lacetti",
                carNumber = "01 A 939 NA",
                driverPhoneNumber = "+998938290012",
                tariff = "Economy",
                paymentType = "Payme",
                order = "4471444",
                startTime = "15:14",
                endTime = "15:19",
                tripTime = "00:04",
                baseFare = "5,000 som",
                rideFee = "2,008 som",
                waitingFee = "873 som",
                surge = "2,500 som",
                total = "7,900 som",
                driverName = "Maxamadjon",
                driverRating = "4.8",
                driverTrips = "858"
            )
        )



        tripsRecyclerViewAdapter = TripsRecyclerViewAdapter(
            trips,
            itemTripCollBack = TripsRecyclerViewAdapter.ItemTripCallBack { trip ->

                val directions = TripsFragmentDirections.actionTripsFragmentToMapsFragment(
                    destination = trip.destination,
                    stopAdress = trip.stopAddress,
                    date = trip.date,
                    paid = trip.paid,
                    carModel = trip.carModel,
                    carNumber = trip.carNumber,
                    driverPhoneNumber = trip.driverPhoneNumber,
                    tariff = trip.tariff,
                    paymentType = trip.paymentType,
                    startTime = trip.startTime,
                    order = trip.order,
                    endTime = trip.endTime,
                    tripTime = trip.tripTime,
                    baseFare = trip.baseFare,
                    rideFee = trip.rideFee,
                    waitingFee = trip.waitingFee,
                    surge = trip.surge,
                    total = trip.total,
                    driverName = trip.driverName,
                    driverRating = trip.driverRating,
                    driverTrips = trip.driverTrips


                )

                Timber.d("$trip")

                findNavController().navigate(directions)

            })


        binding.recyclerView.apply {

            layoutManager = LinearLayoutManager(requireActivity())
            adapter = tripsRecyclerViewAdapter

        }

    }


}