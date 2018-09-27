package training.ruseff.com.checkintraining.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    public static String dateToString(Date date) {
        SimpleDateFormat simpleDate = new SimpleDateFormat("dd-MM-yyyy");
        return simpleDate.format(date);
    }

    public static boolean dateBeforeToday(int day, int month, int year) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        Date today = c.getTime();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        Date choosenDate = c.getTime();
        return choosenDate.before(today);
    }

    public static String translateMonth(int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, i, 1);
        SimpleDateFormat format = new SimpleDateFormat("MMMM", new Locale("bg", ""));
        return format.format(calendar.getTime());
    }

    public static String[] getMonthStrings() {
        return new String[]{"Януари", "Февруари", "Март", "Април", "Май", "Юни", "Юли", "Август", "Септември", "Октомври", "Ноември", "Декември"};
    }
}
