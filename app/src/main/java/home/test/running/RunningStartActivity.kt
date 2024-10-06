package home.test.running

import android.content.Context
import android.content.pm.PackageManager

import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import android.Manifest


//GPS로 속도 측정처리 하는 엑티비티
//속도 측정 완료 시 속도 옵션별로 기준 속도를 넘겨 받아서 미디어 볼륨 조절하도록 수정 예정
class RunningStartActivity : AppCompatActivity(), LocationListener{

    private lateinit var locationManager: LocationManager
    private lateinit var speedTextView: TextView
    private var lastLocation: Location? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.running_start)

        speedTextView = findViewById(R.id.speedchecktext)

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1f, this)
        }

    }

    override fun onLocationChanged(location: Location) {
        if(lastLocation != null){
            val speed = location.speed * 3.6f
            speedTextView.text = String.format("현재 속도 : %.2f km/h", speed)

        }

        lastLocation = location
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

    override fun onProviderEnabled(provider: String) {}

    override fun onProviderDisabled(provider: String) {}

    override fun onDestroy() {
        super.onDestroy()
        locationManager.removeUpdates(this)
    }


}