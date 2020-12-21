package rikamersita.catatankeuanganpribadi;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import rikamersita.catatankeuanganpribadi.db.NoteHelper;

public class FormAddUpdateActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private EditText edtTitle, edtDescription;
    private Button btnSubmit;
    private Spinner spnTipe, spnKategori;

    String tipe[] = {"Pengeluaran", "Pemasukan"};
    String kategoriPengeluaran[] = {"Konsumsi", "Transportasi", "Listrik dan Air", "Lain-lain"};
    String kategoriPemasukan[] = {"Gaji", "Hasil Usaha", "Bonus", "Lain-lain"};



    public static String EXTRA_NOTE = "extra_note";
    public static String EXTRA_POSITION = "extra_position";
    private boolean isEdit = false;
    public static int REQUEST_ADD = 100;
    public static int RESULT_ADD = 101;
    public static int REQUEST_UPDATE = 200;
    public static int RESULT_UPDATE = 201;
    public static int RESULT_DELETE = 301;

    private final int ALERT_DIALOG_CLOSE = 10;
    private final int ALERT_DIALOG_DELETE = 20;

    private Note note;
    private int position;
    private NoteHelper noteHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_add_update);


        ArrayAdapter<String> stringTipeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tipe);
        stringTipeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        edtTitle = (EditText) findViewById(R.id.edt_title);
        edtDescription = (EditText) findViewById(R.id.edt_description);
        btnSubmit =(Button) findViewById(R.id.btn_submit);

        spnTipe = (Spinner) findViewById(R.id.spin_tipe);
        spnKategori = (Spinner) findViewById(R.id.spin_kategori);

        btnSubmit.setOnClickListener(this);

        noteHelper = new NoteHelper(this);
        noteHelper.open();

        note = getIntent().getParcelableExtra(EXTRA_NOTE);

        if (note != null){
            position = getIntent().getIntExtra(EXTRA_POSITION, 0);
            isEdit = true;
        }
        String actionBarTitle = null;
        String btnTitle = null;

        if (isEdit){
            actionBarTitle = "Ubah";
            btnTitle = "Update";
            edtTitle.setText(note.getTitle());
            edtDescription.setText(note.getDescription());

            spnTipe.setAdapter(stringTipeAdapter);
            spnTipe.setSelection(note.getTipe());

            if (spnTipe.getSelectedItem().toString().equals("Pengeluaran")){
                ArrayAdapter<String> stringKategoriPengeluaranAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, kategoriPengeluaran);
                stringKategoriPengeluaranAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnKategori.setAdapter(stringKategoriPengeluaranAdapter);
                spnKategori.setSelection(note.getKategori());
            }else {
                ArrayAdapter<String> stringKategoriPemasukanAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, kategoriPemasukan);
                stringKategoriPemasukanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnKategori.setAdapter(stringKategoriPemasukanAdapter);
                spnKategori.setSelection(note.getKategori());
            }
        }else{
            actionBarTitle = "Tambah";
            btnTitle = "Simpan";

            spnTipe.setAdapter(stringTipeAdapter);
            spnTipe.setOnItemSelectedListener(this);
        }

        getSupportActionBar().setTitle(actionBarTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnSubmit.setText(btnTitle);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btn_submit){
            String spntipe = spnTipe.getSelectedItem().toString();
            String spnkategori = spnKategori.getSelectedItem().toString();
            String title = edtTitle.getText().toString().trim();
            String description = edtDescription.getText().toString().trim();
            int tipe = spnTipe.getSelectedItemPosition();
            int kategori = spnKategori.getSelectedItemPosition();

            boolean isEmpty = false;

            if (TextUtils.isEmpty(title)){
                isEmpty = true;
                edtTitle.setError("Field can not be blank !!");
            }

            if (!isEmpty){
                Note newNote = new Note();
                newNote.setSpntipe(spntipe);
                newNote.setSpnkategori(spnkategori);
                newNote.setTitle(title);
                newNote.setDescription(description);
                newNote.setTipe(tipe);
                newNote.setKategori(kategori);

                Intent intent = new Intent();
                if (isEdit){

                    newNote.setDate(note.getDate());
                    newNote.setId(note.getId());
                    noteHelper.update(newNote);
                    intent.putExtra(EXTRA_POSITION, position);
                    setResult(RESULT_UPDATE, intent);
                    finish();
                }else{
                    newNote.setDate(getCurrentDate());
                    noteHelper.insert(newNote);

                    setResult(REQUEST_ADD);
                    finish();
                }
            }
        }
    }

    private String getCurrentDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isEdit){
            getMenuInflater().inflate(R.menu.menu_form, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    private void showAlertDialog(int type){
        final boolean isDialogCLose = type == ALERT_DIALOG_CLOSE;
        String dialogTitle = null, dialogMessege = null;

        if (isDialogCLose){
            dialogTitle = "Batal";
            dialogMessege = "Apakah anda ingin membatalkan perbahan pada form?";
        }else{
            dialogMessege = "Apakah anda yakin ingin menghapus item ini?";
            dialogTitle = "Hapus note";
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(dialogTitle);
        alertDialogBuilder
                .setMessage(dialogMessege)
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isDialogCLose){
                            finish();
                        }else{
                            noteHelper.delete(note.getId());
                            Intent intent = new Intent();
                            intent.putExtra(EXTRA_POSITION, position);
                            setResult(RESULT_DELETE, intent);
                            finish();
                        }
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_delete:
                showAlertDialog(ALERT_DIALOG_DELETE);
                break;
            case android.R.id.home:
                showAlertDialog(ALERT_DIALOG_CLOSE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        showAlertDialog(ALERT_DIALOG_CLOSE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (noteHelper != null){
            noteHelper.close();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (spnTipe.getSelectedItem().toString().equals("Pengeluaran")){
            ArrayAdapter<String> stringKategoriPengeluaranAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, kategoriPengeluaran);
            stringKategoriPengeluaranAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnKategori.setAdapter(stringKategoriPengeluaranAdapter);
        }else {
            ArrayAdapter<String> stringKategoriPemasukanAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, kategoriPemasukan);
            stringKategoriPemasukanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnKategori.setAdapter(stringKategoriPemasukanAdapter);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
