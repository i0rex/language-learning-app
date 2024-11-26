package com.example.dailingua;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.IOException;

public class Lektionen extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private String targetLanguage = "";
    private String nativeLanguage = "";
    private String currentLanguage = "";
    private int count = 1;
    private int currentLektion;
    private MediaPlayer mediaPlayer;
    private LinearLayout linearLayout;
    private int currentAudioIndex = 0;
    private Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lektionen); // Das entsprechende Layout setzen

        ScrollView scrollView = findViewById(R.id.scrollView2); // ScrollView initialisieren
        linearLayout = findViewById(R.id.linearLayout); // LinearLayout innerhalb des ScrollView initialisieren

        currentLektion = 1;
        updateTextView();
        updateAudio();


        Button leftButton = findViewById(R.id.arrowLeft);
        leftButton.setOnClickListener(v -> {
            if (currentLektion > 1 ){
                currentLektion--;
                updateTextView();
            }
        });

        Button rightButton = findViewById(R.id.arrowRight);
        int maxValueLektionen = getMaxValueLektionen();
        rightButton.setOnClickListener(v -> {
            if (currentLektion < maxValueLektionen ) {
                currentLektion++;
                updateTextView();
            }
        });

        Button audioButton = findViewById(R.id.audio);
        audioButton.setOnClickListener(v -> {
            // Prüfen, ob der MediaPlayer bereits abgespielt wird
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                // Wenn ja, stoppe die Wiedergabe und setze den MediaPlayer zurück
                mediaPlayer.pause();
                mediaPlayer.seekTo(0);
            } else {
                // Wenn nicht, starte die Wiedergabe der nächsten Audiodatei
                playNextAudio();
            }
        });

        // Button zum Hauptbildschirm hinzufügen
        Button lektionen = findViewById(R.id.lektionen);
        lektionen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Lektionen.this, Lektionen.class);
                startActivity(intent);
            }
        });

        Button training = findViewById(R.id.training);
        training.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Lektionen.this, Training.class);
                startActivity(intent);
            }
        });

        Button statistik = findViewById(R.id.statistik);
        statistik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Lektionen.this, Statistik.class);
                startActivity(intent);
            }
        });

        Button account = findViewById(R.id.lexikon);
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Lektionen.this, Lexikon.class);
                startActivity(intent);
            }
        });

        Button homebtn = findViewById(R.id.homebtn);
        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Lektionen.this, MainScreen.class);
                startActivity(intent);
            }
        });
    }



    private void updateTextView(){
        linearLayout.removeAllViews();
        databaseHelper = new DatabaseHelper(this, null);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String targetLanguageQuery = "SELECT language FROM Language WHERE selection = 'Zielsprache' OR selection = 'Muttersprache' ORDER BY selection DESC";
        Cursor targetLanguageCursor = db.rawQuery(targetLanguageQuery, null);
        // Überprüfen, ob die Zielsprache gefunden wurde
        if (targetLanguageCursor.moveToFirst()) {
            targetLanguage = targetLanguageCursor.getString(0); // Den Wert der Sprache abrufen (Index 0)
            // Die Zielsprache wurde gefunden, also suchen wir jetzt nach der Muttersprache
            while (targetLanguageCursor.moveToNext()) {
                // Die Muttersprache speichern
                nativeLanguage = targetLanguageCursor.getString(0); // Den Wert der Sprache abrufen (Index 0)
                break; // Wir nehmen nur die erste Muttersprache, danach beenden wir die Schleife
            }
        }
        targetLanguageCursor.close();

        // SQL-Abfrage definieren
        String query = "SELECT " + targetLanguage + ", " + nativeLanguage + " FROM Phrasen WHERE Dialog = " + currentLektion;

        // SQL-Abfrage ausführen
        Cursor cursor = db.rawQuery(query, null);

        TextView lektion = findViewById(R.id.lektion);
        lektion.setText("Lektion "+ currentLektion);

        // Ergebnisse durchgehen und TextViews erstellen und hinzufügen
        int count = 0; // Zähler für die Ausrichtung der Texte
        while (cursor.moveToNext()) {
            String targetPhrase = cursor.getString(0); // Den Wert der GER-Spalte abrufen (Index 0)
            String nativePhrase = cursor.getString(1); // Den Wert der GER-Spalte abrufen (Index 0)

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            // Die Breite des TextViews auf 70 % der Bildschirmbreite setzen
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            int screenWidth = displayMetrics.widthPixels;
            params.width = (int) (screenWidth * 0.7);

            TextView textView = new TextView(this);
            textView.setText(targetPhrase); // Den Wert von GER setzen

            // Überprüfen, ob der Text auf der rechten Seite sein soll
            if (count % 2 == 0) {
                params.gravity = Gravity.START; // Text auf der linken Seite ausrichten
                params.setMargins(50, 20, 0, 20); // Hier die gewünschten Margins setzen (top, left, right, bottom)
            } else {
                params.gravity = Gravity.END; // Text auf der rechten Seite ausrichten
                params.setMargins(0, 20, 100, 20); // Hier die gewünschten Margins setzen (top, left, right, bottom)
            }
            textView.setLayoutParams(params);

            defineTextViewBackground(textView, targetPhrase, nativePhrase);

            textView.setPadding(8, 8, 8, 8); // Innenabstand setzen
            textView.setTextColor(getResources().getColor(R.color.white)); // Textfarbe setzen
            textView.setTextSize(18); // Textgröße setzen
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD); // Textstil setzen
            linearLayout.addView(textView); // TextView dem LinearLayout hinzufügen

            count++;
        }

        updateAudio();
        // Cursor und Datenbank schließen, wenn nicht mehr benötigt
        cursor.close();
        db.close();
    }

    private void defineTextViewBackground(TextView textView, String targetPhrase, String nativePhrase) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(16); // Ecken abrunden

        if (textView.getText().equals(targetPhrase)) {
            drawable.setColor(ContextCompat.getColor(this, R.color.himmelblau)); // Hintergrundfarbe für Zielsprache setzen
        } else {
            drawable.setColor(ContextCompat.getColor(this, R.color.orange)); // Hintergrundfarbe für Muttersprache setzen
        }

        textView.setBackground(drawable); // Hintergrund setzen
        textView.setOnClickListener(new View.OnClickListener() {
            boolean isTargetLanguage = true; // Variable, um den Zustand der Sprache zu verfolgen

            @Override
            public void onClick(View v) {
                // Den Text je nach Sprachzustand aktualisieren
                if (isTargetLanguage) {
                    textView.setText(nativePhrase); // Die Muttersprache anzeigen
                    ((GradientDrawable) textView.getBackground()).setColor(ContextCompat.getColor(Lektionen.this, R.color.orange)); // Farbe ändern
                } else {
                    textView.setText(targetPhrase); // Die Zielsprache anzeigen
                    ((GradientDrawable) textView.getBackground()).setColor(ContextCompat.getColor(Lektionen.this, R.color.himmelblau)); // Farbe ändern
                }
                isTargetLanguage = !isTargetLanguage; // Den Sprachzustand umkehren
            }
        });
    }

    private void updateAudio() {
        // Initialisiere den MediaPlayer
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // Wenn die Wiedergabe abgeschlossen ist, spiele die nächste Audiodatei ab
                playNextAudio();
            }
        });
    }

    private int getMaxValueLektionen() {
        int maxValue = 0; // Initialisiere den maximalen Wert

        databaseHelper = new DatabaseHelper(this, null);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        // Die SQL-Abfrage, um das Maximum aus der Spalte "Dialog" zu erhalten
        String query = "SELECT MAX(Dialog) FROM Phrasen";
        Cursor cursor = db.rawQuery(query, null);

        // Überprüfen, ob der Cursor Daten enthält und das Maximum extrahieren
        if (cursor != null && cursor.moveToFirst()) {
            maxValue = cursor.getInt(0); // Das Maximum befindet sich in der ersten Spalte
            cursor.close(); // Schließe den Cursor
        }

        return maxValue; // Gib das Maximum zurück
    }

    private void playNextAudio() {
        databaseHelper = new DatabaseHelper(this, null);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String[] audioFiles;

        String query = "SELECT ID FROM Phrasen WHERE Dialog = " + currentLektion;
        Cursor cursor = db.rawQuery(query, null);

        int cursorCount = cursor.getCount(); // Anzahl der Zeilen im Cursor
        audioFiles = new String[cursorCount]; // Erstelle das Array mit der entsprechenden Größe

        // Durchlaufe den Cursor und füge Werte zu audioFiles hinzu
        currentLanguage = targetLanguage.toLowerCase();

        int i = 0;
        while (cursor.moveToNext()) {
            String fileName = currentLanguage+ cursor.getInt(cursor.getColumnIndexOrThrow("ID"));
            audioFiles[i] = fileName;
            i++;
        }
        cursor.close();

        // Überprüfe, ob der aktuelle Audio-Index innerhalb des Arrays liegt
        if (currentAudioIndex < audioFiles.length) {
            String fileName = audioFiles[currentAudioIndex];
            int resourceId = getResources().getIdentifier(fileName, "raw", getPackageName());
            try {
                mediaPlayer.reset();
                AssetFileDescriptor afd = getResources().openRawResourceFd(resourceId);
                if (afd != null) {
                    mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                    afd.close();
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    currentAudioIndex++; // Erhöhe den Index für die nächste Audiodatei
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Alle Audiodateien wurden abgespielt, setze den Index zurück
            currentAudioIndex = 0;
        }
    }

}