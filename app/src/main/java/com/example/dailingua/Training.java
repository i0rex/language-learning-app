package com.example.dailingua;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Training extends AppCompatActivity {

    private TextView karteikarte;
    private DatabaseHelper databaseHelper;
    private ImageView favoritImageView; // Instanzvariable für das ImageView
    private ImageView flagImageView;
    private Button turnButton; // Instanzvariable für den Button "turn"
    private MediaPlayer mediaPlayer;
    private ProgressBar progressBar;
    private boolean isOriginLangDisplayed = true; // Zustandsindikator für die Anzeige der Muttersprache

    private String textToShow;
    private String muttersprache;
    private String zielsprache;
    private String sprache;
    private int currentPhraseId = 1; // Instanzvariable für die aktuelle ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.training);

        // DatabaseHelper initialisieren
        databaseHelper = new DatabaseHelper(this, null);

        // TextView initialisieren
        karteikarte = findViewById(R.id.karteikarte);
        // ImageView initialisieren
        favoritImageView = findViewById(R.id.favorit);

        progressBar = findViewById(R.id.progressBar);

        flagImageView = findViewById(R.id.flag);
        // OnClickListener für das ImageView hinzufügen
        favoritImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Den aktuellen Wert des Favorits abrufen
                int currentFavoritValue = getCurrentFavoritValue();

                // Den neuen Wert des Favorits berechnen
                int newFavoritValue = (currentFavoritValue == 0) ? 1 : 0;

                // Das Bild des ImageView entsprechend des neuen Favoritwerts ändern
                favoritImageView.setImageResource((newFavoritValue == 1) ? R.drawable.star_full : R.drawable.star);

                // Die Datenbank aktualisieren und den Favoritwert setzen
                updateFavoritValue(newFavoritValue);
            }
        });

        // Button "turn" initialisieren und OnClickListener hinzufügen
        turnButton = findViewById(R.id.turn);
        turnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleTranslation(); // Methode aufrufen, um zwischen den Übersetzungen zu wechseln
            }
        });

        // Button yesIcon initialisieren und OnClickListener hinzufügen
        Button yesIcon = findViewById(R.id.yesIcon);
        yesIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseRating();
                isOriginLangDisplayed = true;
                updateTextView();
            }
        });


        // Button noIcon initialisieren und OnClickListener hinzufügen
        Button noIcon = findViewById(R.id.noIcon);
        noIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreaseRating();
                isOriginLangDisplayed = true;
                updateTextView();
            }
        });


        // Zurück-Button
        Button lektionen = findViewById(R.id.lektionen);
        lektionen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Training.this, Lektionen.class);
                startActivity(intent);
            }
        });

        Button training = findViewById(R.id.training);
        training.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Training.this, Training.class);
                startActivity(intent);
            }
        });

        Button statistik = findViewById(R.id.statistik);
        statistik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Training.this, Statistik.class);
                startActivity(intent);
            }
        });

        Button account = findViewById(R.id.lexikon);
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Training.this, Lexikon.class);
                startActivity(intent);
            }
        });

        Button homebtn = findViewById(R.id.homebtn);
        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Training.this, MainScreen.class);
                startActivity(intent);
            }
        });


        // Audio-Button
        Button audioButton = findViewById(R.id.audio);
        audioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Prüfen, ob der MediaPlayer bereits abgespielt wird
                if (mediaPlayer.isPlaying()) {
                    // Wenn ja, stoppe die Wiedergabe und setze den MediaPlayer zurück
                    mediaPlayer.pause();
                    mediaPlayer.seekTo(0);
                } else {
                    // Wenn nicht, starte die Wiedergabe
                    mediaPlayer.start();
                }
            }
        });


        // Zufälligen Text anzeigen, wenn die Seite geladen wird
        updateLanguages(); // Diese Zeile verschoben, damit die Variablen muttersprache und zielsprache initialisiert werden
        updateTextView();
    }


    // Methode zum Abrufen des aktuellen Werts des Favorits aus der Datenbank
    private int getCurrentFavoritValue() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Favorit FROM Phrasen WHERE ENG = ? OR GER = ? OR ESP = ?", new String[]{karteikarte.getText().toString()});
        int favoritValue = 0;
        if (cursor != null && cursor.moveToFirst()) {
            favoritValue = cursor.getInt(cursor.getColumnIndexOrThrow("Favorit"));
            cursor.close();
        }
        db.close();
        return favoritValue;
    }

    // Methode zum Aktualisieren des Favoritwerts in der Datenbank
    private void updateFavoritValue(int newFavoritValue) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Favorit", newFavoritValue);
        db.update("Phrasen", values, "ENG = ? OR GER = ? OR ESP = ?", new String[]{karteikarte.getText().toString()});
        db.close();
    }


    // Methode zum Aktualisieren des TextViews mit zufälligem Text aus der Datenbank
    // Aktualisierte updateTextView-Methode
    private void updateTextView() {
        // Öffnen der Datenbank im lesenden Modus
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        // Abfrage zum Abrufen der Muttersprache aus der Tabelle "Language"
        Cursor languageCursor = db.rawQuery("SELECT language FROM Language WHERE selection = 'Muttersprache'", null);

        // Überprüfen, ob die Abfrage erfolgreich war und mindestens ein Ergebnis zurückgegeben wurde
        if (languageCursor != null && languageCursor.moveToFirst()) {
            // Die aktuelle Muttersprache speichern
            muttersprache = languageCursor.getString(languageCursor.getColumnIndexOrThrow("language"));

            // Den Cursor schließen
            languageCursor.close();
        }

        // Abfrage zum Abrufen einer zufälligen Zeile aus der Tabelle "Phrasen"
        Cursor cursor = db.rawQuery("SELECT * FROM Phrasen ORDER BY RANDOM() LIMIT 1", null);

        // Überprüfen, ob die Abfrage erfolgreich war und mindestens ein Ergebnis zurückgegeben wurde
        if (cursor != null && cursor.moveToFirst()) {
            // Die aktuelle ID speichern
            currentPhraseId = cursor.getInt(cursor.getColumnIndexOrThrow("ID"));

            // Den deutschen oder englischen Text basierend auf dem Zustand des Buttons abrufen
            textToShow = isOriginLangDisplayed ? cursor.getString(cursor.getColumnIndexOrThrow(muttersprache)) :
                    cursor.getString(cursor.getColumnIndexOrThrow(zielsprache));

            // Das zufällige Wort im TextView anzeigen
            karteikarte.setText(textToShow);

            // Den aktuellen Favoritwert für den neuen Text abrufen
            int currentFavoritValue = getCurrentFavoritValue(currentPhraseId);
            // Das Bild des ImageView entsprechend des neuen Favoritwerts ändern
            favoritImageView.setImageResource((currentFavoritValue == 1) ? R.drawable.star_full : R.drawable.star);

            // Setze das Hintergrundbild des flagImageView basierend auf der Sprache
            updateFlag();
            // Den Cursor schließen
            cursor.close();
        } else {
            // Falls die Abfrage fehlschlägt oder kein Ergebnis zurückgegeben wird,
            // setze den TextView auf eine Standardmeldung oder handhabe den Fall entsprechend
            karteikarte.setText("Keine Daten vorhanden");
        }
        updateProgressBar();
        // Die Datenbankverbindung schließen
        db.close();

        // Die Audio-Daten aktualisieren
        updateAudio(currentPhraseId, muttersprache);
    }


    // Methode zum Aktualisieren der Audio-Daten
    private void updateAudio(int currentPhraseId, String language) {
        // Den Dateinamen zusammenstellen und sicherstellen, dass die Sprache in Kleinbuchstaben ist
        String fileName = language.toLowerCase() + currentPhraseId;

        // Die Ressourcen-ID der Audiodatei ermitteln und den MediaPlayer initialisieren
        int resourceId = getResources().getIdentifier(fileName, "raw", getPackageName());
        mediaPlayer = MediaPlayer.create(this, resourceId);
    }


    // Methode zum Abrufen des aktuellen Werts des Favorits für eine bestimmte ID aus der Datenbank
    private int getCurrentFavoritValue(int id) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Favorit FROM Phrasen WHERE ID = ?", new String[]{String.valueOf(id)});
        int favoritValue = 0;
        if (cursor != null && cursor.moveToFirst()) {
            favoritValue = cursor.getInt(cursor.getColumnIndexOrThrow("Favorit"));
            cursor.close();
        }
        db.close();
        return favoritValue;
    }


    // Methode zum Umschalten zwischen deutscher und englischer Übersetzung
    private void toggleTranslation() {
        isOriginLangDisplayed = !isOriginLangDisplayed; // Umschalten des Zustandsindikators
        // Die Anzeige des TextView aktualisieren, um die neue Übersetzung anzuzeigen
        updateTranslation();
    }

    // Methode zum Aktualisieren der Übersetzung des angezeigten Texts
    private void updateTranslation() {
        // Öffnen der Datenbank im lesenden Modus
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        // Abfrage zum Abrufen der aktuellen Phrase basierend auf der ID
        Cursor cursor = db.rawQuery("SELECT * FROM Phrasen WHERE ID = ?", new String[]{String.valueOf(currentPhraseId)});

        // Überprüfen, ob die Abfrage erfolgreich war und mindestens ein Ergebnis zurückgegeben wurde
        if (cursor != null && cursor.moveToFirst()) {
            // Den deutschen oder englischen Text basierend auf dem Zustand des Buttons abrufen
            String textToShow;
            if (isOriginLangDisplayed) {
                textToShow = cursor.getString(cursor.getColumnIndexOrThrow(muttersprache));
                updateAudio(currentPhraseId, muttersprache);
            } else {
                textToShow = cursor.getString(cursor.getColumnIndexOrThrow(zielsprache));
                updateAudio(currentPhraseId, zielsprache);
            }

            // Das zufällige Wort im TextView anzeigen
            karteikarte.setText(textToShow);

            // Den aktuellen Favoritwert für den neuen Text abrufen
            int currentFavoritValue = getCurrentFavoritValue(currentPhraseId);
            // Das Bild des ImageView entsprechend des neuen Favoritwerts ändern
            favoritImageView.setImageResource((currentFavoritValue == 1) ? R.drawable.star_full : R.drawable.star);

            // Setze das Hintergrundbild des flagImageView basierend auf der Sprache
            updateFlag();
            // Den Cursor schließen
            cursor.close();
        } else {
            // Falls die Abfrage fehlschlägt oder kein Ergebnis zurückgegeben wird,
            // setze den TextView auf eine Standardmeldung oder handhabe den Fall entsprechend
            karteikarte.setText("Keine Daten vorhanden");
        }
        // Die Datenbankverbindung schließen
        db.close();
    }

    private void updateLanguages() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        // Cursor für die Muttersprache abrufen
        Cursor mutterspracheCursor = db.rawQuery("SELECT language FROM Language WHERE selection = 'Muttersprache'", null);

        if (mutterspracheCursor != null && mutterspracheCursor.moveToFirst()) {
            // Muttersprache extrahieren
            muttersprache = mutterspracheCursor.getString(mutterspracheCursor.getColumnIndexOrThrow("language"));

            // Cursor schließen
            mutterspracheCursor.close();
        }

        // Cursor für die Zielsprache abrufen
        Cursor zielspracheCursor = db.rawQuery("SELECT language FROM Language WHERE selection = 'Zielsprache'", null);

        if (zielspracheCursor != null && zielspracheCursor.moveToFirst()) {
            // Zielsprache extrahieren
            zielsprache = zielspracheCursor.getString(zielspracheCursor.getColumnIndexOrThrow("language"));

            // Cursor schließen
            zielspracheCursor.close();
        }

        // Datenbankverbindung schließen
        db.close();
    }

    private void increaseRating() {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        // SQL-Abfrage, um den Progress zu aktualisieren, wo ENG, GER oder ESP den Wert von textToShow haben
        String updateQuery = "UPDATE Phrasen SET Progress = Progress + 1 WHERE ENG = ? OR GER = ? OR ESP = ?";

        // SQL-Abfrage ausführen
        db.execSQL(updateQuery, new String[]{textToShow, textToShow, textToShow});

        updateProgressBar();
        
        db.close();
    }

    private void decreaseRating() {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        // SQL-Abfrage, um den Progress zu aktualisieren, wo ENG, GER oder ESP den Wert von textToShow haben
        String updateQuery = "UPDATE Phrasen SET Progress = Progress - 1 WHERE ENG = ? OR GER = ? OR ESP = ?";

        // SQL-Abfrage ausführen
        db.execSQL(updateQuery, new String[]{textToShow, textToShow, textToShow});

        updateProgressBar();

        db.close();
    }

    private void updateProgressBar() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Progress FROM Phrasen WHERE ENG = ? OR GER = ? OR ESP = ?",
                new String[]{textToShow, textToShow, textToShow});
        int progress = 0;
        if (cursor != null && cursor.moveToFirst()) {
            progress = cursor.getInt(cursor.getColumnIndexOrThrow("Progress"));
            cursor.close();
        }
        // ProgressBar-Wert aktualisieren
        if (progress >= 5) {
            progressBar.setProgress(100);
        } else {
            progressBar.setProgress(progress * 20);
        }

        db.close();
    }




    private void updateFlag() {
        if (isOriginLangDisplayed) {
            if (muttersprache.equals("GER")) {
                flagImageView.setImageResource(R.drawable.germany_flag);
            } else if (muttersprache.equals("ENG")) {
                flagImageView.setImageResource(R.drawable.english_flag);
            } else if (muttersprache.equals("ESP")) {
                flagImageView.setImageResource(R.drawable.spain_flag);
            }
        }else {
            if (zielsprache.equals("GER")) {
                flagImageView.setImageResource(R.drawable.germany_flag);
            } else if (zielsprache.equals("ENG")) {
                flagImageView.setImageResource(R.drawable.english_flag);
            } else if (zielsprache.equals("ESP")) {
                flagImageView.setImageResource(R.drawable.spain_flag);
            }
        }

    }
}