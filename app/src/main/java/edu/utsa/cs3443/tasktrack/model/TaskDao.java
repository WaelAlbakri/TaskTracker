package edu.utsa.cs3443.tasktrack.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM tasks")
    List<Task> getAllTasks();

    @Query("SELECT * FROM tasks WHERE completed = 1")
    List<Task> getCompletedTasks();

    @Query("SELECT * FROM tasks WHERE dueDate < date('now') AND completed = 0")
    List<Task> getOverdueTasks();

    @Insert
    void insert(Task task);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);
}
