package com.example.macarbi;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class Push extends Application {
    public static final String TODO_1_ID = "AddToDo";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel ToDoAdd = new NotificationChannel(
                    TODO_1_ID,
                    "ToDo Added",
                    NotificationManager.IMPORTANCE_HIGH
            );
            ToDoAdd.setDescription("This is ToDo Added Channel");



            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(ToDoAdd);

        }
    }
}
