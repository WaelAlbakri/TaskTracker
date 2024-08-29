package edu.utsa.cs3443.tasktrack;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import edu.utsa.cs3443.tasktrack.model.Task;
import edu.utsa.cs3443.tasktrack.model.TaskDatabase;
import java.util.concurrent.Executors;

public class AddTaskActivity extends AppCompatActivity {
    private EditText taskTitle;
    private EditText taskDate;
    private EditText taskDescription;
    private CheckBox taskImportant;
    private EditText taskDueDate;
    private TaskDatabase taskDatabase;
    private Task taskToEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtask);

        taskTitle = findViewById(R.id.task_title);
        taskDate = findViewById(R.id.task_date);
        taskDescription = findViewById(R.id.task_description);
        taskImportant = findViewById(R.id.task_important);
        taskDueDate = findViewById(R.id.task_due_date);
        Button saveButton = findViewById(R.id.button_save);

        taskDatabase = TaskDatabase.getInstance(this);

        taskToEdit = (Task) getIntent().getSerializableExtra("task");
        if (taskToEdit != null) {
            taskTitle.setText(taskToEdit.getTitle());
            taskDate.setText(taskToEdit.getDate());
            taskDescription.setText(taskToEdit.getDescription());
            taskImportant.setChecked(taskToEdit.isImportant());
            taskDueDate.setText(taskToEdit.getDueDate());
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTask();
            }
        });
    }

    private void saveTask() {
        final Task task;
        if (taskToEdit != null) {
            task = taskToEdit;
        } else {
            task = new Task();
        }

        task.setTitle(taskTitle.getText().toString());
        task.setDate(taskDate.getText().toString());
        task.setDescription(taskDescription.getText().toString());
        task.setImportant(taskImportant.isChecked());
        task.setDueDate(taskDueDate.getText().toString());

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                if (taskToEdit != null) {
                    taskDatabase.taskDao().update(task);
                } else {
                    taskDatabase.taskDao().insert(task);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                });
            }
        });
    }
}

