package uz.jaxadev.mytaxiclone.ui

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Resources
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_maps.*
import uz.jaxadev.mytaxiclone.R
import java.util.*

class MapsFragment : Fragment() {

    private lateinit var map: GoogleMap
    private val TAG = MapsFragment::class.java.simpleName
    private val REQUEST_LOCATION_PERMISSION = 1

    private lateinit var homeLatLng: LatLng

    private val callback = OnMapReadyCallback { googleMap ->
        val latitude = 41.32551940
        val longitude = 69.2453650
        val zoomLevel = 30f
        homeLatLng = LatLng(latitude, longitude)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(homeLatLng, zoomLevel))
        googleMap.addMarker(MarkerOptions().position(homeLatLng))
        map = googleMap

        map.mapType = GoogleMap.MAP_TYPE_TERRAIN

        val overlaySize = 100f

        val googleOverlay = GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.najottalim))
                .position(homeLatLng, overlaySize)
        map.addGroundOverlay(googleOverlay)

        setMapLongClick(map)
        setPoiClick(map)
        setMapStyle(map)
        enableMyLocation()
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

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
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", e)
        }
    }

    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
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
            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                enableMyLocation()
            }
        }
    }
}