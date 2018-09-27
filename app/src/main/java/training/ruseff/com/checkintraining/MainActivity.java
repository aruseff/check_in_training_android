package training.ruseff.com.checkintraining;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.Calendar;

import training.ruseff.com.checkintraining.db.DBDataHelper;
import training.ruseff.com.checkintraining.utils.DateUtils;
import training.ruseff.com.checkintraining.utils.Types;

public class MainActivity extends AppCompatActivity {

    CalendarView calendarView;
    Button childButton;
    Button adultButton;
    FloatingActionMenu floatingActionMenu;
    FloatingActionButton manageUsersButton;
    FloatingActionButton statisticsButton;
    TextView dateTextView;
    TextView childCountTextView;
    TextView adultCountTextView;
    SpinKitView progressBarChild;
    SpinKitView progressBarAdult;


    Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFields();
        updateDateAndCount();

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month,
                                            int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                updateDateAndCount();
            }
        });

        childButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent checkInIntent = new Intent(MainActivity.this, CheckInUsersActivity.class);
                checkInIntent.putExtra("day", calendar.get(Calendar.DAY_OF_MONTH));
                checkInIntent.putExtra("month", calendar.get(Calendar.MONTH));
                checkInIntent.putExtra("year", calendar.get(Calendar.YEAR));
                checkInIntent.putExtra("type", Types.CHILD);
                MainActivity.this.startActivity(checkInIntent);
            }
        });

        adultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent checkInIntent = new Intent(MainActivity.this, CheckInUsersActivity.class);
                checkInIntent.putExtra("day", calendar.get(Calendar.DAY_OF_MONTH));
                checkInIntent.putExtra("month", calendar.get(Calendar.MONTH));
                checkInIntent.putExtra("year", calendar.get(Calendar.YEAR));
                checkInIntent.putExtra("type", Types.ADULT);
                MainActivity.this.startActivity(checkInIntent);
            }
        });

        manageUsersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent manageUsersIntent = new Intent(MainActivity.this, ManageUsersActivity.class);

                MainActivity.this.startActivity(manageUsersIntent);
                floatingActionMenu.close(false);
            }
        });

        statisticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent statisticsIntent = new Intent(MainActivity.this, StatisticsActivity.class);
                MainActivity.this.startActivity(statisticsIntent);
                floatingActionMenu.close(false);
            }
        });

    }

    private void updateDateAndCount() {
        dateTextView.setText(DateUtils.dateToString(calendar.getTime()));
        Integer[] params = {calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR)};
        new UpdateDateAndCountChildAsyncTask().execute(params);
        new UpdateDateAndCountAdultAsyncTask().execute(params);
    }

    private void initFields() {
        calendarView = findViewById(R.id.calendarView);
        childButton = findViewById(R.id.childTraining);
        adultButton = findViewById(R.id.adultTraining);
        floatingActionMenu = findViewById(R.id.floatingActionMenu);
        manageUsersButton = findViewById(R.id.manageUsersButton);
        statisticsButton = findViewById(R.id.statisticsButton);
        dateTextView = findViewById(R.id.dateTextView);
        childCountTextView = findViewById(R.id.childCountTextView);
        adultCountTextView = findViewById(R.id.adultCountTextView);
        progressBarChild = findViewById(R.id.progressBarChild);
        progressBarAdult = findViewById(R.id.progressBarAdult);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateDateAndCount();
    }

    private class UpdateDateAndCountChildAsyncTask extends AsyncTask<Integer, Void, Integer> {

        @Override
        protected Integer doInBackground(Integer... param) {
            if (param.length == 3) {
                return DBDataHelper.getCountByDateAndType(param[0], param[1], param[2], Types.CHILD);
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            progressBarChild.setVisibility(View.GONE);
            childCountTextView.setText(String.valueOf(result));
        }

        @Override
        protected void onPreExecute() {
            adultCountTextView.setText("");
            progressBarChild.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private class UpdateDateAndCountAdultAsyncTask extends AsyncTask<Integer, Void, Integer> {

        @Override
        protected Integer doInBackground(Integer... param) {
            if (param.length == 3) {
                return DBDataHelper.getCountByDateAndType(param[0], param[1], param[2], Types.ADULT);
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            progressBarAdult.setVisibility(View.GONE);
            adultCountTextView.setText(String.valueOf(result));
        }

        @Override
        protected void onPreExecute() {
            adultCountTextView.setText("");
            progressBarAdult.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}
