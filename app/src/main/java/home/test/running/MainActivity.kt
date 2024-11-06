package home.test.running

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import home.test.running.databinding.ActivityMainBinding


//앱 실행 첫 화면
class MainActivity : AppCompatActivity() {

    private val REQUEST_PERMISSIONS = 1
    private lateinit var binding: ActivityMainBinding
    private lateinit var permissionChecker: PermissionChecker
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //권한 요청 호출 : 사용자가 권한을 변경할 수도 있기 때문에 앱 실행할 때마다 항상 체크
        permissionChecker = PermissionChecker(this)
        permissionChecker.permissionRequestChecker()
    }

    // 권한 요청 결과 처리
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionChecker.handleRequestPermissionsResult(requestCode, grantResults)
    }






}