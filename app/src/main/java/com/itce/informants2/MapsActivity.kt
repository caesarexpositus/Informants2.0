package com.itce.informants2

/*
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.d.informants.helper.Data.PROFILE_MAPS_QUERY
import com.d.informants.helper.UtilityProfiles
import com.d.informants.helper.currLine
import com.d.informants.helper.showToast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    var message = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            setContentView(R.layout.activity_maps)
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            val mapFragment = supportFragmentManager
                .findFragmentById(R._id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)
        } catch (exc: Exception) {
            message = "ERROR onCreate MapsActivity: " + exc.message + "\n" + currLine
        } finally {
            if (message.isNotEmpty())
                UtilityProfiles.showToast(this, message)
        }
    }

        override fun onMapReady(googleMap: GoogleMap) {
            mMap = googleMap

            val location = codeAddress(Uri.encode(PROFILE_MAPS_QUERY))

            mMap.addMarker(location?.let { MarkerOptions().position(it).title(PROFILE_MAPS_QUERY) })
            mMap.moveCamera(CameraUpdateFactory.newLatLng(location) )
            mMap.moveCamera(CameraUpdateFactory.zoomTo(15.0F))

        }

        private fun codeAddress(query: String): LatLng? {
            val geo = Geocoder(this)
            val lstLocation: MutableList<Address> = geo.getFromLocationName(query, 1)
            return if (lstLocation.size > 0) {
                LatLng(lstLocation[0].latitude *1E6, lstLocation[0].longitude*1E6)
            } else
                null
        }
    }
 */


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.itce.informants2.helper.DataProfiles.Companion.PROFILE_MAPS_QUERY

class MapsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        this.initialize()
    }

    private fun initialize() {
        val mapIntent = Intent(Intent.ACTION_VIEW)

        val encodeQuery = Uri.encode(PROFILE_MAPS_QUERY)
        mapIntent.data = Uri.parse("geo:0,0?q=$encodeQuery&z=18")
        if (mapIntent.resolveActivity(packageManager) != null) startActivity(mapIntent)
    }
}


