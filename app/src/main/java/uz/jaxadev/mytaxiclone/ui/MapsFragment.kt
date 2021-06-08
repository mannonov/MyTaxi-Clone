package uz.jaxadev.mytaxiclone.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_maps.*
import timber.log.Timber
import uz.jaxadev.mytaxiclone.R
import uz.jaxadev.mytaxiclone.database.TripDao
import uz.jaxadev.mytaxiclone.database.TripDatabase
import uz.jaxadev.mytaxiclone.databinding.FragmentMapsBinding
import java.util.*
import kotlin.collections.ArrayList


class MapsFragment : Fragment() {

    private lateinit var binding: FragmentMapsBinding
    private lateinit var map: GoogleMap
    private val REQUEST_LOCATION_PERMISSION = 1

    private lateinit var startPoint: LatLng
    private lateinit var endPoint: LatLng

    private val args: MapsFragmentArgs by navArgs()

    private lateinit var tripDao: TripDao


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = TripDatabase.getInstance(requireActivity())
        tripDao = database.tripDao()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_maps, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }


    private val callback = OnMapReadyCallback { googleMap ->

        map = googleMap

        val startLatitude = 41.32551940
        val startLongitude = 69.2453650
        val endLatitude = 41.342248
        val endLongitude = 69.241522

        startPoint = LatLng(startLatitude, startLongitude)
        endPoint = LatLng(endLatitude, endLongitude)


        val builder: LatLngBounds.Builder = LatLngBounds.Builder()
        builder.include(startPoint)
        builder.include(endPoint)
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100))

        googleMap.addMarker(
            MarkerOptions().position(startPoint)
                .icon(
                    BitmapDescriptorFactory.fromBitmap(
                        smallMarker(
                            R.drawable.pin_main,
                            240,
                            170
                        )
                    )
                )
        )
        map = googleMap
        map.mapType = GoogleMap.MAP_TYPE_TERRAIN
        map.uiSettings.isMyLocationButtonEnabled = false


        googleMap.addMarker(
            MarkerOptions().position(endPoint)
                .icon(
                    BitmapDescriptorFactory.fromBitmap(
                        smallMarker(
                            R.drawable.pin_mini,
                            100,
                            100
                        )
                    )
                )
        )


        setPolyline(map)

        setMapLongClick(map)
        setPoiClick(map)
        setMapStyle(map)
        enableMyLocation()
    }

    private fun setPolyline(googleMap: GoogleMap) {
        val coordinates: MutableList<LatLng> = ArrayList()
        coordinates.add(LatLng(41.32551940, 69.2453650))
        coordinates.add(LatLng(41.328251, 69.247924))
        coordinates.add(LatLng(41.324484, 69.254904))
        coordinates.add(LatLng(41.325305, 69.254910))
        coordinates.add(LatLng(41.329648, 69.246923))
        coordinates.add(LatLng(41.330834, 69.242856))
        coordinates.add(LatLng(41.342248, 69.241522))

        val polyline = PolylineOptions()
        polyline.addAll(coordinates)
        polyline.width(10f)
        polyline.color(ContextCompat.getColor(requireActivity(), R.color.polyline))
        polyline.jointType(JointType.ROUND)
        polyline.startCap(ButtCap())
        googleMap.addPolyline(polyline)

    }


    @SuppressLint("RestrictedApi", "ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)


        binding.btnBack.setOnClickListener {
            val mapsFragmentDirections = MapsFragmentDirections.actionMapsFragmentToTripsFragment()
            findNavController().navigate(mapsFragmentDirections)
        }



        tripDao.queryWithID(args.id)
            .observe(requireActivity(), androidx.lifecycle.Observer { trip ->


                binding.apply {
                    startPoint = trip.startPoint
                    endPoint = trip.endPoind
                    carModel = trip.carModel
                    carNumber = trip.carNumber
                    tariff = trip.tariff
                    dictance = trip.distance
                    paymentType = trip.paymentType
                    startTime = trip.startTime
                    endTime = trip.endTime
                    tripTime = trip.tripTime
                    baseFare = trip.baseFare
                    rideFee = trip.rideFee
                    waitingFee = trip.waitingFee
                    driverName = trip.driverName
                    rating = trip.driverRating
                    trips = trip.driverTrips
                }
            })
        binding.containerRoot.setBackgroundColor(R.color.black)


        val transition = ChangeBounds()
        transition.duration = 200L
        TransitionManager.beginDelayedTransition(binding.bottomSheet, transition)
        BottomSheetBehavior.from(bottom_sheet).setPeekHeight(300,true)

//        binding.bottomSheet.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
//            bottomSheetBehavior.setPeekHeight(bottomSheetPeekLayout.height, true)
//        }
//        BottomSheetBehavior.from(bottom_sheet).apply {
//            peekHeight = 300
//            if (peekHeight <= 300){
//
//                binding.containerRoot.setBackgroundColor(R.color.black)
//
//            }
//        }

    }


    private fun setMapLongClick(map: GoogleMap) {
        map.setOnMapLongClickListener { latLng ->
            val snippet = String.format(
                Locale.getDefault(),
                "Lat: %1$.5f, Long: %2$.5f",
                latLng.latitude,
                latLng.longitude
            )
            map.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(getString(R.string.dropped_pin))
                    .snippet(snippet)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
            )
        }
    }

    private fun setPoiClick(map: GoogleMap) {
        map.setOnPoiClickListener { poi ->
            val poiMarker = map.addMarker(
                MarkerOptions()
                    .position(poi.latLng)
                    .title(poi.name)
            )
            poiMarker.showInfoWindow()
        }
    }

    private fun setMapStyle(map: GoogleMap) {
        try {
            val success = map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    requireActivity(),
                    R.raw.map_style
                )
            )

            if (!success) {
                Timber.d("Style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Timber.d("Can't find style. Error: ", e)
        }
    }

    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun enableMyLocation() {
        if (isPermissionGranted()) {
            if (ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            map.isMyLocationEnabled = true
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                enableMyLocation()
            }
        }
    }

    fun smallMarker(icon: Int, height: Int, width: Int): Bitmap {
        val bitmapdraw = resources.getDrawable(icon) as BitmapDrawable
        val b = bitmapdraw.bitmap
        return Bitmap.createScaledBitmap(b, width, height, false)

    }


}