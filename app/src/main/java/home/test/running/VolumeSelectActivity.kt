package home.test.running

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.media.AudioManager
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView


//볼륨 설정 화면 담당 Activity
class VolumeSelectActivity : AppCompatActivity() {

    private lateinit var audioManager: AudioManager
    private lateinit var volumeSelectBar: SeekBar
    private lateinit var lowVolumeSelectBar: SeekBar
    private lateinit var volumeValueText: TextView
    private lateinit var lowVolumeText: TextView
    private lateinit var volumeSetupBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.volume_select)

        //오디오매니저 객체 얻기
        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager

        //최대볼륨 설정바
        volumeSelectBar = findViewById(R.id.volumeSelectBar)
        //최소볼륨 설정바
        lowVolumeSelectBar = findViewById(R.id.lowVolumeSelectBar)

        //최대볼륨 출력 텍스트
        volumeValueText = findViewById(R.id.volumeValueText)
        //최소볼륨 출력 텍스트
        lowVolumeText = findViewById(R.id.lowVolumeValueText)

        //볼륨설정완료 버튼
        volumeSetupBtn = findViewById(R.id.volumeSetupBtn)

        //현재 기기의 최대 볼륨 얻기
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)

        volumeSelectBar.max = maxVolume
        lowVolumeSelectBar.max = maxVolume


        val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        volumeSelectBar.progress = currentVolume
        lowVolumeSelectBar.progress = currentVolume

        // 최대볼륨바의 변경 리스너 설정
        volumeSelectBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // 현재 볼륨을 조정하고 벨소리 재생
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0)
                volumeValueText.text = "설정한 최대 볼륨: $progress"

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })

        //최소볼륨바의 변경 리스너 설정
        lowVolumeSelectBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // 현재 볼륨을 조정하고 벨소리 재생
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0)
                lowVolumeText.text = "설정한 최소 볼륨: $progress"

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })

        // 버튼 클릭 시 볼륨 값 전달
        volumeSetupBtn.setOnClickListener {
            println("btnClick!!")
            val volumeValue = volumeSelectBar.progress
            val lowVolumeValue = lowVolumeSelectBar.progress

            val intent = Intent(this, RunningSelectActivity::class.java).apply {
                putExtra("volumeValue", volumeValue)
                putExtra("lowVolumeValue",lowVolumeValue)
            }
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

    }


    }







