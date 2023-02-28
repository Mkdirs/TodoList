package fr.unilim.iut.todolist.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import fr.unilim.iut.todolist.R

class MyListAdapter(private val context: Activity, private val id: Array<String>, private val desc: Array<String>, private val state: Array<String>, private val date: Array<String>)
    : ArrayAdapter<String>(context, R.layout.custom_list, desc) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.custom_list, null, true)

        val idText = rowView.findViewById(R.id.textViewId) as TextView
        val descText = rowView.findViewById(R.id.textViewDesc) as TextView
        val stateText = rowView.findViewById(R.id.textViewState) as TextView
        val dateText = rowView.findViewById(R.id.textViewDate) as TextView

        idText.text = "Id: ${id[position]}"
        descText.text = "Desc: ${desc[position]}"
        stateText.text = "State: ${state[position]}"
        dateText.text = "Date: ${date[position]}"
        return rowView
    }
}