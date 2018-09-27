package training.ruseff.com.checkintraining.db;

import android.database.sqlite.SQLiteOpenHelper;

import android.database.sqlite.SQLiteDatabase;

import training.ruseff.com.checkintraining.app.MyApplication;

public class TrainingSQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_USER_TABLE_QUERY = "CREATE TABLE USER (" +
            "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "NAME TEXT," +
            "IS_ACTIVE INTEGER," +
            "LAST_PAYMENT INTEGER" +
            ")";

    private static final String CREATE_DATA_TABLE_QUERY = "CREATE TABLE DATA(" +
            "DAY INTEGER CHECK(DAY >= 1 AND DAY <= 31)," +
            "MONTH INTEGER CHECK(MONTH >= 1 AND MONTH <= 12)," +
            "YEAR INTEGER," +
            "WEEK INTEGER CHECK(WEEK >=1 AND WEEK <= 52)," +
            "TYPE TEXT," +
            "FK_USER_ID INTEGER," +
            "PRIMARY KEY(DAY, MONTH, YEAR, FK_USER_ID))";

    private static TrainingSQLiteHelper instance = null;

    private TrainingSQLiteHelper() {
        super(MyApplication.getAppContext(), "KARATE_CHECKIN_DB", null, DATABASE_VERSION);
    }

    public static TrainingSQLiteHelper getInstance() {
        if (instance == null) {
            instance = new TrainingSQLiteHelper();
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_USER_TABLE_QUERY);
        sqLiteDatabase.execSQL(CREATE_DATA_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS USER");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS DATA");
        onCreate(sqLiteDatabase);
    }
}
