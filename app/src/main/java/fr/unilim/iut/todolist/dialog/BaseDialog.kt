package fr.unilim.iut.todolist.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import fr.unilim.iut.todolist.R

abstract class BaseDialog(val title:String) : DialogFragment() {

    lateinit var listener: BaseDialogListener

    interface BaseDialogListener{
        fun onDialogPositiveClick(dialog:BaseDialog)
        fun onDialogNegativeClick(dialog:BaseDialog)
    }

    abstract fun customizeDialog(builder:AlertDialog.Builder, inflater: LayoutInflater):Dialog

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater

        val builder = AlertDialog.Builder(activity)

        return customizeDialog(builder, inflater)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        listener = context as BaseDialogListener
    }
}