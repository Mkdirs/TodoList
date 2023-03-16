package fr.unilim.iut.todolist

import android.database.DataSetObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import fr.unilim.iut.todolist.adapter.TaskListAdapter
import fr.unilim.iut.todolist.classes.Task
import fr.unilim.iut.todolist.handler.DatabaseHandler
import java.time.Instant

class ProjectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project)
        actionBar?.title = intent.getStringExtra(PROJECT_NAME)

        val db = DatabaseHandler(this)
        db.addTask(Task(
            0, "Développement", R.string.task_status_overdue, "16/03/2023"
        ))
        db.addTask(Task(
            1, "Maquettage", R.string.task_status_awaiting, "20/12/2023"
        ))
        db.addTask(Task(
            2,
            "Modèle MVC", R.string.task_status_finished, "11/01/2023"
        ))

        val adapter = TaskListAdapter(this, db)
        adapter.onRequestChange = {
            adapter.clear()
            adapter.addAll(db.viewTasks())
            adapter.notifyDataSetChanged()
        }

        adapter.addAll(db.viewTasks())

        val tasks = findViewById<ListView>(R.id.tasks_list_view)
        tasks.adapter = adapter

        val addButtonImgView = findViewById<ImageView>(R.id.add_button_image_view)
        addButtonImgView.setOnClickListener {
            Toast.makeText(this, "TODO: ajout nouvelle tâche", Toast.LENGTH_SHORT).show()
        }
    }
}