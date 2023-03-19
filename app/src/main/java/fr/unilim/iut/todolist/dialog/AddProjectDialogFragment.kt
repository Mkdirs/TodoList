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
import fr.unilim.iut.todolist.classes.Project
import fr.unilim.iut.todolist.classes.Task
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.launch

class AddProjectDialogFragment : BaseDialog("Nouveau projet") {
    private lateinit var _project: Project

    val project:Project
        get() = _project

    override fun customizeDialog(builder: AlertDialog.Builder, inflater:LayoutInflater): Dialog {
        val view = inflater.inflate(R.layout.add_project_dialog, null)
        view.findViewById<TextView>(R.id.dialog_title).text = title

        val nameEditText = view.findViewById<EditText>(R.id.dialog_add_project_name)

        builder
            .setView(view)
            .setPositiveButton("OK") { dialog, which ->
                if(nameEditText.text.isNotEmpty()){
                    _project = Project(nameEditText.text.toString())
                    listener.onDialogPositiveClick(this)
                }
            }
            .setNegativeButton("ANNULER"){dialog, i -> listener.onDialogNegativeClick(this)}
            .setCancelable(true)
        return builder.create()
    }

}