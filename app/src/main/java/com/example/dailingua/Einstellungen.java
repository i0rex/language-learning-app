package com.example.dailingua;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class Einstellungen extends AppCompatActivity {

    private CheckBox checkBoxAudio, checkBoxVideo, checkBoxText, checkBoxDialog, checkBoxSprechen;
    private CheckBox checkBoxReisen, checkBoxBuero, checkBoxKonversation;
    private Spinner spinnerStartLevel;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.einstellungen);

        sharedPreferences = getSharedPreferences("ExercisePreferences", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        checkBoxAudio = findViewById(R.id.checkBoxAudio);
        checkBoxVideo = findViewById(R.id.checkBoxVideo);
        checkBoxText = findViewById(R.id.checkBoxText);
        checkBoxDialog = findViewById(R.id.checkBoxDialog);
        checkBoxSprechen = findViewById(R.id.checkBoxSprechen);

        checkBoxReisen = findViewById(R.id.checkBoxReisen);
        checkBoxBuero = findViewById(R.id.checkBoxBuero);
        checkBoxKonversation = findViewById(R.id.checkBoxKonversation);

        spinnerStartLevel = findViewById(R.id.spinnerStartLevel);

        // Set checkboxes based on saved preferences or default to true
        checkBoxAudio.setChecked(sharedPreferences.getBoolean("Audio", true));
        checkBoxVideo.setChecked(sharedPreferences.getBoolean("Video", true));
        checkBoxText.setChecked(sharedPreferences.getBoolean("Text", true));
        checkBoxDialog.setChecked(sharedPreferences.getBoolean("Dialog", true));
        checkBoxSprechen.setChecked(sharedPreferences.getBoolean("Sprechen", true));

        checkBoxReisen.setChecked(sharedPreferences.getBoolean("Reisen", true));
        checkBoxBuero.setChecked(sharedPreferences.getBoolean("Buero", true));
        checkBoxKonversation.setChecked(sharedPreferences.getBoolean("Konversation", true));

        // Set spinner based on saved preferences or default to 0
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.level_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStartLevel.setAdapter(adapter);

        int savedLevel = sharedPreferences.getInt("startLevel", 0);
        spinnerStartLevel.setSelection(adapter.getPosition(String.valueOf(savedLevel)));

        Button buttonSaveSettings = findViewById(R.id.buttonSaveSettings);
        buttonSaveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save the preferences
                editor.putBoolean("Audio", checkBoxAudio.isChecked());
                editor.putBoolean("Video", checkBoxVideo.isChecked());
                editor.putBoolean("Text", checkBoxText.isChecked());
                editor.putBoolean("Dialog", checkBoxDialog.isChecked());
                editor.putBoolean("Sprechen", checkBoxSprechen.isChecked());

                editor.putBoolean("Reisen", checkBoxReisen.isChecked());
                editor.putBoolean("Buero", checkBoxBuero.isChecked());
                editor.putBoolean("Konversation", checkBoxKonversation.isChecked());

                int selectedLevel = Integer.parseInt(spinnerStartLevel.getSelectedItem().toString());
                editor.putInt("startLevel", selectedLevel);

                editor.apply();
                finish();
            }
        });
    }
}
