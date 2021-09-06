package com.example.rememberme.SetNotificationHelper.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {EntityClass.class}, version = 1, exportSchema = false)
public abstract class DatabaseClass extends RoomDatabase {
    private static DatabaseClass INSTANCE;

    public abstract com.example.rememberme.SetNotificationHelper.Database.EventDao EventDao();

    public static DatabaseClass getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (DatabaseClass.class) {
                if (INSTANCE == null) {
                    INSTANCE =
                            Room.databaseBuilder(context.getApplicationContext(),
                                    DatabaseClass.class,
                                    "product_database").allowMainThreadQueries().build();
                }
            }
        }
        return INSTANCE;
    }
}