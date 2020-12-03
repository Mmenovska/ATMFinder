package com.gsixacademy.android.atmfinder



import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Address
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.*
import com.gsixacademy.android.atmfinder.adapter.AtmListAdapter
import com.gsixacademy.android.atmfinder.adapter.AtmsAdapterClickEvent
import com.gsixacademy.android.atmfinder.adapter.CityAdapter
import com.gsixacademy.android.atmfinder.data.AtmsData
import com.gsixacademy.android.atmfinder.data.AtmsModel
import com.gsixacademy.android.atmfinder.data.SpinnerModel
import com.gsixacademy.android.atmfinder.fragment.MapFragment
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet


class MainActivity : AppCompatActivity() {

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var database : DatabaseReference
    var atmsListModel : AtmsModel? = null
    var atmSortedList : ArrayList<AtmsData> = ArrayList()
    lateinit var cityList : ArrayList<SpinnerModel>
    lateinit var cityAdapter: CityAdapter
    lateinit var atmListAdapter: AtmListAdapter
    var wayLatitude : Double = 0.0
    var wayLongitude : Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        database = FirebaseDatabase.getInstance().reference
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        initialiseFirebaseDataBase()
        getAdress()





    }

    fun onRequestPermission (requestCode : Int, permission : Array<out kotlin.String>, grantResult : IntArray) {
        if (requestCode == 2000) {
            if (grantResult.isNotEmpty() && grantResult[0] == PackageManager.PERMISSION_GRANTED) {
                getAdress()
            }
        }

    }


    fun getAdress(){
        if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            val permission = ArrayList<String>()
            permission.add(android.Manifest.permission.ACCESS_FINE_LOCATION)
            permission.add(android.Manifest.permission.ACCESS_COARSE_LOCATION)
            if (permission.isNotEmpty())
                ActivityCompat.requestPermissions(this,permission.toTypedArray(),2000)
        } else {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener (this, { location ->
                if (location != null){
                     wayLatitude = location.getLatitude()
                     wayLongitude = location.getLongitude()
                    val geocoder = Geocoder (this, Locale.getDefault())
                    val addressess : List<Address> = geocoder.getFromLocation(wayLatitude,wayLongitude,1)
                    val address : String = addressess[0].getAddressLine(0).split(",")[0]
                    text_view_location.setText(address  )

                    var cities : HashSet<String> = HashSet()
                    if (atmsListModel != null && atmsListModel?.atms != null)
                        for (item in atmsListModel!!.atms!!){
                            if (item != null)
                                cities.add(item.city.toString())
                        }


                    val array = arrayOfNulls<String>(cities.size)
                    cityAdapter = CityAdapter(this,cities.toArray(array))
                    spinner.adapter = cityAdapter

                    floating_action_btn.setOnClickListener {
                        var bundle = Bundle()
                        bundle.putString("cityClicked", spinner.selectedItem.toString())
                    }





                }

            })
        }
    }

    fun getSortedAtmList (){
        if (atmsListModel?.atms!!.isNotEmpty())
            for (item in atmsListModel?.atms!!) {
                val userLocation = Location("point A")
               userLocation.latitude = wayLatitude
                userLocation.longitude = wayLongitude
                val locationB = Location("point B")
                locationB.latitude = item.lat_long?.latitude ?: 0.0
                locationB.longitude = item.lat_long?.longitude ?: 0.0
                val distance = userLocation.distanceTo(locationB)
//                if (distance <= 1000) {
                    atmSortedList.add(item)

                    recycler_view_main.adapter = AtmListAdapter(atmSortedList) {
                        if (it is AtmsAdapterClickEvent.onAtmsAdapterItemClicked) {
                            var intent = Intent(applicationContext, MapFragment::class.java)
                            intent.putExtra("atmClicked", it.atmsData.id)
                            startActivity(intent)


                        }
                    }
//                }
            }
        }


    private fun initialiseFirebaseDataBase () {
        val postListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }


            override fun onDataChange(snapshot: DataSnapshot) {
                atmsListModel = snapshot.getValue(AtmsModel::class.java)
                getSortedAtmList()





            }



        }

        database.addValueEventListener(postListener)

            }





        }










