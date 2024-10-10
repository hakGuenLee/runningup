package home.test.running

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat


//달리기 옵션 설정 화면 담당 엑티비티
class RunningSelectActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.running_select)

        //사용자가 전 화면에서 선택한 최대볼륨
        val userSelectVolume = intent.getIntExtra("volumeValue",0)
        println(userSelectVolume)

        //각 버튼 얻어오기
        val slowRunner = findViewById<Button>(R.id.slow_runner)
        val reqularRunner = findViewById<Button>(R.id.running_selector)
        val fastRunner = findViewById<Button>(R.id.fastest_runner)

        //느리게 뛰기 속도 기준값 :
        val slowSpeed = 0.833f * 3.6f
        //평균 속도 기준값 : 7km/h
        val regularSpeed = 7.0f
        //전력질주 속도 기준값 : 10km/h
        val fastSpeed = 10.0f

        //속도측정 엑티비티로 넘어가주는 인텐트 생성
        val intent = Intent(this, RunningStartActivity::class.java)



        //각 버튼 클릭마다 속도 기준값 담아서 보내기
        slowRunner.setOnClickListener{
            intent.putExtra("speedValue",slowSpeed)
            intent.putExtra("userSelectVolume",userSelectVolume)
            startActivity(intent)
        }

        reqularRunner.setOnClickListener {
            intent.putExtra("speedValue",regularSpeed)
            intent.putExtra("userSelectVolume",userSelectVolume)
            startActivity(intent)
        }

        fastRunner.setOnClickListener {
            intent.putExtra("speedValue",fastSpeed)
            intent.putExtra("userSelectVolume",userSelectVolume)
            startActivity(intent)
        }

      
    }








}