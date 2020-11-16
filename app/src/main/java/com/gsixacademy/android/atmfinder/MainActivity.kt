package com.gsixacademy.android.atmfinder

import android.app.Activity

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.*
import com.gsixacademy.android.atmfinder.adapter.AtmListAdapter
import com.gsixacademy.android.atmfinder.adapter.CityAdapter
import com.gsixacademy.android.atmfinder.data.AtmsData
import com.gsixacademy.android.atmfinder.data.AtmsModel
import com.gsixacademy.android.atmfinder.data.SpinnerModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_recycler_view.*
import kotlinx.android.synthetic.main.activity_recycler_view.recycler_view_main
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var database : DatabaseReference
    var atmsListModel : AtmsModel? = null
    var atmSortedList : ArrayList<AtmsData> = ArrayList()
    lateinit var cityList : ArrayList<SpinnerModel>
    lateinit var cityAdapter : CityAdapter
    var isLoading : Boolean = false
    var isLastPage : Boolean = false
    var currentPage : Int? = 1
    lateinit var atmListAdapter : AtmListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        database = FirebaseDatabase.getInstance().reference
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        initialiseFirebaseDataBase()
        getAdress()

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
                    text_view_location.setText(address  )



                }

            })
        }
    }

    fun getSortedAtmList () {
        if (atmsListModel?.atms?.isNotEmpty()!!) {
            for (item in atmsListModel?.atms!!) {
                val userLocation = Location ("point A")
                userLocation.latitude = fusedLocationClient.lastLocation.result.latitude
                userLocation.longitude = fusedLocationClient.lastLocation.result.longitude
                val locationB = Location("point B")
                locationB.latitude = item.lat_long?.latitude ?: 0.0
                locationB.longitude = item.lat_long?.longitude ?: 0.0
                val distance = userLocation.distanceTo(locationB)
                if (distance <= 1000){
                    atmSortedList.add(item)
                }


            }
        }

    }


    fun initialiseFirebaseDataBase () {
        val postListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                atmsListModel = snapshot.getValue(AtmsModel::class.java)
                getSortedAtmList()
                val linearLayoutManager = LinearLayoutManager(applicationContext , RecyclerView.VERTICAL,false)
                recycler_view_main.adapter = AtmListAdapter(atmsListModel?.atms!!) {}
                recycler_view_main.addOnScrollListener(object : PaginationScrollListener(linearLayoutManager){
                    override fun isLastPage() : Boolean{
                        return isLastPage
                    }
                    override fun isLoading():Boolean{
                        return isLoading
                    }

                    override fun loadMoreItems() {
                        getAtms(currentPage?.plus(1))
                    }

                })


                val array = arrayOfNulls<String>(cityList.size)
                cityAdapter = CityAdapter(Activity(), cityList.toArray(array))
                spinner.adapter = cityAdapter


                floating_action_btn.setOnClickListener {
                    var bundle = Bundle()
                    bundle.putString("cityClicked", spinner.selectedItem.toString())

                }


            }

        }
        database.addValueEventListener(postListener)


    }

    fun getAtms (page : Int?){
        isLoading = true
        progress_layout.visibility = View.VISIBLE
        var tempAtmList = atmSortedList
        if (tempAtmList != null){
            atmSortedList.addAll(tempAtmList)
            atmListAdapter.addAtms(atmSortedList)
            isLoading = false
            progress_layout.visibility = View.GONE
            currentPage = page



            }

    }









    }


