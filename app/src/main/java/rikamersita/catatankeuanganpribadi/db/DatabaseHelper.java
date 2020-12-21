package rikamersita.catatankeuanganpribadi.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DatabaseHelper extends SQLiteOpenHelper implements BaseColumns {

    public static String DATABASE_NAME = "dbnoteapp";
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
}
