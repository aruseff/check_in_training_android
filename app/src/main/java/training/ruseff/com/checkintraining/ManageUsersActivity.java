package training.ruseff.com.checkintraining;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.Calendar;

import training.ruseff.com.checkintraining.adapters.UserListAdapter;
import training.ruseff.com.checkintraining.db.DBUserHelper;
import training.ruseff.com.checkintraining.models.User;

public class ManageUsersActivity extends AppCompatActivity {

    SearchView searchView;
    RelativeLayout mainLayout;
    SwipeMenuListView activeUsersListView;
    SwipeMenuListView inactiveUsersListView;
    TextView countTextView;
    EditText usernameEditText;
    Button addUserButton;
    Spinner spinner;
    LinearLayout addUserLayout;

    boolean mustReloadActiveUsers = true;
    boolean mustReloadInactiveUsers = true;
    ArrayList<User> activeUsers;
    ArrayAdapter<User> activeUsersAdapter;
    ArrayList<User> inactiveUsers;
    ArrayAdapter<User> inactiveUsersAdapter;

    SwipeMenuCreator activeSlider;
    SwipeMenuCreator inactiveSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        initFields();
        loadActiveUsers();

        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString();
                if (!"".equals(username)) {
                    new AddUserAsyncTask().execute(new User(username, false, true, Calendar.getInstance().get(Calendar.MONTH)));
                }
            }
        });
    }

    private void loadActiveUsers() {
        if (activeUsers == null || mustReloadActiveUsers) {
            new GetUsersAsyncTask().execute(true);
        } else {
            showActiveUsers();
        }
    }

    private void loadInactiveUsers() {
        if (inactiveUsers == null || mustReloadInactiveUsers) {
            new GetUsersAsyncTask().execute(false);
        } else {
            showInactiveUsers();
        }
    }

    private void showActiveUsers() {
        inactiveUsersListView.setVisibility(View.GONE);
        activeUsersListView.setVisibility(View.VISIBLE);
        addUserLayout.setVisibility(View.VISIBLE);
        countTextView.setText(String.valueOf(activeUsers.size()));
    }

    private void showInactiveUsers() {
        addUserLayout.setVisibility(View.GONE);
        activeUsersListView.setVisibility(View.GONE);
        inactiveUsersListView.setVisibility(View.VISIBLE);
        countTextView.setText(String.valueOf(inactiveUsers.size()));
    }

    private void initFields() {
        searchView = findViewById(R.id.searchView);
        mainLayout = findViewById(R.id.layoutId);
        activeUsersListView = findViewById(R.id.manageUsersListView);
        inactiveUsersListView = findViewById(R.id.inactiveUsersListView);
        countTextView = findViewById(R.id.usersCount);
        usernameEditText = findViewById(R.id.usernameEditText);
        addUserButton = findViewById(R.id.addUserButton);
        spinner = findViewById(R.id.spinner);
        addUserLayout = findViewById(R.id.addUserLayout);

        activeSlider = createSlider(android.R.drawable.ic_delete);
        inactiveSlider = createSlider(android.R.drawable.ic_input_add);

        createTrainingSpinner();
        createSearchView();
    }

    private void createSearchView() {
        int editTextId = searchView.getContext()
                .getResources()
                .getIdentifier("android:id/search_src_text", null, null);
        EditText searchEditText = searchView.findViewById(editTextId);
        searchEditText.setTextColor(Color.parseColor("#FFFFFF"));
        searchEditText.setHintTextColor(Color.parseColor("#FFFFFF"));

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    spinner.setVisibility(View.GONE);
                    addUserLayout.setVisibility(View.GONE);
                } else {
                    spinner.setVisibility(View.VISIBLE);
                    addUserLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (activeUsersListView.getVisibility() == View.GONE) {
                    inactiveUsersAdapter.getFilter().filter(s);
                } else {
                    activeUsersAdapter.getFilter().filter(s);
                }
                return false;
            }
        });
    }

    private void createTrainingSpinner() {
        String[] spinnerOptions = {"Всички трениращи", "Неактивни"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.users_spinner_item_selected, spinnerOptions);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    loadActiveUsers();
                } else {
                    loadInactiveUsers();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private SwipeMenuCreator createSlider(final int iconId) {
        return new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {

                SwipeMenuItem userInfo = new SwipeMenuItem(getApplicationContext());
                userInfo.setBackground(new ColorDrawable(Color.rgb(242, 242, 242)));
                userInfo.setWidth(200);
                userInfo.setIcon(R.drawable.user_info);
                menu.addMenuItem(userInfo);

                SwipeMenuItem changeUserState = new SwipeMenuItem(getApplicationContext());
                changeUserState.setBackground(new ColorDrawable(Color.rgb(242, 242, 242)));
                changeUserState.setWidth(200);
                changeUserState.setIcon(iconId);
                menu.addMenuItem(changeUserState);
            }
        };
    }

    private void createDeleteConfirmDialog(final User user, final boolean activeUsersFlag, final ArrayAdapter<User> userListAdapter) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Избери опция");
        builder.setMessage("Потребител: " + user.getName());
        builder.setPositiveButton(activeUsersFlag ? "Деактивирай" : "Активирай", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (activeUsersFlag) {
                    new ChangeUserStateAsyncTask().execute(user.getId(), 0);
                    mustReloadInactiveUsers = true;
                } else {
                    new ChangeUserStateAsyncTask().execute(user.getId(), 1);
                    mustReloadActiveUsers = true;
                }
                userListAdapter.remove(user);
                countTextView.setText(String.valueOf(Integer.parseInt(countTextView.getText().toString()) - 1));
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("Изтрий", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new DeleteUserAsyncTask().execute(user.getId());
                userListAdapter.remove(user);
                countTextView.setText(String.valueOf(Integer.parseInt(countTextView.getText().toString()) - 1));
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private class AddUserAsyncTask extends AsyncTask<User, Void, User> {

        @Override
        protected User doInBackground(User... param) {
            return DBUserHelper.addUser(param[0]);
        }

        @Override
        protected void onPostExecute(User result) {
            if (result == null) {
                Snackbar.make(mainLayout, "Възникна грешка! Моля опитайте отново", Snackbar.LENGTH_LONG).show();
            } else {
                activeUsersAdapter.add(result);
                usernameEditText.setText("");
                countTextView.setText(String.valueOf(Integer.parseInt(countTextView.getText().toString()) + 1));
            }
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private class GetUsersAsyncTask extends AsyncTask<Boolean, Void, ArrayList<User>> {

        private boolean activeUsersFlag;

        @Override
        protected ArrayList<User> doInBackground(Boolean... param) {
            activeUsersFlag = param[0];
            if (activeUsersFlag) {
                return DBUserHelper.getAllActiveUsers();
            } else {
                return DBUserHelper.getAllInactiveUsers();
            }
        }

        @Override
        protected void onPostExecute(ArrayList<User> result) {
            if (activeUsersFlag) {
                activeUsers = result;
                activeUsersAdapter = new UserListAdapter(activeUsers, ManageUsersActivity.this, "#FFFFFF");
                fillListView(activeUsersAdapter, activeUsersListView, activeSlider);
                showActiveUsers();
            } else {
                inactiveUsers = result;
                inactiveUsersAdapter = new UserListAdapter(inactiveUsers, ManageUsersActivity.this, "#FFFFFF");
                fillListView(inactiveUsersAdapter, inactiveUsersListView, inactiveSlider);
                showInactiveUsers();
            }
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

        private void fillListView(final ArrayAdapter<User> userListAdapter, SwipeMenuListView listView, SwipeMenuCreator slider) {
            listView.setAdapter(userListAdapter);
            listView.setMenuCreator(slider);
            listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
            if (activeUsersFlag) {
                mustReloadActiveUsers = false;
            } else {
                mustReloadInactiveUsers = false;
            }
            listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                    User user = userListAdapter.getItem(position);
                    if (user != null) {
                        if (index == 0) {
                            Intent userDetailsIntent = new Intent(ManageUsersActivity.this, UserDetailsActivity.class);
                            userDetailsIntent.putExtra("userId", user.getId());
                            ManageUsersActivity.this.startActivity(userDetailsIntent);
                        } else if (index == 1) {
                            createDeleteConfirmDialog(user, activeUsersFlag, userListAdapter);
                        }
                    }
                    return false;
                }
            });
        }
    }

    private class ChangeUserStateAsyncTask extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... param) {
            int id = param[0];
            if (param[1] == 1) {
                DBUserHelper.activateUserById(id);
            } else {
                DBUserHelper.deactivateUserById(id);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private class DeleteUserAsyncTask extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... param) {
            int id = param[0];
            DBUserHelper.deleteUserById(id);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}
