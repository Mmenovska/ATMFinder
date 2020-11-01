package com.gsixacademy.android.atmfinder.utils

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback

class CurrentLocationActivity : AppCompatActivity(){

    private lateinit var map : GoogleMap


    fun getLocationPermission() : Boolean {
        val permission = ArrayList<String>()
        if (!isPermissionGranted (android.Manifest.permission.ACCESS_FINE_LOCATION)){
            permission.add(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (permission.isNotEmpty()){
            ActivityCompat.requestPermissions(this,permission.toTypedArray(),2000)
            return false
        }else
            return true
    }

    private fun isPermissionGranted (permission : String) : Boolean{
        return this.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

     fun onRequestPermissionResult (requestCode : Int, permissions : Array<out String>, grantResults : IntArray){
        if (requestCode == 2000){
            if (grantResults.isNotEmpty()&& grantResults[0] == PackageManager.PERMISSION_GRANTED){
                uptadeLocationUI()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun uptadeLocationUI(){
        if (isPermissionGranted(android.Manifest.permission.ACCESS_FINE_LOCATION)){
            map.isMyLocationEnabled = true
            map.uiSettings.isMyLocationButtonEnabled = true
        }else
            map.isMyLocationEnabled = false
        map.uiSettings.isMyLocationButtonEnabled = false
        getLocationPermission()
    }





}