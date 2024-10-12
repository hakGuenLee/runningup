package home.test.running

import android.app.Service
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.media.AudioManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import androidx.localbroadcastmanager.content.LocalBroadcastManager

//백그라운드에서도 수행되도록 하는 서비스.
//서비스단에서 핵심 기능 수행.
//엑티비티와의 데이터 통신은 굳이 외부와 통신할 필요가 없기 때문에 보안 강화의 목적으로 로컬브로드캐스트 사용.
class RunningService : Service(), LocationListener {

    private lateinit var locationManager: LocationManager
    private lateinit var audioManager: AudioManager
    private var speedThreshold = 0.0f
    private var userSelectVolumeValue = 0
    private var userSelectLowVolumeValue = 0

    override fun onCreate() {
        super.onCreate()
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager

        // 포그라운드 서비스 시작 (백그라운드에서도 수행)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "RunningServiceChannel"
            val channel = NotificationChannel(channelId, "Running Service", NotificationManager.IMPORTANCE_DEFAULT)
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)

            val notification = NotificationCompat.Builder(this, channelId)
                .setContentTitle("RUNNING UP")
                .setContentText("계속 달리세요!!")

                .build()

            startForeground(1, notification)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            userSelectVolumeValue = it.getIntExtra("userSelectVolume", 0)
            userSelectLowVolumeValue = it.getIntExtra("userSelectLowVolume", 0)
            speedThreshold = it.getFloatExtra("speedThreshold", 0.0f)
            startLocationUpdates()
        }

        return START_STICKY
    }

    private fun startLocationUpdates() {
        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 5f, this)
            //달리기에 적합하도록 위치업데이트를 2초에 한번씩 수행하도록 늘려봄. 
            //단, 위치 측정을 위한 최소 이동 거리가 5m이기 때문에, 5m 이상 이동하지 않으면 수행되지 않음
        }
    }

    override fun onLocationChanged(location: Location) {
        val speed = location.speed * 3.6f // m/s를 km/h로 변환
        val adjustedVolume = volumeChanger(speed)

        // 속도와 볼륨을 Activity로 LocalBroadcast
        val intent = Intent("SPEED_UPDATE")
        intent.putExtra("speed", speed)
        intent.putExtra("adjustedVolume", adjustedVolume)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    private fun volumeChanger(speed: Float): Int {
        var currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

        val newVolume: Int = when {
            speed < speedThreshold * 0.2 -> userSelectLowVolumeValue
            speed < speedThreshold * 0.4 -> userSelectLowVolumeValue / 6
            speed < speedThreshold * 0.6 -> userSelectVolumeValue / 4
            speed < speedThreshold * 0.8 -> userSelectVolumeValue / 2
            else -> userSelectVolumeValue
        }

        if (currentVolume != newVolume) {
            val handler = Handler(mainLooper)
            val runnable = object : Runnable {
                override fun run() {
                    if (currentVolume != newVolume) {
                        val targetVolume = (currentVolume + if (newVolume > currentVolume) 1 else -1).coerceIn(0, userSelectVolumeValue)
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, targetVolume, 0)
                        currentVolume = targetVolume
                        handler.postDelayed(this, 100)
                    }
                }
            }
            handler.post(runnable)
        }

        return newVolume // 반환된 값을 Activity로 전달
    }

    override fun onDestroy() {
        super.onDestroy()
        locationManager.removeUpdates(this)
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)

        stopSelf()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}