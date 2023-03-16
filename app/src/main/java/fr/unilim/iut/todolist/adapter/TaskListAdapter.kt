package fr.unilim.iut.todolist.adapter

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import fr.unilim.iut.todolist.R
import fr.unilim.iut.todolist.classes.Task

class TaskListAdapter(private val context:Activity) : ArrayAdapter<Task>(context, R.layout.task_list_item) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val view = convertView ?: inflater.inflate(R.layout.task_list_item, null, true)

        val task = getItem(position)

        /*val dropdown = view.findViewById<ImageView>(R.id.task_list_item_dropdown_arrow)
        dropdown.setOnClickListener {

            it.rotation = if(it.rotation == 90f) 0f else 90f
        }

         */

        val checkbox = view.findViewById<CheckBox>(R.id.task_list_item_checkbox)
        checkbox.isChecked = task?.state == R.string.task_status_finished

        checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            /*
            TODO: modifier l'état de la tâche
                - Modifier la tâche sur la base de donnée
                - Remplacer l'ancienne tâche par la nouvelle

             */

        }

        view.findViewById<TextView>(R.id.task_list_item_title).text = task?.desc
        view.findViewById<TextView>(R.id.task_list_item_status).also {
            it.text = context.getString(task!!.state)
            when(task.state){
                R.string.task_status_finished -> it.setTextColor(context.resources.getColor(R.color.green, null))

                R.string.task_status_awaiting -> it.setTextColor(context.resources.getColor(R.color.orange, null))
                R.string.task_status_overdue -> it.setTextColor(context.resources.getColor(R.color.red, null))

            }
        }

        view.findViewById<ImageView>(R.id.task_list_item_delete).setOnClickListener {
            remove(task)
            //TODO: supprimer la tâche de la BDD
            notifyDataSetChanged()
        }


        return view
    }
}