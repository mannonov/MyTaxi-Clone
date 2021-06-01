package uz.jaxadev.mytaxiclone.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_maps.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import uz.jaxadev.mytaxiclone.R
import uz.jaxadev.mytaxiclone.database.TripDao
import uz.jaxadev.mytaxiclone.database.TripDatabase
import uz.jaxadev.mytaxiclone.databinding.FragmentMapsBinding
import uz.jaxadev.mytaxiclone.viewmodel.TripViewModel
import uz.jaxadev.mytaxiclone.viewmodel.TripViewModelFactory
import uz.jaxadev.util.Route
import java.util.*
import kotlin.collections.ArrayList

class MapsFragment : Fragment() {

    private lateinit var binding: FragmentMapsBinding
    private lateinit var map: GoogleMap
    private val REQUEST_LOCATION_PERMISSION = 1

    private lateinit var startPoint: LatLng
    private lateinit var endPoint: LatLng

    private lateinit var polylineList: ArrayList<LatLng>

    private lateinit var tripViewModel: TripViewModel

    private val args: MapsFragmentArgs by navArgs()

    private lateinit var tripDao: TripDao

    private lateinit var polylineOptions: PolylineOptions


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = TripDatabase.getInstance(requireActivity())
        tripDao = database.tripDao()

        val viewModelFactory = TripViewModelFactory(tripDao)
        tripViewModel = ViewModelProvider(this, viewModelFactory).get(TripViewModel::class.java)


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
        val startLatitude = 41.32551940
        val startLongitude = 69.2453650
        val endLatitude = 41.326541
        val endLongitude = 69.246328
        val zoomLevel = 17f
        startPoint = LatLng(startLatitude, startLongitude)
        endPoint = LatLng(endLatitude, endLongitude)

        val job = Job()
        val viewModelScope = CoroutineScope(Dispatchers.Main + job)

        polylineList = ArrayList()

        viewModelScope.launch {
            tripViewModel.getDirection(
                "$startLatitude,$startLongitude",
                "$endLatitude,$endLongitude"
            ).observe(requireActivity(), androidx.lifecycle.Observer {
                Timber.d("Observe")

                    val listRoute: List<Route> = it.routes
                    for (route in listRoute) {
                        val polyline: String = route.overViewPolyline.points
                        polylineList.addAll(decodePoly(polyline)!!)
                        Timber.d("$polylineList")
                    }


                    polylineOptions = PolylineOptions()
                    polylineOptions.color(ContextCompat.getColor(requireActivity(), R.color.black))
                    polylineOptions.width(8f)
                    polylineOptions.startCap(ButtCap())
                    polylineOptions.jointType(JointType.ROUND)
                    polylineOptions.addAll(polylineList)
                    map.addPolyline(polylineOptions)

                    val builder : LatLngBounds.Builder = LatLngBounds.Builder()
                    builder.include(startPoint)
                    builder.include(endPoint)
                    map.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(),100))

                })
        }


        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startPoint, zoomLevel))
        googleMap.addMarker(
            MarkerOptions().position(startPoint)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin_eta_main))
        )
        map = googleMap
        map.mapType = GoogleMap.MAP_TYPE_TERRAIN

        val overlaySize = 20f

        val googleOverlay = GroundOverlayOptions()
            .image(BitmapDescriptorFactory.fromResource(R.drawable.ic_point_end))
            .position(LatLng(41.326541, 69.246328), overlaySize)
        map.addGroundOverlay(googleOverlay)

        setMapLongClick(map)
        setPoiClick(map)
        setMapStyle(map)
        enableMyLocation()
    }


    @SuppressLint("RestrictedApi")
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

        BottomSheetBehavior.from(bottom_sheet).apply {
            peekHeight = 300
        }


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
                Timber.d( "Style parsing failed.")
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

    private fun decodePoly(encoded: String): List<LatLng>? {
        val poly: MutableList<LatLng> = java.util.ArrayList()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0
        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            run {
                val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
                lat += dlat
                shift = 0
                result = 0
                do {
                    b = encoded[index++].toInt() - 63
                    result = result or (b and 0x1f shl shift)
                    shift += 5
                } while (b >= 0x20)
                {
                    val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
                    lng += dlng
                    val p = LatLng(lat.toDouble() / 1E5, lng.toDouble() / 1E5)
                    poly.add(p)
                }
            }
        }
        return poly
    }

}