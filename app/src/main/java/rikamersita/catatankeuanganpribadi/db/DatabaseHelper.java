package rikamersita.catatankeuanganpribadi.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.content.ContentValues;
import android.database.Cursor;

import rikamersita.catatankeuanganpribadi.login.DBHelper;

public class DatabaseHelper extends SQLiteOpenHelper implements BaseColumns {

    public static String DATABASE_NAME = "db_catatan";
    public static String TABLE_NAME = "note";
    public static String FIELD_TITLE = "title";
    public static String FIELD_TIPE = "tipe";
    public static String FIELD_TIPESPN = "tipespn";
    public static String FIELD_KATEGORI = "kategori";
    public static String FIELD_KATEGORISPN = "kategorispn";
    public static String FIELD_DESCRIPTION = "description";
    public static String FIELD_DATE = "date";
    public static String FIELD_ID = "_id";

    private static final int DATABASE_VERSION = 1;
    private static String CREATE_TABLE_NOTE = "create table " + TABLE_NAME + " (" +
            FIELD_ID + " integer primary key autoincrement, " +
            FIELD_TIPE +" text not null, " +
            FIELD_TIPESPN +" text not null, " +
            FIELD_KATEGORI + " text not null, " +
            FIELD_KATEGORISPN + " text not null, " +
            FIELD_DATE + " text not null, "+
            FIELD_TITLE + " text not null, " +
            FIELD_DESCRIPTION + " text not null);";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_NOTE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    public static class DBHelper extends SQLiteOpenHelper {

        public static final String TAG = rikamersita.catatankeuanganpribadi.login.DBHelper.class.getSimpleName();
        public static final String DATABASE_NAME = "db_catatan";

        public static final String USER_TABLE = "users";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_CNFRMPASS = "cnfrmpassword";

        public  SQLiteDatabase db ;

        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, 1);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(" CREATE TABLE " + USER_TABLE +
                    "(" + COLUMN_NAME + " TEXT, " + COLUMN_USERNAME + " TEXT, " +
                    COLUMN_PASSWORD + " TEXT, " + COLUMN_CNFRMPASS + " TEXT " +")");    }


        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
            onCreate(db);
        }

        /* Storing User details*/

        public void addUser(String name, String username, String password, String cnfrmpassword) {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, name);
            values.put(COLUMN_USERNAME, username);
            values.put(COLUMN_PASSWORD, password);
            values.put(COLUMN_CNFRMPASS, cnfrmpassword);

            db.insert(USER_TABLE, null, values);
            db.close();
        }

        public Cursor getData(){
            SQLiteDatabase db = this.getWritableDatabase();
            return db.rawQuery("SELECT * FROM " + USER_TABLE, null);
        }
    }
}
