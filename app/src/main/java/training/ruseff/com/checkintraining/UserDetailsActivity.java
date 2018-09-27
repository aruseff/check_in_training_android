package training.ruseff.com.checkintraining;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import training.ruseff.com.checkintraining.app.MyApplication;
import training.ruseff.com.checkintraining.db.DBDataHelper;
import training.ruseff.com.checkintraining.db.DBUserHelper;
import training.ruseff.com.checkintraining.models.User;
import training.ruseff.com.checkintraining.utils.DateUtils;
import training.ruseff.com.checkintraining.utils.ExamMessages;

public class UserDetailsActivity extends AppCompatActivity {

    TextView nameTextView;
    TextView statusTextView;
    TextView trainingsTextView;
    Spinner lastPaymentSpinner;
    TextView examTextView;
    Button saveButton;
    LinearLayout paymentLayout;
    User user;
    private boolean toUpdateUser = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        final int userId = getIntent().getIntExtra("userId", -1);

        if (userId != -1) {
            user = DBUserHelper.getUserById(userId);
            if (user != null) {
                int trainingsCount = DBDataHelper.getCountByUserIdAndMonth(userId, Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.YEAR));
                int trainingsCountLast3Months = DBDataHelper.getCountByUserIdForLastThreeMonths(userId);
                initFields();
                nameTextView.setText(user.getName());
                statusTextView.setText(user.isActive() ? "Активен" : "Неактивен");
                statusTextView.setTextColor(user.isActive() ? Color.GREEN : Color.RED);
                trainingsTextView.setText(String.valueOf(trainingsCount));
                createMonthSpinner();
                examTextView.setText(ExamMessages.getExamMessage(trainingsCountLast3Months, user.getLastPayment()));
                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (toUpdateUser) {
                            DBUserHelper.updateLastPaymentDate(userId, lastPaymentSpinner.getSelectedItemPosition());
                        }
                        finish();
                    }
                });
            } else {
                finish();
            }
        } else {
            finish();
        }
    }

    private void createMonthSpinner() {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.spinner_item_selected, DateUtils.getMonthStrings());
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);
        lastPaymentSpinner.setAdapter(dataAdapter);
        lastPaymentSpinner.setSelection(user.getLastPayment());
        lastPaymentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                toUpdateUser = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initFields() {
        nameTextView = findViewById(R.id.nameTextView);
        statusTextView = findViewById(R.id.statusTextView);
        trainingsTextView = findViewById(R.id.trainingsTextView);
        paymentLayout = findViewById(R.id.paymentLayout);
        lastPaymentSpinner = findViewById(R.id.lastPaymentTextView);
        examTextView = findViewById(R.id.examTextView);
        saveButton = findViewById(R.id.saveButton);
    }
}
