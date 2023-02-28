package fr.unilim.iut.todolist.classes;

import java.util.*

//creating a Data Model Class
class Task (
    private val description: String,
    private val state: String,
    private val date: Long
) {
    private var taskId: Double = UUID.randomUUID().toString().toDouble()

    fun getID()     : Double = taskId
    fun getDesc()   : String = description
    fun getState()  : String = state
    fun getDate()   : Long = date
}
