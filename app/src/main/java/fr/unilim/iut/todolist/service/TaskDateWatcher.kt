package fr.unilim.iut.todolist.service

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Debug
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import fr.unilim.iut.todolist.Dashboard
import fr.unilim.iut.todolist.PROJECT_NAME
import fr.unilim.iut.todolist.ProjectActivity
import fr.unilim.iut.todolist.R
import fr.unilim.iut.todolist.handler.DatabaseHandler

class TaskDateWatcher : Service() {
    lateinit var db:DatabaseHandler

    override fun onBind(intent: Intent): IBinder? {
        return null;
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel("TaskOverdueChannel", "TaskOverdueChannel", NotificationManager.IMPORTANCE_DEFAULT)
        manager.createNotificationChannel(channel)
        for (project in db.viewProjects()){
            val title = resources.getString(R.string.notification_title_task_overdue)
            var content = "";
            val tasks = db.viewTasks(project.name).filter { it.isOverdue() }
            if(tasks.isNotEmpty()){
                tasks.forEach {
                    content += "\t\t-${it.desc}\n"
                }

                val intent = Intent(applicationContext, ProjectActivity::class.java).apply {
                    putExtra(PROJECT_NAME, project.name)
                }

                val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)
                val notif = NotificationCompat.Builder(applicationContext, channel.id)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setSubText("Projet: ${project.name}")
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build()

                manager.notify(project.hashCode(), notif)

            }
        }


        return START_STICKY
    }

    override fun onCreate() {
        db = DatabaseHandler(applicationContext)
    }
}