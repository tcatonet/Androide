package View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androidproject.R
import kotlinx.android.synthetic.main.activity_no_network.*

class NoNetworkActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_network)

        retry?.setOnClickListener {
            val intent = Intent(this, LoaderActivity::class.java)
            startActivity(intent)
        }

        continue_?.setOnClickListener {
            val intent = Intent(this, ListeItemsActivity::class.java)
            startActivity(intent)
        }

    }




}