package fr.unilim.iut.todolist.classes;

data class Task (
    var id: Int,
    val desc: String,
    val state: String,
    val date: Long
)
