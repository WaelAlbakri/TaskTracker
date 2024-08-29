package edu.utsa.cs3443.tasktrack;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import edu.utsa.cs3443.tasktrack.model.Task;
import edu.utsa.cs3443.tasktrack.model.TaskDatabase;
import java.util.List;
import java.util.concurrent.Executors;

public class MainActivity extends BaseActivity {
    private TaskDatabase taskDatabase;
    private TaskAdapter taskAdapter;
    private ListView taskListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskDatabase = TaskDatabase.getInstance(this);
        taskListView = findViewById(R.id.task_list);
        loadTasks();

        Button addButton = findViewById(R.id.button_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
                startActivity(intent);
            }
        });

        Button deleteButton = findViewById(R.id.button_delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskAdapter.deleteSelectedTasks();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTasks();
    }

    private void loadTasks() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                final List<Task> tasks = taskDatabase.taskDao().getAllTasks();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        taskAdapter = new TaskAdapter(MainActivity.this, tasks);
                        taskListView.setAdapter(taskAdapter);
                    }
                });
            }
        });
    }
}
