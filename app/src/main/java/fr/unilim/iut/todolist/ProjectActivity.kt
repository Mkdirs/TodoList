package fr.unilim.iut.todolist

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.database.DataSetObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import fr.unilim.iut.todolist.adapter.TaskListAdapter
import fr.unilim.iut.todolist.classes.Task
import fr.unilim.iut.todolist.dialog.AddTaskDialogFragment
import fr.unilim.iut.todolist.dialog.BaseDialog
import fr.unilim.iut.todolist.handler.DatabaseHandler
import java.time.Instant

class ProjectActivity : AppCompatActivity(), BaseDialog.BaseDialogListener {
    lateinit var adapter: TaskListAdapter
    lateinit var db: DatabaseHandler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project)
        val project = intent.getStringExtra(PROJECT_NAME)!!
        supportActionBar!!.title = project

        db = DatabaseHandler(this)

        adapter = TaskListAdapter(this, db)
        adapter.onRequestChange = {
            adapter.clear()
            adapter.addAll(db.viewTasks(project))
            adapter.notifyDataSetChanged()
        }

        adapter.addAll(db.viewTasks(project))

        val tasks = findViewById<ListView>(R.id.tasks_list_view)
        tasks.adapter = adapter

        val addButtonImgView = findViewById<ImageButton>(R.id.add_button_image_view)
        addButtonImgView.setOnClickListener {
            val d = AddTaskDialogFragment(project)
            d.show(supportFragmentManager, "add_task")
        }
    }

    override fun onDialogPositiveClick(dialog:BaseDialog) {
        db.addTask((dialog as AddTaskDialogFragment).task)
        adapter.onRequestChange()
    }

    override fun onDialogNegativeClick(dialog:BaseDialog) {
        Toast.makeText(this, "Annulé", Toast.LENGTH_SHORT).show()
    }
}