package fr.unilim.iut.todolist

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.database.DataSetObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import fr.unilim.iut.todolist.adapter.TaskListAdapter
import fr.unilim.iut.todolist.classes.Task
import fr.unilim.iut.todolist.dialog.AddTaskDialogFragment
import fr.unilim.iut.todolist.dialog.BaseDialog
import fr.unilim.iut.todolist.handler.DatabaseHandler
import java.time.Instant

class ProjectActivity : AppCompatActivity(), BaseDialog.BaseDialogListener, AdapterView.OnItemSelectedListener {
    lateinit var adapter: TaskListAdapter
    lateinit var db: DatabaseHandler
    lateinit var project:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project)
        project = intent.getStringExtra(PROJECT_NAME)!!
        supportActionBar!!.title = project

        db = DatabaseHandler(this)

        val spinner = findViewById<Spinner>(R.id.spin)
        ArrayAdapter.createFromResource(this, R.array.task_list_options, android.R.layout.simple_spinner_item).also{
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = it
        }

        spinner.onItemSelectedListener = this

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

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val option = parent!!.getItemAtPosition(position) as String
        val tasks = db.viewTasks(project)
        adapter.clear()
        if(option == resources.getString(R.string.task_listing_all)){
            adapter.addAll(tasks)
        }else{
            adapter.addAll(tasks.filter { resources.getString(it.state) == option })
        }

        adapter.notifyDataSetChanged()

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }
}