package fr.unilim.iut.todolist.dialog

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.icu.util.Calendar
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import fr.unilim.iut.todolist.R
import fr.unilim.iut.todolist.classes.Task
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.launch

class AddTaskDialogFragment : BaseDialog("Nouvelle tâche"), DatePickerDialog.OnDateSetListener {
    private lateinit var _task: Task
    private lateinit var dateEditText: EditText

    val task:Task
        get() = _task

    override fun customizeDialog(builder: AlertDialog.Builder, inflater:LayoutInflater): Dialog {
        val view = inflater.inflate(R.layout.add_task_dialog, null)
        view.findViewById<TextView>(R.id.dialog_title).text = title

        val titleEditText = view.findViewById<EditText>(R.id.dialog_add_task_title)
        dateEditText = view.findViewById(R.id.dialog_add_task_date)
        dateEditText.isFocusable = false
        val checkBox = view.findViewById<CheckBox>(R.id.dialog_add_task_checkbox)
        checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            when(isChecked){
                true -> {
                    val c = Calendar.getInstance()
                    val year = c.get(Calendar.YEAR)
                    val month = c.get(Calendar.MONTH)
                    val day = c.get(Calendar.DAY_OF_MONTH)
                    val datePickerDialog = DatePickerDialog(
                        requireActivity(),
                        this,
                        year,
                        month,
                        day
                    )


                    datePickerDialog.show()

                }
                false -> {
                    dateEditText.text.clear()
                    dateEditText.visibility = View.INVISIBLE
                }
            }
        }
        builder
            .setView(view)
            .setPositiveButton("OK") { dialog, which ->
                _task = Task(-1, titleEditText.text.toString(), R.string.task_status_awaiting, dateEditText.text.toString())
                listener.onDialogPositiveClick(this)
            }
            .setNegativeButton("ANNULER"){dialog, i -> listener.onDialogNegativeClick(this)}
            .setCancelable(true)
        return builder.create()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        dateEditText.text.clear()
        dateEditText.text.append("$dayOfMonth/${month+1}/$year")
        dateEditText.visibility = View.VISIBLE
    }
}