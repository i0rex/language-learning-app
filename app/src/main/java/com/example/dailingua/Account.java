package com.example.dailingua;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class Account extends AppCompatActivity {


    private DatabaseHelper databaseHelper;
    private Spinner spinnerMuttersprache;
    private Spinner spinnerZielsprache;
    private String muttersprache = "Deutsch";
    private String zielsprache = "Englisch";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account);

        // DatabaseHelper initialisieren
        databaseHelper = new DatabaseHelper(this, null);
        // Initialisiere die Spinner
        spinnerMuttersprache = findViewById(R.id.spinnerMuttersprache);
        spinnerZielsprache = findViewById(R.id.spinnerZielsprache);

        // Fülle die Spinner mit Auswahlmöglichkeiten
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sprachen_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMuttersprache.setAdapter(adapter);

        // Entferne die aktuelle Auswahl des Muttersprache-Spinners aus den Optionen des Zielsprache-Spinners
        ArrayAdapter<CharSequence> zielspracheAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, android.R.id.text1);
        for (int i = 0; i < adapter.getCount(); i++) {
            if (!adapter.getItem(i).toString().equals(muttersprache)) {
                zielspracheAdapter.add(adapter.getItem(i));
            }
        }
        zielspracheAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerZielsprache.setAdapter(zielspracheAdapter);



        // Setze die Standardauswahl für die Muttersprache auf Deutsch
        muttersprache = getMuttersprache();
        spinnerMuttersprache.setSelection(adapter.getPosition(muttersprache));
        // Setze die Standardauswahl für die Zielsprache auf Englisch
        zielsprache = getZielsprache();
        spinnerZielsprache.setSelection(zielspracheAdapter.getPosition(zielsprache));

        // Setze einen Listener für die Spinner, um die ausgewählte Sprache zu speichern
        spinnerMuttersprache.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                muttersprache = parent.getItemAtPosition(position).toString();

                SQLiteDatabase db = databaseHelper.getWritableDatabase();
                Cursor cursor = db.rawQuery("SELECT * FROM Language WHERE selection = 'Muttersprache'", null);

                if (cursor.moveToFirst()) {
                    db.execSQL("UPDATE Language SET selection = 'leer' WHERE selection = 'Muttersprache'");
                }

                String languageCode = "";

                if (muttersprache.equals("Deutsch")) {
                    languageCode = "GER";
                } else if (muttersprache.equals("Englisch")) {
                    languageCode = "ENG";
                } else if (muttersprache.equals("Spanisch")) {
                    languageCode = "ESP";
                }

                if (!languageCode.isEmpty()) {
                    db.execSQL("UPDATE Language SET selection = 'Muttersprache' WHERE language = ?", new String[]{languageCode});
                }

                cursor.close();
                db.close();
                updateZielspracheSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerZielsprache.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                zielsprache = parent.getItemAtPosition(position).toString();

                SQLiteDatabase db = databaseHelper.getWritableDatabase();
                Cursor cursor = db.rawQuery("SELECT * FROM Language WHERE selection = 'Zielsprache'", null);

                if (cursor.moveToFirst()) {
                    db.execSQL("UPDATE Language SET selection = 'leer' WHERE selection = 'Zielsprache'");
                }

                String languageCode = "";

                if (zielsprache.equals("Deutsch")) {
                    languageCode = "GER";
                } else if (zielsprache.equals("Englisch")) {
                    languageCode = "ENG";
                } else if (zielsprache.equals("Spanisch")) {
                    languageCode = "ESP";
                }

                if (!languageCode.isEmpty()) {
                    db.execSQL("UPDATE Language SET selection = 'Zielsprache' WHERE language = ?", new String[]{languageCode});
                }

                cursor.close();
                db.close();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button lektionen = findViewById(R.id.lektionen);
        lektionen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Account.this, Lektionen.class);
                startActivity(intent);
            }
        });

        Button training = findViewById(R.id.training);
        training.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Account.this, Training.class);
                startActivity(intent);
            }
        });

        Button statistik = findViewById(R.id.statistik);
        statistik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Account.this, Statistik.class);
                startActivity(intent);
            }
        });

        Button account = findViewById(R.id.lexikon);
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Account.this, Lexikon.class);
                startActivity(intent);
            }
        });

        Button homebtn = findViewById(R.id.homebtn);
        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Account.this, MainScreen.class);
                startActivity(intent);
            }
        });

    }

    // Methode zur Aktualisierung des Zielsprache-Spinners basierend auf der Auswahl der Muttersprache
    private void updateZielspracheSpinner() {
        ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinnerMuttersprache.getAdapter();
        ArrayAdapter<CharSequence> zielspracheAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, android.R.id.text1);
        for (int i = 0; i < adapter.getCount(); i++) {
            if (!adapter.getItem(i).toString().equals(muttersprache)) {
                zielspracheAdapter.add(adapter.getItem(i));
            }
        }
        spinnerZielsprache.setAdapter(zielspracheAdapter);
    }

    public String getMuttersprache() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Language WHERE selection = 'Muttersprache'", null);

        String muttersprache = "";

        if (cursor.moveToFirst()) {
            int languageColumnIndex = cursor.getColumnIndex("language");

            if (languageColumnIndex >= 0) {
                String languageCode = cursor.getString(languageColumnIndex);

                if (languageCode.equals("GER")) {
                    muttersprache = "Deutsch";
                } else if (languageCode.equals("ENG")) {
                    muttersprache = "Englisch";
                } else if (languageCode.equals("ESP")) {
                    muttersprache = "Spanisch";
                }
            }
        }

        cursor.close();
        db.close();

        return muttersprache;
    }

    public String getZielsprache() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Language WHERE selection = 'Zielsprache'", null);

        String zielsprache = "";

        if (cursor.moveToFirst()) {
            int languageColumnIndex = cursor.getColumnIndex("language");

            if (languageColumnIndex >= 0) {
                String languageCode = cursor.getString(languageColumnIndex);

                if (languageCode.equals("GER")) {
                    zielsprache = "Deutsch";
                } else if (languageCode.equals("ENG")) {
                    zielsprache = "Englisch";
                } else if (languageCode.equals("ESP")) {
                    zielsprache = "Spanisch";
                }
            }
        }

        cursor.close();
        db.close();

        return zielsprache;
    }



}