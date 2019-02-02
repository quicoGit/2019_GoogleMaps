package com.plumbaria.e_7_5_ejemplogooglemaps

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),
    GoogleMap.OnMapClickListener,
    OnMapReadyCallback
{
    private companion object {
        val PERMISO_LOCALIZACION : Array<String> =
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        val PETICION_LOCALLIZACION = 123
    }
    private val BENIDORM : LatLng = LatLng(38.543685, -0.132227)
    private val PEREMARIA : LatLng = LatLng(38.553489, -0.121579)
    private var mapa : GoogleMap? = null
    private var mapFragment :SupportMapFragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (!hayPermisoLocalizacion()) {
            pedirPermisos()
        }
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment!!.getMapAsync(this)

    }

    override fun onResume() {
        super.onResume()
        if (!hayPermisoLocalizacion()) {
            error()
            pedirPermisos()
        } else {
            mapFragment!!.getMapAsync(this)
        }
    }

    fun pedirPermisos() {
        ActivityCompat.requestPermissions(this, PERMISO_LOCALIZACION, PETICION_LOCALLIZACION)
    }

    fun hayPermiso(permiso:String) : Boolean {
        return ContextCompat.checkSelfPermission(this, permiso) == PackageManager.PERMISSION_GRANTED
    }

    fun hayPermisoLocalizacion(): Boolean {
        return hayPermiso(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun error() {
        Toast.makeText(this, "La aplicación no puede funcionar sin permisos de localización", Toast.LENGTH_SHORT).show()
    }

    // Este método se ejecuta cada vez que hacemos clic sobre el mapa.
    override fun onMapClick(p0: LatLng?) {
        mapa?.addMarker(MarkerOptions().position(p0!!).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)))
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap?) {
        if (hayPermisoLocalizacion()) {
            mapa = googleMap
            mapa?.mapType = GoogleMap.MAP_TYPE_NORMAL
            mapa?.moveCamera(CameraUpdateFactory.newLatLngZoom(BENIDORM, 13f))
            mapa?.isMyLocationEnabled = true
            mapa?.setOnMapClickListener(this)
            mapa?.addMarker(MarkerOptions().position(PEREMARIA).title("IES Pere Mª").snippet("IES Pere Maria Orts i Bosch"))
        } else {
            pedirPermisos()
        }
    }

    /* Mover la cámara */
    fun moverCamara(v:View) {
        mapa?.mapType = GoogleMap.MAP_TYPE_NORMAL
        mapa?.moveCamera(CameraUpdateFactory.newLatLngZoom(BENIDORM,13f))
    }

    /* Animar camara */
    fun animarCamara(v:View) {
        mapa?.mapType = GoogleMap.MAP_TYPE_HYBRID
        mapa?.animateCamera(CameraUpdateFactory.newLatLngZoom(PEREMARIA,18f))
    }

    fun aMiPosicion(v:View) {
        if (mapa?.myLocation != null) {
            mapa?.mapType = GoogleMap.MAP_TYPE_SATELLITE
            mapa?.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        mapa?.myLocation?.latitude!!,
                        mapa?.myLocation?.longitude!!
                    ), 20f
                )
            )
        }
    }
}
