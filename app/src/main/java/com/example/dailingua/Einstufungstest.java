package com.example.dailingua;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Einstufungstest extends AppCompatActivity {

    private LinearLayout exerciseContainer;
    private List<Exercise> exercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.einstufungstest);

        exerciseContainer = findViewById(R.id.exerciseContainer);
        Button buttonCalculateLevel = findViewById(R.id.buttonCalculateLevel);

        generateExercises();

        buttonCalculateLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int userLevel = calculateLevel();
                Intent intent = new Intent(Einstufungstest.this, Personalize.class);
                intent.putExtra("USER_LEVEL", userLevel);
                startActivity(intent);
            }
        });
    }

    private void generateExercises() {
        exercises = new ArrayList<>();
        Random random = new Random();
        for (int i = 1; i <= 10; i++) {
            int difficulty = random.nextInt(100) + 1;
            Exercise exercise = new Exercise("Übung " + i, difficulty);
            exercises.add(exercise);

            View exerciseView = getLayoutInflater().inflate(R.layout.exercise_item, null);
            TextView exerciseTextView = exerciseView.findViewById(R.id.exerciseTextView);
            CheckBox exerciseCheckBox = exerciseView.findViewById(R.id.exerciseCheckBox);

            exerciseTextView.setText(exercise.getName() + " (Schwierigkeitsgrad: " + difficulty + ")");
            exerciseCheckBox.setTag(exercise);

            exerciseContainer.addView(exerciseView);
        }
    }

    private int calculateLevel() {
        int totalDifficulty = 0;
        int solvedDifficulty = 0;
        for (int i = 0; i < exerciseContainer.getChildCount(); i++) {
            View exerciseView = exerciseContainer.getChildAt(i);
            CheckBox exerciseCheckBox = exerciseView.findViewById(R.id.exerciseCheckBox);
            Exercise exercise = (Exercise) exerciseCheckBox.getTag();
            totalDifficulty += exercise.getDifficulty();
            if (exerciseCheckBox.isChecked()) {
                solvedDifficulty += exercise.getDifficulty();
            }
        }

        int userLevel = (solvedDifficulty * 100) / totalDifficulty;
        Toast.makeText(this, "Ihr Niveau ist: " + userLevel, Toast.LENGTH_LONG).show();
        return userLevel;
    }

    private static class Exercise {
        private final String name;
        private final int difficulty;

        public Exercise(String name, int difficulty) {
            this.name = name;
            this.difficulty = difficulty;
        }

        public String getName() {
            return name;
        }

        public int getDifficulty() {
            return difficulty;
        }
    }
}
