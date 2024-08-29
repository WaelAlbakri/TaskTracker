package edu.utsa.cs3443.tasktrack.model;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Task.class}, version = 5, exportSchema = false)
public abstract class TaskDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();

    private static TaskDatabase INSTANCE;

    public static TaskDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TaskDatabase.class, "task_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}
