package training.ruseff.com.checkintraining.db;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Calendar;

public class DBDataHelper {

    public static ArrayList<Integer> getUserIDsByDateAndType(int day, int month, int year, String type) {
        String sql = "SELECT FK_USER_ID FROM DATA WHERE DAY=? AND MONTH=? AND YEAR=? AND TYPE=?";
        Cursor cursor = TrainingSQLiteHelper.getInstance().getReadableDatabase().rawQuery(sql, new String[]{String.valueOf(day), String.valueOf(month), String.valueOf(year), type});
        return convertCursorToListOfInts(cursor);
    }

    public static int getCountByDateAndType(int day, int month, int year, String type) {
        String sql = "SELECT COUNT(FK_USER_ID) FROM DATA WHERE DAY=? AND MONTH=? AND YEAR=? AND TYPE=?";
        Cursor cursor = TrainingSQLiteHelper.getInstance().getReadableDatabase().rawQuery(sql, new String[]{String.valueOf(day), String.valueOf(month), String.valueOf(year), type});
        if (cursor.getCount() == 0) {
            return 0;
        }
        cursor.moveToFirst();
        int result = cursor.getInt(0);
        cursor.close();
        return result;
    }

    public static boolean addData(int day, int month, int year, int userId, String type) {
        if (!ifExist(day, month, year, userId)) {
            try {
                ContentValues values = new ContentValues();
                values.put("DAY", day);
                values.put("MONTH", month);
                values.put("YEAR", year);
                values.put("FK_USER_ID", userId);
                values.put("TYPE", type);
                TrainingSQLiteHelper.getInstance().getWritableDatabase().insertOrThrow("DATA", null, values);
                return true;
            } catch (android.database.SQLException e) {
                return false;
            }
        }
        return true;
    }

    public static boolean deleteDataIfExist(int day, int month, int year, int userId, String type) {
        if (ifExist(day, month, year, userId)) {
            return TrainingSQLiteHelper.getInstance().getWritableDatabase().delete("DATA", "DAY=? AND MONTH=? AND YEAR=? AND FK_USER_ID=? AND TYPE=?", new String[]{String.valueOf(day), String.valueOf(month), String.valueOf(year), String.valueOf(userId), type}) > 0;
        }
        return true;
    }

    private static boolean ifExist(int day, int month, int year, int userId) {
        String sql = "SELECT 1 FROM DATA WHERE DAY=? AND MONTH=? AND YEAR=? AND FK_USER_ID=?";
        Cursor cursor = TrainingSQLiteHelper.getInstance().getReadableDatabase().rawQuery(sql, new String[]{String.valueOf(day), String.valueOf(month), String.valueOf(year), String.valueOf(userId)});
        if (cursor.getCount() == 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public static int getCountByUserIdAndMonth(int userId, int month, int year) {
        String sql = "SELECT COUNT(FK_USER_ID) FROM DATA WHERE FK_USER_ID=? AND MONTH=? AND YEAR=?";
        Cursor cursor = TrainingSQLiteHelper.getInstance().getReadableDatabase().rawQuery(sql, new String[]{String.valueOf(userId), String.valueOf(month), String.valueOf(year)});
        if (cursor.getCount() == 0) {
            cursor.close();
            return 0;
        }
        cursor.moveToFirst();
        int result = cursor.getInt(0);
        cursor.close();
        return result;
    }

    public static int getCountByUserIdForLastThreeMonths(int userId) {
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        Cursor cursor = null;
        String sql;
        if (currentMonth > 1) {
            sql = "SELECT COUNT(FK_USER_ID) FROM DATA WHERE MONTH IN (?, ?, ?) AND FK_USER_ID=? AND YEAR=?";
            cursor = TrainingSQLiteHelper.getInstance().getReadableDatabase().rawQuery(sql, new String[]{String.valueOf(currentMonth), String.valueOf(currentMonth - 1),
                    String.valueOf(currentMonth - 2), String.valueOf(userId), String.valueOf(currentYear)});
        } else {
            sql = "SELECT COUNT(FK_USER_ID) FROM DATA WHERE MONTH IN (?, ?, ?) AND (YEAR=? Or YEAR=?) AND FK_USER_ID=?";
            if (currentMonth == 1) {
                cursor = TrainingSQLiteHelper.getInstance().getReadableDatabase().rawQuery(sql, new String[]{String.valueOf(currentMonth), String.valueOf(currentMonth - 1),
                        String.valueOf(11), String.valueOf(currentYear), String.valueOf(currentYear - 1), String.valueOf(userId)});
            } else if (currentMonth == 0) {
                cursor = TrainingSQLiteHelper.getInstance().getReadableDatabase().rawQuery(sql, new String[]{String.valueOf(currentMonth), String.valueOf(11),
                        String.valueOf(10), String.valueOf(currentYear), String.valueOf(currentYear - 1), String.valueOf(userId)});
            }
        }
        if (cursor == null) {
            return -1;
        } else {
            if (cursor.getCount() == 0) {
                cursor.close();
                return 0;
            }
            cursor.moveToFirst();
            int result = cursor.getInt(0);
            cursor.close();
            return result;
        }
    }

    private static ArrayList<Integer> convertCursorToListOfInts(Cursor cursor) {
        ArrayList<Integer> ids = new ArrayList<>();
        if (cursor.getCount() == 0) {
            return ids;
        }
        cursor.moveToFirst();
        do {
            int id = cursor.getInt(0);
            ids.add(id);
        } while (cursor.moveToNext());
        cursor.close();
        return ids;
    }
}
