package com.gsixacademy.android.atmfinder

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.gsixacademy.android.atmfinder.data.AtmsModel
import com.gsixacademy.android.atmfinder.utils.CurrentLocationActivity

class MapFragment : Fragment(), OnMapReadyCallback {

    private var atmListModel : AtmsModel? = null
    private lateinit var map : GoogleMap


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate (R.layout.activity_map,container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.fragment_map) as SupportMapFragment
        atmListModel = (activity as MainActivity).atmsListModel
        mapFragment.getMapAsync(this)
    }



    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(activity,R.raw.my_map_style))

        setMarkers()

    }

    fun setMarkers(){
        if (atmListModel?.atms != null){
            val builder = LatLngBounds.builder()
            for (atm in atmListModel!!.atms!!){
                val markerOptions = MarkerOptions().position(LatLng(atm.lat_long?.latitude!!, atm.lat_long?.longitude!!))
                    .title (atm.bank)
                    .snippet("see more...")
                builder.include(markerOptions.position)
                map.addMarker(markerOptions).tag = atm.id
            }

            val bounds = builder.build()
            val padding = 90
            val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds,padding)
            map.moveCamera(cameraUpdate)
        }
    }


}