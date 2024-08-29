package edu.utsa.cs3443.tasktrack;


import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                startActivity(new Intent(this, MainActivity.class));
                return true;
            case R.id.completed:
                startActivity(new Intent(this, CompletedTasksActivity.class));
                return true;
            case R.id.overdue:
                startActivity(new Intent(this, OverdueTasksActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
