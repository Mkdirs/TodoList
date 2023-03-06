package fr.unilim.iut.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import fr.unilim.iut.todolist.adapter.TaskListAdapter
import fr.unilim.iut.todolist.classes.Task
import java.time.Instant

class ProjectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project)
        actionBar?.title = intent.getStringExtra(PROJECT_NAME)

        val adapter = TaskListAdapter(this)
        adapter.addAll(
            Task(0, "Maquettage", resources.getString(R.string.task_status_awaiting), 0),
            Task(0, "Développement", resources.getString(R.string.task_status_overdue), 0)
        )

        val tasks = findViewById<ListView>(R.id.tasks_list_view)
        tasks.adapter = adapter

        val addButtonImgView = findViewById<ImageView>(R.id.add_button_image_view)
        addButtonImgView.setOnClickListener {
            Toast.makeText(this, "TODO: ajout nouvelle tâche", Toast.LENGTH_SHORT).show()
        }
    }
}