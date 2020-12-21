package rikamersita.catatankeuanganpribadi.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import rikamersita.catatankeuanganpribadi.Note;

import static android.provider.BaseColumns._ID;
import static rikamersita.catatankeuanganpribadi.db.DatabaseHelper.FIELD_DATE;
import static rikamersita.catatankeuanganpribadi.db.DatabaseHelper.FIELD_DESCRIPTION;
import static rikamersita.catatankeuanganpribadi.db.DatabaseHelper.FIELD_ID;
import static rikamersita.catatankeuanganpribadi.db.DatabaseHelper.FIELD_KATEGORI;
import static rikamersita.catatankeuanganpribadi.db.DatabaseHelper.FIELD_KATEGORISPN;
import static rikamersita.catatankeuanganpribadi.db.DatabaseHelper.FIELD_TIPE;
import static rikamersita.catatankeuanganpribadi.db.DatabaseHelper.FIELD_TIPESPN;
import static rikamersita.catatankeuanganpribadi.db.DatabaseHelper.FIELD_TITLE;

public class NoteHelper {
    private static String DATABASE_TABLE = DatabaseHelper.TABLE_NAME;
    private Context context;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    public NoteHelper(Context context) {
        this.context = context;
    }

    public NoteHelper open() throws SQLException {
        databaseHelper = new DatabaseHelper(context);
        database = databaseHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        databaseHelper.close();
    }


    public Cursor searchQuery(String title) {
        return database.rawQuery("SELECT * FROM " + DATABASE_TABLE + " WHERE "
                + FIELD_TITLE + " LIKE '%" + title + "%'" + " OR "
                + FIELD_TIPE + " LIKE '%" + title + "%'" + " OR "
                + FIELD_TIPESPN + " LIKE '%" + title + "%'" + " OR "
                + FIELD_KATEGORI + " LIKE '%" + title + "%'" + " OR "
                + FIELD_KATEGORISPN + " LIKE '%" + title + "%'" + " OR "
                + FIELD_DESCRIPTION + " LIKE '%" + title + "%'" + " OR "
                + FIELD_DATE + " LIKE '%" + title + "%'" + " OR "
                + FIELD_ID + " LIKE '%" + title + "%'", null);
    }

    public ArrayList<Note> getSearchResult(String keyword) {
        ArrayList<Note> arrayList = new ArrayList<Note>();
        Cursor cursor = searchQuery(keyword);
        cursor.moveToFirst();
        Note note;

        if (cursor.getCount() > 0) {
            do {
                note = new Note();

                note.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_ID)));
                note.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(FIELD_TITLE)));
                note.setTipe(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_TIPE)));
                note.setSpntipe(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_TIPESPN)));
                note.setKategori(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_KATEGORI)));
                note.setSpnkategori(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_KATEGORISPN)));
                note.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(FIELD_DESCRIPTION)));
                note.setDate(cursor.getString(cursor.getColumnIndexOrThrow(FIELD_DATE)));

                arrayList.add(note);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;

    }

    public Cursor searchWeeklyReport (String kalender) {
        return database.rawQuery("SELECT * FROM " + DATABASE_TABLE + " WHERE "
                + FIELD_DATE + " LIKE '%" + kalender + "%'"
                , null);
    }
    public ArrayList<Note> getDataReport (String keyword) {
        ArrayList<Note> arrayList = new ArrayList<Note>();
        Cursor cursor = searchWeeklyReport(keyword);
        cursor.moveToFirst();
        Note note;

        if (cursor.getCount() > 0) {
            do {
                note = new Note();

                note.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_ID)));
                note.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(FIELD_TITLE)));
                note.setTipe(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_TIPE)));
                note.setSpntipe(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_TIPESPN)));
                note.setKategori(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_KATEGORI)));
                note.setSpnkategori(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_KATEGORISPN)));
                note.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(FIELD_DESCRIPTION)));
                note.setDate(cursor.getString(cursor.getColumnIndexOrThrow(FIELD_DATE)));

                arrayList.add(note);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public String getData(String kata) {
        String result = "";
        Cursor cursor = searchQuery(kata);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            result = cursor.getString(2);
            for (; !cursor.isAfterLast(); cursor.moveToNext()) {
                result = cursor.getString(2);
            }
        }
        cursor.close();
        return result;
    }



    public Cursor queryAllData() {
        return database.rawQuery("SELECT * FROM " + DATABASE_TABLE + " ORDER BY " + _ID + " DESC", null);
    }

    public ArrayList<Note> getAllData() {
        ArrayList<Note> arrayList = new ArrayList<Note>();
        Cursor cursor = queryAllData();
        cursor.moveToFirst();
        Note note;

        if (cursor.getCount() > 0) {
            do {
                note = new Note();

                note.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_ID)));
                note.setTipe(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_TIPE)));
                note.setSpntipe(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_TIPESPN)));
                note.setKategori(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_KATEGORI)));
                note.setSpnkategori(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_KATEGORISPN)));
                note.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(FIELD_TITLE)));
                note.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(FIELD_DESCRIPTION)));
                note.setDate(cursor.getString(cursor.getColumnIndexOrThrow(FIELD_DATE)));

                arrayList.add(note);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public long insert(Note note) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(FIELD_TITLE, note.getTitle());
        initialValues.put(FIELD_TIPE, note.getTipe());
        initialValues.put(FIELD_TIPESPN, note.getSpntipe());
        initialValues.put(FIELD_KATEGORI, note.getKategori());
        initialValues.put(FIELD_KATEGORISPN, note.getSpnkategori());
        initialValues.put(FIELD_DESCRIPTION, note.getDescription());
        initialValues.put(FIELD_DATE, note.getDate());

        return database.insert(DATABASE_TABLE, null, initialValues);
    }

    public void update(Note note) {
        ContentValues args = new ContentValues();
        args.put(FIELD_TITLE, note.getTitle());
        args.put(FIELD_TIPE, note.getTipe());
        args.put(FIELD_TIPESPN, note.getSpntipe());
        args.put(FIELD_KATEGORI, note.getKategori());
        args.put(FIELD_KATEGORISPN, note.getSpnkategori());
        args.put(FIELD_DESCRIPTION, note.getDescription());
        args.put(FIELD_DATE, note.getDate());
        database.update(DATABASE_TABLE, args, DatabaseHelper.FIELD_ID + "= '" + note.getId() + "'", null);
    }

    public void delete(int id) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.FIELD_ID + " = '" + id + "'", null);
    }

}
