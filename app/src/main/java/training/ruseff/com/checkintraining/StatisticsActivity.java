package training.ruseff.com.checkintraining;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Calendar;

public class StatisticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        TextView monthAverageTextView = findViewById(R.id.monthAverage);
        TextView monthMostVisitingTextView = findViewById(R.id.monthMostVisiting);
        TextView weekAverageTextView = findViewById(R.id.weekAverage);
        TextView weekMostVisitingTextView = findViewById(R.id.weekMostVisiting);

        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

//        int monthAverage = DBCrudHelper.getMonthAverage(month, year);
//        monthAverageTextView.setText(String.valueOf(monthAverage));
//
//        String monthMostVisiting = DBCrudHelper.getMonthMostVisitingUser(month, year);
//        monthMostVisitingTextView.setText(monthMostVisiting);

//        int monthMostVisitedDay = DBCrudHelper.getMonthMostVisitedDay(month, year);

    }
}
