package fr.unilim.iut.todolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import java.util.*

class Dashboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // DB Activity start
        val intent: Intent = Intent(this, DBActivity::class.java)
        val buttonDB: Button = findViewById(R.id.db_button)
        buttonDB.setOnClickListener {
            startActivity(intent)
        }
    }
}