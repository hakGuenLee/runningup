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

//GPS를 통해 속도 측정하는 엑티비티.
//속도측정 구현 완료 후 각 옵션별로 기준점 설정하여 미디어 볼륨 조절 예정

class RunningStartActivity : AppCompatActivity(), LocationListener {

    private lateinit var locationManager: LocationManager
    private lateinit var speedTextView: TextView
    private var lastLocation: Location? = null
    private val speedThreshold = 0.1f // 속도 임계값

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.running_start)

        speedTextView = findViewById(R.id.speedchecktext)

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        // 위치 권한 확인 및 요청
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1f, this)
        }
    }

    override fun onLocationChanged(location: Location) {
        // 이동하지 않거나 속도가 너무 낮은 경우 속도를 0으로 표시
        if (lastLocation != null) {
            val speed = location.speed * 3.6f // m/s를 km/h로 변환
            if (speed < speedThreshold) {
                speedTextView.text = "현재 속도 : 0.00 km/h"
            } else {
                speedTextView.text = String.format("현재 속도 : %.2f km/h", speed)
            }
        } else {
            speedTextView.text = String.format("현재 속도 : %.2f km/h", location.speed * 3.6f)
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