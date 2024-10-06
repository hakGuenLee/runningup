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
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private val REQUEST_PERMISSIONS = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

       permissionRequestChecker()

        //첫 화면 가운데 텍스트 얻어오기
        val mainText = findViewById<TextView>(R.id.main_text)

        //첫 화면 텍스트 클릭 시 다음 화면 전환
        mainText.setOnClickListener{
            val intent = Intent(this, RunningStartActivity::class.java)
            startActivity(intent)
        }
    }



    //권한 허용 요청
    private fun permissionRequestChecker(){
        val permissionList = mutableMapOf<String, String>()
        permissionList["location"] = Manifest.permission.ACCESS_COARSE_LOCATION
        permissionList["fineLocation"] = Manifest.permission.ACCESS_FINE_LOCATION

        //권한 허용 여부 확인
        val permissionDeny = permissionList.count{ ContextCompat.checkSelfPermission(this,it.value) == PackageManager.PERMISSION_DENIED}

        if(permissionDeny > 0){
            requestPermissions(permissionList.values.toTypedArray(),REQUEST_PERMISSIONS)
        }


    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQUEST_PERMISSIONS){

            grantResults.forEach {
                if(it == PackageManager.PERMISSION_DENIED){
                    Toast.makeText(applicationContext,"앱 실행에 필요한 권한입니다. 동의해주세요.",Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

        }


    }





}