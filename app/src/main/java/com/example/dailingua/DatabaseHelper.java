package com.example.dailingua;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "dailingua_database21";

    public static final String PHRASEN_TAB_CREATE =
            "create table Phrasen (" +
                    " ID int primary key, " +
                    " GER text not null, " +
                    " ENG text not null, " +
                    " ESP text not null, " +
                    " Dialog int not null, " +
                    " Niveau int not null, " +
                    " Progress int, " +
                    " Favorit tinyint);";

    public static final String LANGUAGE_TAB_CREATE =
            "create table Language (" +
                    " ID int primary key, " +
                    " language text, " +
                    " selection text);";

    public static final String LEXIKON_TAB_CREATE =
            "create table Lexikon (" +
                    " ID int primary key, " +
                    " GER text not null, " +
                    " ENG text not null, " +
                    " ESP text not null, " +
                    " Thema text not null, " +
                    " Favorit tinyint);";

    public static final String EXERCISES_TAB_CREATE =
            "create table Exercises (" +
                    " ID integer primary key autoincrement, " +
                    " Name text not null, " +
                    " Type text not null, " +
                    " Difficulty int not null, " +
                    " Attempted int default 0, " +
                    " Successful int default 0, " +
                    " Unsuccessful int default 0, " +
                    " last_successful TIMESTAMP default NULL, " +
                    " last_unsuccessful TIMESTAMP default NULL, " +
                    " Theme text not null);";

    public DatabaseHelper (Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Wird bei DB-Neuanlage automatisch aufgerufen
        db.execSQL(PHRASEN_TAB_CREATE);
        db.execSQL(LANGUAGE_TAB_CREATE);
        db.execSQL(LEXIKON_TAB_CREATE);
        db.execSQL(EXERCISES_TAB_CREATE);

        insertInitialData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Upgrade-Logik (falls notwendig)
    }

    private void insertInitialData(SQLiteDatabase db) {
        // Lektion 1
        db.execSQL("INSERT INTO Phrasen (ID, GER, ENG, ESP, Dialog, Niveau, Progress, Favorit) " +
                "VALUES (1, 'Hallo, wie heißt du?', 'Hi, what is your name?', '¡Hola! ¿Cómo te llamas?', 1, 1, 0, 0);");
        db.execSQL("INSERT INTO Phrasen (ID, GER, ENG, ESP, Dialog, Niveau, Progress, Favorit) " +
                "VALUES (2, 'Hallo, ich heiße Anna.', 'Hi, my name is Anna.', 'Hola, me llamo Anna.', 1, 2, 0, 0);");
        db.execSQL("INSERT INTO Phrasen (ID, GER, ENG, ESP, Dialog, Niveau, Progress, Favorit) " +
                "VALUES (3, 'Ich bin Ben. Freut mich, dich kennenzulernen.', 'I am Ben. Nice to meet you. ', 'Soy Ben. Mucho gusto.', 1, 2, 0, 0);");
        db.execSQL("INSERT INTO Phrasen (ID, GER, ENG, ESP, Dialog, Niveau, Progress, Favorit) " +
                "VALUES (4, 'Woher kommst du?', 'Where are you from?', '¿De dónde eres?', 1, 2, 0, 0);");
        db.execSQL("INSERT INTO Phrasen (ID, GER, ENG, ESP, Dialog, Niveau, Progress, Favorit) " +
                "VALUES (5, 'Ich komme aus Deutschland.', 'I am from Germany.', 'Soy de Alemania.', 1, 2, 0, 0);");
        db.execSQL("INSERT INTO Phrasen (ID, GER, ENG, ESP, Dialog, Niveau, Progress, Favorit) " +
                "VALUES (6, 'Ich komme aus England. Sprichst du Englisch?', 'I am from England. Do you speak English?', 'Soy de Inglaterra. ¿Hablas inglés?', 1, 2, 0, 0);");
        db.execSQL("INSERT INTO Phrasen (ID, GER, ENG, ESP, Dialog, Niveau, Progress, Favorit) " +
                "VALUES (7, 'Ja, ich spreche ein bisschen Englisch. Kannstu du auch Spanisch?', 'Yes, I speak a little Englisch. Do you speak Spanish? ', 'Sí, hablo un poco de inglés. ¿Y tú español?', 1, 2, 0, 0);");
        db.execSQL("INSERT INTO Phrasen (ID, GER, ENG, ESP, Dialog, Niveau, Progress, Favorit) " +
                "VALUES (8, 'Nur ein bisschen, ich lerne es gerade. Möchtest du zusammen mit mir lernen?', 'Just a bit, I am learning it. Do you want to practice together? ', 'Solo un poco, estoy aprendiendo. ¿Quieres practicar juntos?', 1, 2, 0, 0);");
        // Lektion 2
        db.execSQL("INSERT INTO Phrasen (ID, GER, ENG, ESP, Dialog, Niveau, Progress, Favorit) " +
                "VALUES (9, 'Entschuldigung, können Sie mir helfen?', 'Excuse me, can you help me?', 'Disculpe, ¿puede ayudarme?', 2, 1, 0, 0);");
        db.execSQL("INSERT INTO Phrasen (ID, GER, ENG, ESP, Dialog, Niveau, Progress, Favorit) " +
                "VALUES (10, 'Natürlich, wie kann ich Ihnen helfen?', 'Of course, how can I help you?', 'Por supuesto, ¿cómo puedo ayudarle?', 2, 1, 0, 0);");
        db.execSQL("INSERT INTO Phrasen (ID, GER, ENG, ESP, Dialog, Niveau, Progress, Favorit) " +
                "VALUES (11, 'Ich muss zum Bahnhof, weiß aber nicht, wohin ich gehen muss.', 'I need to get to the train station, but I do not know where to go.', 'Necesito ir a la estación de tren, pero no sé hacia dónde ir.', 2, 1, 0, 0);");
        db.execSQL("INSERT INTO Phrasen (ID, GER, ENG, ESP, Dialog, Niveau, Progress, Favorit) " +
                "VALUES (12, 'Gehen Sie einfach geradeaus, bis zur nächsten Kreuzung und biegen rechts ab.', 'Just go straight ahead until the next intersection and turn right.', 'Simplemente siga recto hasta la próxima intersección y gire a la derecha.', 2, 1, 0, 0);");
        db.execSQL("INSERT INTO Phrasen (ID, GER, ENG, ESP, Dialog, Niveau, Progress, Favorit) " +
                "VALUES (13, 'Wie lange geht man dorthin?', 'How long does it take to get there?', '¿Cuánto tiempo se tarda en llegar allí?', 2, 1, 0, 0);");
        db.execSQL("INSERT INTO Phrasen (ID, GER, ENG, ESP, Dialog, Niveau, Progress, Favorit) " +
                "VALUES (14, 'Es geht schnell, nur fünf Minuten.', 'It is quick, only five minutes.', 'Es rápido, solo cinco minutos.', 2, 1, 0, 0);");
        db.execSQL("INSERT INTO Phrasen (ID, GER, ENG, ESP, Dialog, Niveau, Progress, Favorit) " +
                "VALUES (15, 'Vielen Dank, das hat mir sehr geholfen.', 'Thank you very much, that helped me a lot.', 'Muchas gracias, eso me ayudó mucho.', 2, 1, 0, 0);");
        // Lektion 3
        db.execSQL("INSERT INTO Phrasen (ID, GER, ENG, ESP, Dialog, Niveau, Progress, Favorit) " +
                "VALUES (16, 'Guten Abend, haben Sie noch einen Tisch für zwei Personen frei?', 'Good evening, do you have a table available for two people?', 'Buenas noches, ¿tienen una mesa libre para dos personas?', 3, 1, 0, 0);");
        db.execSQL("INSERT INTO Phrasen (ID, GER, ENG, ESP, Dialog, Niveau, Progress, Favorit) " +
                "VALUES (17, 'Ja es ist noch ein Tisch frei, möchten Sie am Fenster sitzen?', 'Yes, there is still a table available, would you like to sit by the window?', 'Sí, todavía hay una mesa libre, ¿les gustaría sentarse junto a la ventana?', 3, 1, 0, 0);");
        db.execSQL("INSERT INTO Phrasen (ID, GER, ENG, ESP, Dialog, Niveau, Progress, Favorit) " +
                "VALUES (18, 'Gerne, das klingt gut. ', 'Yes, please, that sounds good.', 'Sí, por favor, suena bien.', 3, 1, 0, 0);");
        db.execSQL("INSERT INTO Phrasen (ID, GER, ENG, ESP, Dialog, Niveau, Progress, Favorit) " +
                "VALUES (19, 'Ich bringe Ihnen gleich das Menü.', 'I will bring you the menu right away.', 'Les traeré el menú de inmediato.', 3, 1, 0, 0);");
        db.execSQL("INSERT INTO Phrasen (ID, GER, ENG, ESP, Dialog, Niveau, Progress, Favorit) " +
                "VALUES (20, 'Danke, wir warten so lange.', 'Thank you, we will wait.', 'Gracias, esperaremos.', 3, 1, 0, 0);");
        db.execSQL("INSERT INTO Phrasen (ID, GER, ENG, ESP, Dialog, Niveau, Progress, Favorit) " +
                "VALUES (21, 'Hier ist das Menü, möchten Sie schon etwas trinken?', 'Here is the menu, would you like to order something to drink?', 'Aquí tienen el menú, ¿les gustaría pedir algo de beber?', 3, 1, 0, 0);");
        db.execSQL("INSERT INTO Phrasen (ID, GER, ENG, ESP, Dialog, Niveau, Progress, Favorit) " +
                "VALUES (22, 'Wir schauen erstmal, danke.', 'We will look first, thank you.', 'Primero vamos a mirar, gracias.', 3, 1, 0, 0);");
        db.execSQL("INSERT INTO Phrasen (ID, GER, ENG, ESP, Dialog, Niveau, Progress, Favorit) " +
                "VALUES (23, 'Kein Problem, lassen Sie sich Zeit. Ich komme gleich wieder.', 'No problem, take your time. I will be back shortly.', 'No hay problema, tómense su tiempo. Volveré enseguida.', 3, 1, 0, 0);");


        db.execSQL("INSERT INTO Lexikon (ID, GER, ENG, ESP, Thema, Favorit) " +
                "VALUES (1, 'Wie heißt du?', 'What is your name?', 'Como te llamas?', 'Konversation', 0);");
        db.execSQL("INSERT INTO Lexikon (ID, GER, ENG, ESP, Thema, Favorit) " +
                "VALUES (2, 'Schön dich kennenzulernen!', 'Nice to meet you!', 'Muco gusto', 'Konversation', 0);");
        db.execSQL("INSERT INTO Lexikon (ID, GER, ENG, ESP, Thema, Favorit) " +
                "VALUES (3, 'Tschüß', 'Bye bye', 'Adios', 'Konversation', 0);");

        db.execSQL("INSERT INTO Language (ID, language, selection) VALUES (1, 'GER', 'Muttersprache')");
        db.execSQL("INSERT INTO Language (ID, language, selection) VALUES (2, 'ENG', 'Zielsprache')");
        db.execSQL("INSERT INTO Language (ID, language, selection) VALUES (3, 'ESP', 'leer')");

        insertExercises(db);
    }

    private void insertExercises(SQLiteDatabase db) {
        String[] types = {"Audio", "Video", "Text", "Dialog", "Sprechen"};
        String[] themes = {"Reisen", "Büro", "Konversation"};
        int id = 1;
        int themeIndex = 0;
        for (String type : types) {
            for (int i = 1; i <= 100; i++) {
                String theme = themes[themeIndex];
                db.execSQL("INSERT INTO Exercises (ID, Name, Type, Difficulty, Theme) " +
                        "VALUES (" + id + ", '" + type + " Übung " + i + "', '" + type + "', " + i + ", '" + theme + "');");
                id++;
                themeIndex = (themeIndex + 1) % themes.length;
            }
        }
    }

}
