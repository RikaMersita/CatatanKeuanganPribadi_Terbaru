package rikamersita.catatankeuanganpribadi;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

import rikamersita.catatankeuanganpribadi.adapter.NoteAdapter;
import rikamersita.catatankeuanganpribadi.db.NoteHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private RecyclerView rvNotes;
    private ProgressBar progressBar;
    private FloatingActionButton fabAdd;

    private LinkedList<Note> list;
    private NoteAdapter noteAdapter;
    private NoteHelper noteHelper;

    private Spinner spnFilter;
    String filter[] = {"Semua Data", "Satu Minggu Ini", "Satu Bulan Ini"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Catatan Keuangan Pribadi");

        rvNotes = findViewById(R.id.rv_notes);
        progressBar = findViewById(R.id.progressbar);
        fabAdd = findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(this);

        rvNotes.setLayoutManager(new LinearLayoutManager(this));
        rvNotes.setHasFixedSize(true);

        noteHelper = new NoteHelper(this);
        noteHelper.open();
        list = new LinkedList<>();

        noteAdapter = new NoteAdapter(this);
        noteAdapter.setListNotes(list);
        rvNotes.setAdapter(noteAdapter);

        spnFilter = (Spinner) findViewById(R.id.spn_Filter);
        ArrayAdapter<String> filterAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, filter);
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnFilter.setAdapter(filterAdapter);
        spnFilter.setOnItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        spnFilter.setSelection(0);

        new LoadNoteAsync().execute();
    }

    public static String getCalculatedDate(String dateFormat, int days) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat(dateFormat);
        cal.add(Calendar.DAY_OF_YEAR, days);
        return s.format(new Date(cal.getTimeInMillis()));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab_add) {
            Intent intent = new Intent(this, FormAddUpdateActivity.class);
            startActivityForResult(intent, FormAddUpdateActivity.REQUEST_ADD);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (spnFilter.getSelectedItemPosition() == 0){
            list.clear();
            list.addAll(noteHelper.getAllData());
            noteAdapter.setListNotes(list);
            rvNotes.setAdapter(noteAdapter);
        }
        else if (spnFilter.getSelectedItemPosition() == 1){
            list.clear();
            for (int days = 0; days < 7; days++){
                String date =  getCalculatedDate("yyyy/MM/dd", -days);
                list.addAll(noteHelper.getDataReport(date));
                noteAdapter.setListNotes(list);
            }
            rvNotes.setAdapter(noteAdapter);
        }
        else if (spnFilter.getSelectedItemPosition() == 2){
            list.clear();
            for (int days = 0; days < 30; days++){
                String date =  getCalculatedDate("yyyy/MM/dd", -days);
                list.addAll(noteHelper.getDataReport(date));
                noteAdapter.setListNotes(list);
            }
            rvNotes.setAdapter(noteAdapter);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private class LoadNoteAsync extends AsyncTask<Void, Void, ArrayList<Note>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);

            if (list.size() > 0) {
                list.clear();
            }
        }

        @Override
        protected ArrayList<Note> doInBackground(Void... voids) {
            return noteHelper.getAllData();
        }

        @Override
        protected void onPostExecute(ArrayList<Note> notes) {
            super.onPostExecute(notes);
            progressBar.setVisibility(View.GONE);

            list.addAll(notes);
            noteAdapter.setListNotes(list);
            noteAdapter.notifyDataSetChanged();

            if (list.size() == 0) {
                showSnackbarMessage("Tidak ada data saat ini");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FormAddUpdateActivity.REQUEST_ADD) {
            if (resultCode == FormAddUpdateActivity.RESULT_ADD) {
                new LoadNoteAsync().execute();
                showSnackbarMessage("berhasil");
                rvNotes.getLayoutManager().smoothScrollToPosition(rvNotes, new RecyclerView.State(), 0);
            }
        }

        if (requestCode == FormAddUpdateActivity.REQUEST_UPDATE) {
            if (resultCode == FormAddUpdateActivity.RESULT_ADD) {
                new LoadNoteAsync().execute();
                int position = data.getIntExtra(FormAddUpdateActivity.EXTRA_POSITION, 0);
                rvNotes.getLayoutManager().smoothScrollToPosition(rvNotes, new RecyclerView.State(), position);
                showSnackbarMessage("Satu item berhasil diubah");
            }
        }

        if (requestCode == FormAddUpdateActivity.RESULT_DELETE) {
            int position = data.getIntExtra(FormAddUpdateActivity.EXTRA_POSITION, 0);
            list.remove(position);
            noteAdapter.setListNotes(list);
            noteAdapter.notifyDataSetChanged();
            showSnackbarMessage("Salah satu item berhasil dihapus");
        }
    }

    private void showSnackbarMessage(String messege) {
        Snackbar.make(rvNotes, messege, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (noteHelper != null) {
            noteHelper.close();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        list = new LinkedList<>();
        getMenuInflater().inflate(R.menu.menu_search, menu);

        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView search = (SearchView) menu.findItem(R.id.menuSearch).getActionView();


        search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                list.clear();
                list.addAll(noteHelper.getSearchResult(query));
                noteAdapter.setListNotes(list);
                rvNotes.setAdapter(noteAdapter);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                list.clear();
                list.addAll(noteHelper.getSearchResult(newText));
                noteAdapter.setListNotes(list);
                rvNotes.setAdapter(noteAdapter);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

}
