package com.example.dailingua;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest2 {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void mainActivityTest2() {
        // Starte die MainActivity und navigiere zum personalisierten Lernen
        onView(withId(R.id.getStarted)).perform(click());
        onView(withId(R.id.buttonPersonalisiertesLernen)).perform(click());

        // Für jede der Stufen (ALG1 bis ALG4)
        for (int stage = 0; stage < 4; stage++) {
            // Wähle die entsprechende Stufe im Spinner aus
            selectStage(stage);

            // Führe 50 Iterationen durch
            for (int i = 0; i < 50; i++) {
                // Klicke auf "Übungen anzeigen"
                onView(withId(R.id.buttonShowExercises)).perform(click());

                // Simuliere einen zufälligen Klick auf "Richtig" oder "Falsch"
                if (new Random().nextBoolean()) {
                    onView(withText("Richtig")).perform(scrollTo(), click());
                } else {
                    onView(withText("Falsch")).perform(scrollTo(), click());
                }
            }

            // Ausgabe des erreichten Niveaus für die aktuelle Stufe (Stage)
            int finalStage = stage;
            mActivityScenarioRule.getScenario().onActivity(activity -> {
                int reachedLevel = activity.userLevel;
                System.out.println("Erreichtes Niveau für Stufe " + finalStage + ": " + reachedLevel);
            });
        }
    }

    private void selectStage(int stage) {
        // Öffne den Spinner und wähle die entsprechende Stufe aus
        onView(withId(R.id.stageSpinner)).perform(click());
        String stageName = getStageName(stage);
        onView(withText(stageName)).perform(click());
    }

    private String getStageName(int stage) {
        switch (stage) {
            case 0:
                return "Stufe 1";
            case 1:
                return "Stufe 2";
            case 2:
                return "Stufe 3";
            case 3:
                return "Stufe 4";
            default:
                throw new IllegalArgumentException("Ungültige Stufe: " + stage);
        }
    }
}
