package home.test.running

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import home.test.running.databinding.FaceResultBinding


//달리기 결과 담당 엑티비티
class FaceResultActivity : AppCompatActivity() {

    private lateinit var binding:FaceResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = FaceResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

      
    }


}