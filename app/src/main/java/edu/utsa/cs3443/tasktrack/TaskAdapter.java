package edu.utsa.cs3443.tasktrack;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import edu.utsa.cs3443.tasktrack.model.Task;
import edu.utsa.cs3443.tasktrack.model.TaskDatabase;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

public class TaskAdapter extends ArrayAdapter<Task> {
    private List<Task> tasks;
    private TaskDatabase taskDatabase;

    public TaskAdapter(Context context, List<Task> tasks) {
        super(context, 0, tasks);
        this.tasks = tasks;
        this.taskDatabase = TaskDatabase.getInstance(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Task task = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.task_item, parent, false);
        }

        final CheckBox completeCheckBox = convertView.findViewById(R.id.check_complete);
        completeCheckBox.setChecked(task.isCompleted());
        completeCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task.setCompleted(completeCheckBox.isChecked());
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        TaskDatabase.getInstance(getContext()).taskDao().update(task);
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                notifyDataSetChanged();
                            }
                        });
                    }
                });
            }
        });

        TextView taskTitle = convertView.findViewById(R.id.task_title);
        taskTitle.setText(task.getTitle());

        TextView taskDate = convertView.findViewById(R.id.task_date);
        taskDate.setText(task.getDueDate());

        TextView taskDescription = convertView.findViewById(R.id.task_description);
        taskDescription.setText(task.getDescription());

        Button editButton = convertView.findViewById(R.id.button_edit);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddTaskActivity.class);
                intent.putExtra("task", task);
                getContext().startActivity(intent);
            }
        });

        // Check if the task is overdue
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
        try {
            Date taskDueDate = sdf.parse(task.getDueDate());
            Date currentDate = new Date();
            if (taskDueDate != null && currentDate.after(taskDueDate) && !task.isCompleted()) {
                task.setOverdue(true);
                TaskDatabase.getInstance(getContext()).taskDao().update(task);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Update text color based on importance
        updateTextColor(taskTitle, taskDate, taskDescription, task.isImportant());

        return convertView;
    }

    private void updateTextColor(TextView taskTitle, TextView taskDate, TextView taskDescription, boolean isImportant) {
        int color = isImportant ? Color.RED : Color.BLACK;
        taskTitle.setTextColor(color);
        taskDate.setTextColor(color);
        taskDescription.setTextColor(color);
    }


    public List<Task> getTasks() {
        return tasks;
    }

    public void deleteSelectedTasks() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                // Delete the selected tasks
                for (Task task : tasks) {
                    if (task.isCompleted()) { // Assuming checkbox is used for selection
                        taskDatabase.taskDao().delete(task);
                    }
                }

                // Fetch updated task list in background thread
                final List<Task> updatedTasks = taskDatabase.taskDao().getAllTasks();

                // Update UI in main thread
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        tasks.clear();
                        tasks.addAll(updatedTasks);
                        notifyDataSetChanged();
                    }
                });
            }
        });
    }
}

