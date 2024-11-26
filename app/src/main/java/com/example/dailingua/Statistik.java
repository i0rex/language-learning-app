package com.example.dailingua;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Statistik extends AppCompatActivity {
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistik);

        databaseHelper = new DatabaseHelper(this, null);

        // TextViews aus der XML-Datei referenzieren
        TextView mutterspracheTextView = findViewById(R.id.muttersprache);
        TextView zielspracheTextView = findViewById(R.id.zielsprache);
        TextView behandelteLektionen = findViewById(R.id.behandelteLektionen);
        TextView ungelerntePhrasen = findViewById(R.id.ungelerntePhrasen);
        TextView eineWdh = findViewById(R.id.eineWdh);
        TextView zweiWdh = findViewById(R.id.zweiWdh);
        TextView dreiWdh = findViewById(R.id.dreiWdh);
        TextView vierWdh = findViewById(R.id.vierWdh);
        TextView gelerntePhrasen = findViewById(R.id.gelerntePhrasen);
        TextView favorit = findViewById(R.id.favorit);

        int[] phraseCounts = getPhraseCounts();
        int favoritCount = getFavoritCount();

        // Setze den Text für die TextViews (optional)
        mutterspracheTextView.setText("Muttersprache: " + getMuttersprache());
        zielspracheTextView.setText("Zielsprache: " + getZielsprache());
        behandelteLektionen.setText("behandelte Lektionen: 0");
        ungelerntePhrasen.setText("ungelernte Phrasen: " + phraseCounts[0]); // Progress-Level 0
        eineWdh.setText("1x wiederholt: " + phraseCounts[1]); // Progress-Level 1
        zweiWdh.setText("2x wiederholt: " + phraseCounts[2]); // Progress-Level 2
        dreiWdh.setText("3x wiederholt: " + phraseCounts[3]); // Progress-Level 3
        vierWdh.setText("4x wiederholt: " + phraseCounts[4]); // Progress-Level 4
        gelerntePhrasen.setText("gelernte Phrasen: " + phraseCounts[5]); // Progress-Level 5 oder mehr
        favorit.setText("Favoriten: " + favoritCount);

        // Button zum Hauptbildschirm hinzufügen
        Button lektionen = findViewById(R.id.lektionen);
        lektionen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Statistik.this, Lektionen.class);
                startActivity(intent);
            }
        });

        Button training = findViewById(R.id.training);
        training.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Statistik.this, Training.class);
                startActivity(intent);
            }
        });

        Button statistik = findViewById(R.id.statistik);
        statistik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Statistik.this, Statistik.class);
                startActivity(intent);
            }
        });

        Button account = findViewById(R.id.lexikon);
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Statistik.this, Lexikon.class);
                startActivity(intent);
            }
        });

        Button homebtn = findViewById(R.id.homebtn);
        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Statistik.this, MainScreen.class);
                startActivity(intent);
            }
        });
    }

    private String getMuttersprache() {
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

    private String getZielsprache() {
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

    private int[] getPhraseCounts() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Progress FROM Phrasen", null);

        int[] phraseCounts = new int[6]; // 0, 1, 2, 3, 4, 5 oder mehr

        int countGreaterThanFive = 0;

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int progress = cursor.getInt(cursor.getColumnIndexOrThrow("Progress"));
                if (progress >= 0 && progress <= 4) {
                    phraseCounts[progress]++;
                } else {
                    countGreaterThanFive++;
                }
            } while (cursor.moveToNext());
            cursor.close();
        }

        phraseCounts[5] = countGreaterThanFive;

        return phraseCounts;
    }

    private int getFavoritCount() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Favorit FROM Phrasen", null);

        int favoritCount = 0;

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int favorit = cursor.getInt(cursor.getColumnIndexOrThrow("Favorit"));
                if (favorit == 1) {
                    favoritCount++;
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        return favoritCount;
    }
}