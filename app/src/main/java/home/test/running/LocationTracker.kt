package home.test.running

import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import android.content.Context
import android.Manifest
import android.content.pm.PackageManager

//위치 확인 및 페이스 계산 객체
class LocationTracker(private val context: Context, private val locationManager: LocationManager, private val callback: LocationCallback) : LocationListener {

    interface LocationCallback {
        fun onLocationUpdated(distance: Double, pace: String)
    }

    private var totalDistance = 0.0
    private var lastLocation: Location? = null

    // 위치 업데이트 시작
    fun startTracking() {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // 권한 요청
            return
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1f, this)
    }

    // 위치 업데이트 중지
    fun stopTracking() {
        locationManager.removeUpdates(this)
    }

    // 위치 변경 시 호출
    override fun onLocationChanged(location: Location) {
        if (lastLocation == null) {
            lastLocation = location
            return
        }

        // 두 지점 사이의 거리 계산 (km 단위)
        val distance = lastLocation!!.distanceTo(location) / 1000
        totalDistance += distance

        // 페이스 계산
        val speed = location.speed * 3.6f // m/s to km/h 변환
        val pace = calculatePace(speed)

        // 위치 및 페이스 업데이트
        callback.onLocationUpdated(totalDistance, pace)

        lastLocation = location
    }

    // 페이스 계산 (km/h를 min/km로 변환)
    private fun calculatePace(speed: Float): String {
        return if (speed > 0) {
            val paceInMinutes = 60 / speed // 시간당 거리에서 페이스 계산
            val minutes = paceInMinutes.toInt()
            val seconds = ((paceInMinutes - minutes) * 60).toInt()
            String.format("%02d:%02d", minutes, seconds)
        } else {
            "정지 상태"
        }
    }
}
