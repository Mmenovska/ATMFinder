package com.gsixacademy.android.atmfinder

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.Polyline
import com.google.firebase.database.*
import com.google.maps.android.PolyUtil
import com.gsixacademy.android.atmfinder.adapter.AtmListAdapter
import com.gsixacademy.android.atmfinder.data.AtmsData
import com.gsixacademy.android.atmfinder.data.AtmsModel
import com.gsixacademy.android.atmfinder.utils.CurrentLocationActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import java.util.jar.Manifest
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var database : DatabaseReference
    var atmsListModel : AtmsModel? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        database = FirebaseDatabase.getInstance().reference

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getAdress()
        initialiseFirebaseDataBase()




        recycler_view_main.setOnClickListener{
            val intent = Intent(this,MapFragment::class.java)
            startActivity(intent)


        }

    }

    fun onRequestPermisiion (requestCode : Int, permission : Array <out kotlin.String>, grantResult : IntArray){
        if (requestCode == 2000){
            if (grantResult.isNotEmpty()&& grantResult[0] == PackageManager.PERMISSION_GRANTED){
                getAdress()
            }
        }
    }

    fun getAdress(){
        if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            val permission = ArrayList<kotlin.String>()
            permission.add(android.Manifest.permission.ACCESS_FINE_LOCATION)
            permission.add(android.Manifest.permission.ACCESS_COARSE_LOCATION)
            if (permission.isNotEmpty())
                ActivityCompat.requestPermissions(this,permission.toTypedArray(),2000)
        } else {
            fusedLocationClient.getLastLocation().addOnSuccessListener (this, { location ->
                if (location != null){
                    val wayLatitude = location.getLatitude()
                    val wayLongitude = location.getLongitude()
                    val geocoder = Geocoder (this, Locale.getDefault())
                    val addressess : List<Address> = geocoder.getFromLocation(wayLatitude,wayLongitude,1)
                    val address : kotlin.String = addressess[0].getAddressLine(0).split(",")[0]
                    edit_text_location.setText(address  )

                }
            })
        }
    }

    fun initialiseFirebaseDataBase (){
        val postListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                atmsListModel = snapshot.getValue(AtmsModel ::class.java)
            }

        }
        database.addValueEventListener(postListener)
    }







    }


