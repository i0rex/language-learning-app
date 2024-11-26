import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.dailingua.Personalize;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

@RunWith(AndroidJUnit4.class)
public class PersonalizeTest {

    private SharedPreferences sharedPreferences;
    private Random random;

    @Before
    public void setup() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        sharedPreferences = appContext.getSharedPreferences("ExercisePreferences", Context.MODE_PRIVATE);
        random = new Random();
    }

    @Test
    public void testAlgorithm1() {
        testAlgorithm(0);  // Stufe 1 (ALG1)
    }

    @Test
    public void testAlgorithm2() {
        testAlgorithm(1);  // Stufe 2 (ALG2)
    }

    @Test
    public void testAlgorithm3() {
        testAlgorithm(2);  // Stufe 3 (ALG3)
    }

    @Test
    public void testAlgorithm4() {
        testAlgorithm(3);  // Stufe 4 (ALG4)
    }

    private void testAlgorithm(int stage) {
        // Starte die Personalize Activity
        ActivityScenario<Personalize> scenario = ActivityScenario.launch(Personalize.class);

        // Wähle die Stufe im Spinner aus
        onView(withId(R.id.stageSpinner)).perform(click());
        onView(withText(getStageName(stage))).perform(click());

        for (int i = 0; i < 50; i++) {
            // Klicke auf "Übungen anzeigen"
            onView(withId(R.id.buttonShowExercises)).perform(click());

            // Warte, bis die Übungen angezeigt werden
            scenario.onActivity(activity -> {
                if (!activity.exerciseStack.isEmpty()) {
                    // Klicke zufällig auf "Richtig" oder "Falsch"
                    if (random.nextBoolean()) {
                        onView(withText("Richtig")).perform(click());
                    } else {
                        onView(withText("Falsch")).perform(click());
                    }
                }
            });
        }

        // Überprüfe das erreichte Niveau
        scenario.onActivity(activity -> {
            int reachedLevel = activity.userLevel;
            assertTrue("Niveau muss zwischen 1 und 100 liegen", reachedLevel >= 1 && reachedLevel <= 100);
            System.out.println("Erreichtes Niveau für Stufe " + stage + ": " + reachedLevel);
        });
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
