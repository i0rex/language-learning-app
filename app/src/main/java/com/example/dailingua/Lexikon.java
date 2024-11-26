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

public class Lexikon extends AppCompatActivity {

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
        setContentView(R.layout.lexikon); // Das entsprechende Layout setzen

        ScrollView scrollView = findViewById(R.id.scrollView2); // ScrollView initialisieren
        linearLayout = findViewById(R.id.linearLayout); // LinearLayout innerhalb des ScrollView initialisieren

        currentLektion = 1;
        updateTextView("default");

        Button buttonLektionen= findViewById(R.id.buttonLektionen);
        buttonLektionen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTextView("lektionen");
            }
        });

        Button buttonFavoriten= findViewById(R.id.buttonFavoriten);
        buttonFavoriten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTextView("favoriten");
            }
        });
        Button buttonLexikon= findViewById(R.id.buttonLexikon);
        buttonLexikon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTextView("lexikon");
            }
        });

        // Button zum Hauptbildschirm hinzufügen
        Button lektionen = findViewById(R.id.lektionen);
        lektionen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Lexikon.this, Lektionen.class);
                startActivity(intent);
            }
        });

        Button training = findViewById(R.id.training);
        training.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Lexikon.this, Training.class);
                startActivity(intent);
            }
        });

        Button statistik = findViewById(R.id.statistik);
        statistik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Lexikon.this, Statistik.class);
                startActivity(intent);
            }
        });

        Button account = findViewById(R.id.lexikon);
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Lexikon.this, Lexikon.class);
                startActivity(intent);
            }
        });

        Button homebtn = findViewById(R.id.homebtn);
        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Lexikon.this, MainScreen.class);
                startActivity(intent);
            }
        });
    }


    private void updateTextView(String status) {
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


        if (status.equals("default") || status.equals("lektionen")) {
            // SQL-Abfrage definieren
            String query = "SELECT " + targetLanguage + ", " + nativeLanguage + " FROM Phrasen";
            generateText(db, query);
        }
        if (status.equals("favoriten")){
            String query = "SELECT " + targetLanguage + ", " + nativeLanguage + " FROM Phrasen WHERE Favorit = 1";
            generateText(db, query);
        }
        if (status.equals("lexikon")){
            String query = "SELECT " + targetLanguage + ", " + nativeLanguage + " FROM Lexikon WHERE Thema = 'Konversation'";
            generateText(db, query);
        }

        db.close();
    }

    private void generateText(SQLiteDatabase db, String query ) {
        // SQL-Abfrage ausführen
        Cursor cursor = db.rawQuery(query, null);


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

            params.gravity = Gravity.START; // Text auf der linken Seite ausrichten
            params.setMargins(50, 10, 0, 10); // Hier die gewünschten Margins setzen (top, left, right, bottom)

            textView.setLayoutParams(params);

            defineTextViewBackground(textView, targetPhrase, nativePhrase);

            textView.setPadding(8, 8, 8, 8); // Innenabstand setzen
            textView.setTextColor(getResources().getColor(R.color.white)); // Textfarbe setzen
            textView.setTextSize(18); // Textgröße setzen
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD); // Textstil setzen
            linearLayout.addView(textView); // TextView dem LinearLayout hinzufügen

            count++;
        }


        // Cursor und Datenbank schließen, wenn nicht mehr benötigt
        cursor.close();
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
                    ((GradientDrawable) textView.getBackground()).setColor(ContextCompat.getColor(Lexikon.this, R.color.orange)); // Farbe ändern
                } else {
                    textView.setText(targetPhrase); // Die Zielsprache anzeigen
                    ((GradientDrawable) textView.getBackground()).setColor(ContextCompat.getColor(Lexikon.this, R.color.himmelblau)); // Farbe ändern
                }
                isTargetLanguage = !isTargetLanguage; // Den Sprachzustand umkehren
            }
        });
    }







}

