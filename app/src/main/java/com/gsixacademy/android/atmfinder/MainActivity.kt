package com.gsixacademy.android.atmfinder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.gsixacademy.android.atmfinder.adapter.AtmListAdapter
import com.gsixacademy.android.atmfinder.data.AtmsData
import com.gsixacademy.android.atmfinder.data.AtmsModel
import com.gsixacademy.android.atmfinder.utils.CurrentLocationActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var database : DatabaseReference
    var atmsListModel : AtmsModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        database = FirebaseDatabase.getInstance().reference

        recycler_view_main.layoutManager = LinearLayoutManager(this)
        val adapter  = AtmListAdapter(ArrayList<AtmsData>()){}
        recycler_view_main.adapter = adapter

        recycler_view_main.setOnClickListener{
            val intent = Intent(this,MapFragment::class.java)
            startActivity(intent)
            initialiseFirebaseDataBase()



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


