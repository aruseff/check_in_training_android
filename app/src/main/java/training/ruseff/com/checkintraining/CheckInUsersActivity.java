package training.ruseff.com.checkintraining;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import training.ruseff.com.checkintraining.adapters.UserListAdapter;
import training.ruseff.com.checkintraining.db.DBDataHelper;
import training.ruseff.com.checkintraining.db.DBUserHelper;
import training.ruseff.com.checkintraining.models.User;
import training.ruseff.com.checkintraining.utils.DateUtils;
import training.ruseff.com.checkintraining.utils.Types;

public class CheckInUsersActivity extends AppCompatActivity {

    TextView alertTextView;
    ListView listView;
    TextView noUsersTextView;
    TextView doneTextView;
    String defaultBackgroundTextViewColor = "#FFFFFF";
    String activeBackgroundTextViewColor = "#920291FF";

    int day;
    int month;
    int year;
    String type;
    ArrayList<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in_users);

        getIntentParams();
        initFields();
        new GetUsersAsyncTask().execute();

        doneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (User user : users) {
                    if (user.isChecked()) {
                        // TODO
                        DBDataHelper.addData(day, month, year, user.getId(), type);
                    } else {
                        // TODO
                        DBDataHelper.deleteDataIfExist(day, month, year, user.getId(), type);
                    }
                }
                CheckInUsersActivity.this.finish();
            }
        });
    }

    private void initFields() {
        alertTextView = findViewById(R.id.alertTextView);
        noUsersTextView = findViewById(R.id.noUsersTextView);
        listView = findViewById(R.id.usersListView);
        doneTextView = findViewById(R.id.doneButton);

        if (type.equals(Types.CHILD)) {
            activeBackgroundTextViewColor = "#923DFF02";
        }

        if (DateUtils.dateBeforeToday(day, month, year)) {
            alertTextView.setVisibility(View.VISIBLE);
        }
    }

    private void fillUsersListView() {
        if (users.isEmpty()) {
            noUsersTextView.setVisibility(View.VISIBLE);
        } else {
            ArrayAdapter<User> adapter = new UserListAdapter(users, this, activeBackgroundTextViewColor);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    if (users.get(position).isChecked()) {
                        view.setBackgroundColor(Color.parseColor(defaultBackgroundTextViewColor));
                        TextView textView = (TextView) parent.getChildAt(position);
                        textView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
                        users.get(position).setChecked(false);
                    } else {
                        view.setBackgroundColor(Color.parseColor(activeBackgroundTextViewColor));
                        TextView textView = (TextView) parent.getChildAt(position);
                        textView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.check, 0);
                        users.get(position).setChecked(true);
                    }
                }
            });
        }
    }

    public void getIntentParams() {
        Intent intent = getIntent();
        day = intent.getIntExtra("day", -1);
        month = intent.getIntExtra("month", -1);
        year = intent.getIntExtra("year", -1);
        type = intent.getStringExtra("type");
    }

    private class GetUsersAsyncTask extends AsyncTask<Void, Void, ArrayList<User>> {

        @Override
        protected ArrayList<User> doInBackground(Void... param) {
            ArrayList<User> allUsers = DBUserHelper.getAllActiveUsers();
            ArrayList<Integer> idsToCheck = DBDataHelper.getUserIDsByDateAndType(day, month, year, type);
            ArrayList<Integer> idsToRemove = DBDataHelper.getUserIDsByDateAndType(day, month, year, (type.equals(Types.CHILD) ? Types.ADULT : Types.CHILD));
            ArrayList<User> returnUsers = new ArrayList<>();
            for (User user : allUsers) {
                if (!idsToRemove.contains(user.getId())) {
                    returnUsers.add(user);
                }
            }
            for (User user : returnUsers) {
                if (idsToCheck.contains(user.getId())) {
                    user.setChecked(true);
                }
            }
            Collections.sort(returnUsers, new Comparator<User>() {
                @Override
                public int compare(User user1, User user2) {
                    if (user1.isChecked() == user2.isChecked()) {
                        return user1.getName().compareTo(user2.getName());
                    } else {
                        return user1.isChecked() ? -1 : 1;
                    }
                }
            });
            return returnUsers;
        }

        @Override
        protected void onPostExecute(ArrayList<User> result) {
            users = result;
            fillUsersListView();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

}
