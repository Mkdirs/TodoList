package fr.unilim.iut.todolist.adapter

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.view.View
import android.view.ViewGroup
import android.widget.*
import fr.unilim.iut.todolist.R
import fr.unilim.iut.todolist.classes.Task
import fr.unilim.iut.todolist.handler.DatabaseHandler

class TaskListAdapter(private val context:Activity, private val db:DatabaseHandler) : ArrayAdapter<Task>(context, R.layout.task_list_item) {

    var onRequestChange:() -> Unit = {}


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val view = convertView ?: inflater.inflate(R.layout.task_list_item, null, true)

        val task = getItem(position)!!

        val desc = view.findViewById<TextView>(R.id.task_list_item_title);
        desc.text = task.desc;

        val checkbox = view.findViewById<CheckBox>(R.id.task_list_item_checkbox)
        checkbox.isChecked = task.state == R.string.task_status_finished


        val sf = SimpleDateFormat("dd/MM/yyyy")
        checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            val cal = Calendar.getInstance()
            val currentDate = cal.time
            val status = when(isChecked){
                true -> {
                    ObjectAnimator.ofFloat(checkbox, "rotation", 0f, 360f).apply {
                        duration = 1000
                        start()
                    }


                    ObjectAnimator.ofFloat(view, "scaleY",2f, 1f, 1.5f, 1f).apply {
                        duration = 500
                        start()
                    }
                    R.string.task_status_finished
                }
                false -> {
                    if (task.date.isEmpty())
                        R.string.task_status_awaiting
                    else if(task.date.isNotEmpty() && currentDate >= sf.parse(task.date)) {
                        R.string.task_status_overdue
                    }else
                        R.string.task_status_awaiting
                }
            }

            db.updateTask(
                Task(task.id, task.desc, status, task.date, task.project)
            )

            onRequestChange()



        }

        view.findViewById<TextView>(R.id.task_list_item_status).also {
            val cal = Calendar.getInstance()
            val currentDate = cal.time

            if(task.date.isNotEmpty() && currentDate >= sf.parse(task.date) && task.state == R.string.task_status_awaiting){
                db.updateTask(
                    Task(
                        task.id,
                        task.desc,
                        R.string.task_status_overdue,
                        task.date,
                        task.project
                    )
                )
                onRequestChange()

            }
            it.text = context.getString(task.state)
            when(task.state){
                R.string.task_status_finished -> it.setTextColor(context.resources.getColor(R.color.green, null))

                R.string.task_status_awaiting -> it.setTextColor(context.resources.getColor(R.color.orange, null))
                R.string.task_status_overdue -> it.setTextColor(context.resources.getColor(R.color.red, null))

            }
        }

        view.findViewById<ImageView>(R.id.task_list_item_delete).setOnClickListener {
            db.deleteTask(task)
            onRequestChange()
        }




        return view
    }
}