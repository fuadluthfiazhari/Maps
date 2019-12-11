package com.example.maps

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.GroundOverlayOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private val permission: Array<String> = arrayOf<String>(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    private val permissionCode = 1100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        cari_lokasi.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                var cari = query.toString()
                var address: List<Address> = emptyList()

                val geocoder = Geocoder(this@MapsActivity)
                address = geocoder.getFromLocationName(cari, 1)

                val hasilCari = address.get(0)
                val latLng = LatLng(hasilCari.latitude, hasilCari.longitude)

                mMap.addMarker(
                    MarkerOptions().position(latLng)
                        .title(cari)
                )
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f))

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun verifyPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(
                    this.applicationContext, permission[0]
                )
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    this.applicationContext, permission[1]
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
                val uty = LatLng(-7.747033, 110.355398)
                val zoomSize = 17.0f
                mMap.addMarker(MarkerOptions().position(uty).title("Universitas Teknologi Yogyakarta"))
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(uty, zoomSize))
                mMap.isMyLocationEnabled = true
            } else {
                ActivityCompat.requestPermissions(this, permission, permissionCode)
            }

        } else {
            val uty = LatLng(-7.747033, 110.355398)
            val zoomSize = 17.0f
            mMap.addMarker(MarkerOptions().position(uty).title("Universitas Teknologi Yogyakarta"))
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(uty, zoomSize))
            mMap.isMyLocationEnabled = true
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val latitude =-7.747033
        val longitude =  110.355398
        val zoomLevel =15f
        val overlaySize = 100f

        val homeLatLng = LatLng(latitude,longitude)
        mMap.addMarker(MarkerOptions().position(homeLatLng).title("Universitas Teknologi Yogjakarta").snippet("-7.747033/110.355398"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(homeLatLng,zoomLevel))


        val googleOverlay= GroundOverlayOptions()
            .image(BitmapDescriptorFactory.fromResource(R.drawable.uty)).position(homeLatLng,overlaySize)
        mMap.addGroundOverlay(googleOverlay)


        verifyPermission()
    }
}
