package com.example.ash1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var marker: Marker

    public lateinit var databaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        databaseRef = Firebase.database.reference
        databaseRef.addValueEventListener(logListener)

    }

    val logListener = object : ValueEventListener {
        override fun onCancelled(error: DatabaseError) {
            Toast.makeText(applicationContext, "Could not read from database", Toast.LENGTH_LONG).show()
        }

        //     @SuppressLint("LongLogTag")
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if (dataSnapshot.exists()) {

                mMap.clear() //to remove prev marker after loc change

                val locationlogging = dataSnapshot.child("BusA").getValue(LocationLogging::class.java)
                var driverLat=locationlogging?.Latitude
                var driverLong=locationlogging?.Longitude
                //Log.d("Latitude of driver", driverLat.toString())
                //    Log.d("Longitude read from database", driverLong.toString()).

                if (driverLat !=null  && driverLong != null) {
                    val driverLoc = LatLng(driverLat, driverLong)

                    val markerOptions = MarkerOptions().position(driverLoc).title("Bus").icon(
                       BitmapDescriptorFactory.fromResource(R.drawable.markerm)) //markerm.png is the bus icon
                    mMap.addMarker(markerOptions)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(driverLoc, 15f))
                    //Zoom level - 1: World, 5: Landmass/continent, 10: City, 15: Streets and 20: Buildings

                    //Toast.makeText(applicationContext, "Locations accessed from the database", Toast.LENGTH_LONG).show()


                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        //adding point for college

        //val college = LatLng(8.470814, 76.979630)
        //mMap.addMarker(MarkerOptions().position(college).title("SCT"))

        Toast.makeText(applicationContext, "Safe travel!", Toast.LENGTH_LONG).show()



        }

}
