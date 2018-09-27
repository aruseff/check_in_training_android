package training.ruseff.com.checkintraining.db;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Calendar;

import training.ruseff.com.checkintraining.models.User;

public class DBUserHelper {

    public static User addUser(User user) {
        try {
            ContentValues values = new ContentValues();
            values.put("NAME", user.getName());
            values.put("IS_ACTIVE", user.isActive());
            values.put("LAST_PAYMENT", user.getLastPayment());
            int id = (int) TrainingSQLiteHelper.getInstance().getWritableDatabase().insertOrThrow("USER", null, values);
            user.setId(id);
            user.setChecked(user.isChecked());
            user.setActive(user.isActive());
            return user;
        } catch (android.database.SQLException e) {
            return null;
        }
    }

    public static User getUserById(int id) {
        String sql = "SELECT * FROM USER WHERE ID=?";
        Cursor cursor = TrainingSQLiteHelper.getInstance().getReadableDatabase().rawQuery(sql, new String[]{String.valueOf(id)});
        if (cursor.getCount() == 0) {
            cursor.close();
            return null;
        }
        cursor.moveToFirst();
        String name = cursor.getString(1);
        boolean isActive = cursor.getInt(2) == 1;
        int lastPayment = cursor.getInt(3);
        cursor.close();
        return new User(id, name, false, isActive, lastPayment);
    }

    public static void deleteUserById(int id) {
        TrainingSQLiteHelper.getInstance().getWritableDatabase().delete("USER", "ID=?", new String[]{String.valueOf(id)});
    }

    public static void deactivateUserById(int id) {
        ContentValues args = new ContentValues();
        args.put("IS_ACTIVE", false);
        TrainingSQLiteHelper.getInstance().getWritableDatabase().update("USER", args, "ID="+String.valueOf(id), null);
    }

    public static void activateUserById(int id) {
        ContentValues args = new ContentValues();
        args.put("IS_ACTIVE", true);
        TrainingSQLiteHelper.getInstance().getWritableDatabase().update("USER", args, "ID="+String.valueOf(id), null);
    }

    public static ArrayList<User> getAllActiveUsers() {
        String sql = "SELECT * FROM USER WHERE IS_ACTIVE=1 ORDER BY NAME ASC";
        Cursor cursor = TrainingSQLiteHelper.getInstance().getReadableDatabase().rawQuery(sql, new String[]{});
        return convertCursorToListOfUsers(cursor);
    }

    public static ArrayList<User> getAllInactiveUsers() {
        String sql = "SELECT * FROM USER WHERE IS_ACTIVE=0 ORDER BY NAME ASC";
        Cursor cursor = TrainingSQLiteHelper.getInstance().getReadableDatabase().rawQuery(sql, new String[]{});
        return convertCursorToListOfUsers(cursor);
    }

    public static void updateLastPaymentDate(int userId, int lastPaymentMonth) {
        ContentValues values = new ContentValues();
        values.put("LAST_PAYMENT", lastPaymentMonth);
        TrainingSQLiteHelper.getInstance().getWritableDatabase().update("USER", values, "id=?", new String[]{String.valueOf(userId)});
    }

    private static ArrayList<User> convertCursorToListOfUsers(Cursor cursor) {
        ArrayList<User> people = new ArrayList<>();
        if (cursor.getCount() == 0) {
            cursor.close();
            return people;
        }
        cursor.moveToFirst();
        do {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            boolean isActive = cursor.getInt(2)==1;
            int lastPayment = cursor.getInt(3);
            people.add(new User(id, name, false, isActive, lastPayment));
        } while (cursor.moveToNext());
        cursor.close();
        return people;
    }
}
