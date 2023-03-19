package fr.unilim.iut.todolist

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import fr.unilim.iut.todolist.dialog.AddProjectDialogFragment
import fr.unilim.iut.todolist.dialog.BaseDialog
import fr.unilim.iut.todolist.handler.DatabaseHandler

const val PROJECT_NAME = "PROJECT_NAME"
class Dashboard : AppCompatActivity(), BaseDialog.BaseDialogListener{
    lateinit var db:DatabaseHandler
    lateinit var adapter:ArrayAdapter<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        db = DatabaseHandler(this)
        adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1)

        adapter.addAll(db.viewProjects().map { it.name })

        val listView = findViewById<ListView>(R.id.project_list_view)
        listView.adapter = adapter
        listView.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, ProjectActivity::class.java).apply {
                putExtra(PROJECT_NAME, listView.getItemAtPosition(position) as String)
            }
            startActivity(intent)
        }

        val addButtonImgView = findViewById<ImageButton>(R.id.add_button_image_view)
        addButtonImgView.setOnClickListener {
            val dialog = AddProjectDialogFragment()
            dialog.show(supportFragmentManager, "add_project")
        }


        // DB Activity start
        val intent: Intent = Intent(this, DBActivity::class.java)
        val buttonDB: Button = findViewById(R.id.db_button)
        buttonDB.setOnClickListener {
            startActivity(intent)
        }
    }

    override fun onDialogPositiveClick(dialog: BaseDialog) {
        val project = (dialog as AddProjectDialogFragment).project
        db.addProject(project)
        adapter.add(project.name)
        adapter.notifyDataSetChanged()
    }

    override fun onDialogNegativeClick(dialog: BaseDialog) {
        Toast.makeText(this, "Annulé", Toast.LENGTH_SHORT).show()
    }
}