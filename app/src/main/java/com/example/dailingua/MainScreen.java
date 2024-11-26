package com.example.dailingua;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainScreen extends AppCompatActivity {

    private static final String TAG = "MainScreen";

    private int currentPhraseID;
    private String phrase = "";
    private boolean isNativeLanguageShown = false; // Variable zum Verfolgen des aktuellen Zustands

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        databaseHelper = new DatabaseHelper(this, null);

        Button buttonLektionen = findViewById(R.id.buttonLektionen);
        buttonLektionen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Log.d(TAG, "Button 'Lektionen' clicked");
                    // Aktion ausführen, wenn auf den Button "Lektionen" geklickt wird
                    Intent intent = new Intent(MainScreen.this, Lektionen.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Log.e(TAG, "Error while starting Lektionen activity", e);
                }
            }
        });

        final TextView phraseOfTheDay = findViewById(R.id.phraseOfTheDay);
        currentPhraseID = getRandomPhraseID();
        phraseOfTheDay.setText(updatePhrase());
        // Füge den OnClickListener zum TextView hinzu
        phraseOfTheDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Umschalten zwischen der Muttersprache und der Zielsprache
                isNativeLanguageShown = !isNativeLanguageShown;
                // Aktualisiere den Text basierend auf dem aktuellen Zustand
                if (isNativeLanguageShown) {
                    phraseOfTheDay.setText(updatePhraseForNativeLanguage());
                } else {
                    phraseOfTheDay.setText(updatePhrase());
                }
            }
        });

        Button buttonTraining = findViewById(R.id.buttonTraining);
        buttonTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aktion ausführen, wenn auf den Button "Get Started" geklickt wird
                Intent intent = new Intent(MainScreen.this, Training.class);
                startActivity(intent);
            }
        });

        Button buttonStatistik = findViewById(R.id.buttonStatistik);
        buttonStatistik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aktion ausführen, wenn auf den Button "Get Started" geklickt wird
                Intent intent = new Intent(MainScreen.this, Statistik.class);
                startActivity(intent);
            }
        });

        Button buttonAccount = findViewById(R.id.buttonAccount);
        buttonAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aktion ausführen, wenn auf den Button "Get Started" geklickt wird
                Intent intent = new Intent(MainScreen.this, Account.class);
                startActivity(intent);
            }
        });

        Button buttonPersonalisiertesLernen = findViewById(R.id.buttonPersonalisiertesLernen);
        buttonPersonalisiertesLernen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aktion ausführen, wenn auf den Button "Personalisiertes Lernen" geklickt wird
                Intent intent = new Intent(MainScreen.this, Personalize.class);
                startActivity(intent);
            }
        });
    }

    private String updatePhrase() {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        // Hole den Wert von 'Zielsprache' aus der Tabelle 'Language'
        Cursor languageCursor = db.rawQuery("SELECT language FROM Language WHERE selection = 'Zielsprache'", null);
        String language = ""; // Standardwert festlegen
        if (languageCursor.moveToFirst()) {
            language = languageCursor.getString(0);
        }
        languageCursor.close();

        // Überprüfen, ob currentPhraseID gültig ist
        if (currentPhraseID != -1) {
            Cursor phraseCursor = db.rawQuery("SELECT * FROM Phrasen WHERE ID = " + currentPhraseID, null);

            if (phraseCursor.moveToFirst()) {
                // Die Phrase entsprechend der Zielsprache auswählen
                phrase = phraseCursor.getString(phraseCursor.getColumnIndexOrThrow(language));
            }
            phraseCursor.close();
        }

        // Schließe die Datenbankverbindung
        db.close();

        return phrase;
    }

    // Die Methode updatePhraseForNativeLanguage hinzufügen
    private String updatePhraseForNativeLanguage() {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        Cursor languageCursor = db.rawQuery("SELECT language FROM Language WHERE selection = 'Muttersprache'", null);
        String nativeLanguage = "";
        if (languageCursor.moveToFirst()) {
            nativeLanguage = languageCursor.getString(0);
        }
        languageCursor.close();

        Cursor cursor = db.rawQuery("SELECT * FROM Phrasen WHERE ID = " + currentPhraseID, null);
        String phraseInNativeLanguage = "";
        if (cursor.moveToFirst()) {
            phraseInNativeLanguage = cursor.getString(cursor.getColumnIndexOrThrow(nativeLanguage));
        }
        cursor.close();
        db.close();

        return phraseInNativeLanguage;
    }

    private int getRandomPhraseID() {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT ID FROM Phrasen ORDER BY RANDOM() LIMIT 1", null);
        int randomPhraseID = -1;
        if (cursor != null && cursor.moveToFirst()) {
            randomPhraseID = cursor.getInt(cursor.getColumnIndexOrThrow("ID"));
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return randomPhraseID;
    }
}
