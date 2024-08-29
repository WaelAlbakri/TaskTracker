package edu.utsa.cs3443.tasktrack;

import android.os.Bundle;
import android.widget.ListView;
import edu.utsa.cs3443.tasktrack.model.Task;
import edu.utsa.cs3443.tasktrack.model.TaskDatabase;
import java.util.List;
import java.util.concurrent.Executors;

public class OverdueTasksActivity extends BaseActivity {
    private TaskDatabase taskDatabase;
    private TaskAdapter taskAdapter;
    private ListView taskListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_overdue);

        taskDatabase = TaskDatabase.getInstance(this);
        taskListView = findViewById(R.id.task_list);
        loadOverdueTasks();
    }

    private void loadOverdueTasks() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                final List<Task> tasks = taskDatabase.taskDao().getOverdueTasks();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        taskAdapter = new TaskAdapter(OverdueTasksActivity.this, tasks);
                        taskListView.setAdapter(taskAdapter);
                    }
                });
            }
        });
    }
}
