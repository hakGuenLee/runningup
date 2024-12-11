package home.test.running

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Handler
import android.widget.Toast
import androidx.core.content.ContextCompat

// 권한 요청/허용 상태 확인 객체
class PermissionChecker(private val context: Context) {

    private val REQUEST_PERMISSIONS = 1

    fun permissionRequestChecker() {
        val permissionList = mutableMapOf<String, String>()
        permissionList["location"] = Manifest.permission.ACCESS_COARSE_LOCATION
        permissionList["fineLocation"] = Manifest.permission.ACCESS_FINE_LOCATION
        permissionList["audio"] = Manifest.permission.READ_MEDIA_AUDIO

        // 권한 허용 여부 확인: map에 담아둔 권한 목록 별로 상태가 deny 되어 있는지 확인
        val permissionDeny = permissionList.count {
            ContextCompat.checkSelfPermission(context, it.value) == PackageManager.PERMISSION_DENIED
        }

        // 결과값이 0보다 크면 deny되어 있는 것. 이때는 권한 요청 페이지로 이동
        if (permissionDeny > 0) {
            Handler().postDelayed({
                val permissionIntent = Intent(context, PermissionRequestActivity::class.java)
                context.startActivity(permissionIntent)
                if (context is MainActivity) {
                    context.finish() // 현재 Activity 종료
                }
            }, 980)

//            (context as? MainActivity)?.requestPermissions(
//                permissionList.values.toTypedArray(),
//                REQUEST_PERMISSIONS
//            )
        } else {
            // 권한이 모두 허용되어 있으면 0.97초 후에 자동으로 화면 전환
            navigateToFaceSelectActivity()
        }
    }

    //권한이 허용되었을 때 FaseSelectActivity로 전환
    fun navigateToFaceSelectActivity() {
        Handler().postDelayed({
            val intent = Intent(context, FaceSelectActivity::class.java)
            context.startActivity(intent)
            if (context is MainActivity) {
                context.finish() // 현재 Activity 종료
            }
        }, 970) // 0.97초 지연
    }


    // 권한 요청 결과 처리
    fun handleRequestPermissionsResult(
        requestCode: Int,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_PERMISSIONS) {
            grantResults.forEach {
                if (it == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(context, "앱 실행에 필요한 권한입니다. 동의해주세요.", Toast.LENGTH_SHORT).show()
//                    if (context is MainActivity) {
//                        context.finish() // 사용자가 권한 허용을 거절하면 위 메시지 띄우면서 앱 종료
//                    }
                } else if (it == PackageManager.PERMISSION_GRANTED) {
                    navigateToFaceSelectActivity() // 권한을 허용하면 자동으로 다음 화면 전환
                }
            }
        }
    }
}
