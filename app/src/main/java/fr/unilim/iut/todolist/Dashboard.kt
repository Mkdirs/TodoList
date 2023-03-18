package fr.unilim.iut.todolist

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import fr.unilim.iut.todolist.handler.DatabaseHandler
import java.util.*

public const val PROJECT_NAME = "PROJECT_NAME"
class Dashboard : AppCompatActivity() {
    lateinit var db:DatabaseHandler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        db = DatabaseHandler(this)
        val listAdaper = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1)
        listAdaper.add("TodoList")

        val listView = findViewById<ListView>(R.id.project_list_view)
        listView.adapter = listAdaper
        listView.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, ProjectActivity::class.java).apply {
                putExtra(PROJECT_NAME, listView.getItemAtPosition(position) as String)
            }
            startActivity(intent)
        }

        val addButtonImgView = findViewById<ImageButton>(R.id.add_button_image_view)
        addButtonImgView.setOnClickListener {
            Toast.makeText(this, "TODO: ajout nouveau projet", Toast.LENGTH_SHORT).show()
        }


        // DB Activity start
        val intent: Intent = Intent(this, DBActivity::class.java)
        val buttonDB: Button = findViewById(R.id.db_button)
        buttonDB.setOnClickListener {
            startActivity(intent)
        }
    }

}