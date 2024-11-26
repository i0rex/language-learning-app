package com.example.dailingua;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public int userLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button getStartedButton = findViewById(R.id.getStarted);
        getStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Log.d(TAG, "Button 'Get Started' clicked");
                    // Aktion ausführen, wenn auf den Button "Get Started" geklickt wird
                    Intent intent = new Intent(MainActivity.this, MainScreen.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Log.e(TAG, "Error while starting MainScreen activity", e);
                }
            }
        });
    }
}