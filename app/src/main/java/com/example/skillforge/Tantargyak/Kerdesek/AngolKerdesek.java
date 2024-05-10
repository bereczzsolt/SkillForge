package com.example.skillforge.Tantargyak.Kerdesek;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.example.skillforge.Aktivitik.PontActivity;
import com.example.skillforge.R;
import com.example.skillforge.Tantargyak.Aktivitik.AngolActivity;
import com.example.skillforge.Tantargyak.Model.KerdesModel;

import com.example.skillforge.Tantargyak.Aktivitik.TortenelemActivity;
import com.example.skillforge.databinding.ActivityKerdesekBinding;

import java.util.ArrayList;


public class AngolKerdesek extends AppCompatActivity {


    ArrayList<KerdesModel> list = new ArrayList<>();
    ActivityKerdesekBinding binding;
    private int szamlalo = 0;
    private int helyzet = 0;
    private int pont = 0;
    CountDownTimer ido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityKerdesekBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        idoujraindito();
        ido.start();


        String temanev = getIntent().getStringExtra("tema");
        if (temanev != null) {
            switch (temanev) {
                case "Angol-Magyar Szófordítás":
                    Tema_feladat1();
                    break;
                case "Angol Nyelvtan":
                    Tema_feladat2();
                    break;
                case "Olvasás és Értelmezés":
                    Tema_feladat3();
                    break;
                case "Szókincs és Szóhasználat":
                    Tema_feladat4();
                    break;
                case "Írás és Összeállítás":
                    Tema_feladat5();
                    break;
                case "Szövegértés":
                    Tema_feladat6();
                    break;
                case "Angol nyelvoktatási módszertan":
                    Tema_feladat7();
                    break;
                default:
                    // Ismeretlen téma kezelése
                    break;
            }
        }



        for (int i = 0; i < 4 && i < list.size(); i++) {
            binding.Valaszlehetosegek.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkAnswer((Button) view);
                }
            });

            if (helyzet < list.size()) {
                playAnimation(binding.feltettkerdes, 0, list.get(helyzet).getKerdes());
            }
        }


        binding.kevetkezokerdes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ido != null){
                    ido.cancel();
                }
                ido.start();

                binding.kevetkezokerdes.setEnabled(false);
                binding.kevetkezokerdes.setAlpha((float) 0.3);
                ElerhetoValasz(true);
                helyzet++;
                if (helyzet == list.size()) {

                    Intent intent = new Intent(AngolKerdesek.this, PontActivity.class);
                    intent.putExtra("pont", pont);
                    intent.putExtra("osszes", list.size());
                    startActivity(intent);
                    finish();
                    return;
                }
                szamlalo = 0;
                playAnimation(binding.feltettkerdes,0,list.get(helyzet).getKerdes());



            }
        });


    }

    private void idoujraindito() {

        ido = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long l) {

                binding.ido.setText(String.valueOf(l/1000));
            }

            @Override
            public void onFinish() {
                Dialog dialog = new Dialog(AngolKerdesek.this);
                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.lejartido);
                dialog.findViewById(R.id.lejartidogomb).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(AngolKerdesek.this, AngolActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                dialog.show();
            }
        };
    }


    private void playAnimation(View view, int ertek, String kerdes) {

        view.animate().alpha(ertek).scaleX(ertek).scaleY(ertek).setDuration(500).setStartDelay(100).setInterpolator(new DecelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(@NonNull Animator animation) {
                        if (ertek == 0 && szamlalo < 4) {
                            String option = "";

                            if (szamlalo == 0) {
                                option = list.get(helyzet).getOpcio1();
                            } else if (szamlalo == 1) {
                                option = list.get(helyzet).getOpcio2();
                            } else if (szamlalo == 2) {
                                option = list.get(helyzet).getOpcio3();
                            } else if (szamlalo == 3) {
                                option = list.get(helyzet).getOpcio4();
                            }
                            playAnimation(binding.Valaszlehetosegek.getChildAt(szamlalo), 0, option);
                            szamlalo++;

                        }
                    }

                    @Override
                    public void onAnimationEnd(@NonNull Animator animation) {

                        if (ertek == 0) {

                            try {
                                ((TextView) view).setText(kerdes);
                                binding.kerdesekszama.setText(helyzet + 1 + "/" + list.size());

                            } catch (Exception e) {
                                ((Button) view).setText(kerdes);
                            }
                            view.setTag(kerdes);
                            playAnimation(view, 1, kerdes);
                        }

                    }

                    @Override
                    public void onAnimationCancel(@NonNull Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(@NonNull Animator animation) {

                    }

                });
    }

    private void ElerhetoValasz(boolean elerheto) {

        for (int i= 0; i<4; i++)
        {
            binding.Valaszlehetosegek.getChildAt(i).setEnabled(elerheto);
            if(elerheto){
                binding.Valaszlehetosegek.getChildAt(i).setBackgroundResource(R.drawable.background);

            }

        }

    }





    private void checkAnswer(Button valasztottOpcio) {

        if(ido!=null)
        {
            ido.cancel();

        }
        binding.kevetkezokerdes.setEnabled(true);
        binding.kevetkezokerdes.setAlpha(1);

        // Ellenőrizzük, hogy a helyes válasz megegyezik-e a kiválasztott válasszal
        if (valasztottOpcio.getText().toString().equals(list.get(helyzet).getJovalasz())) {
            // Ha a válasz helyes, növeljük a pontszámot és beállítjuk a megfelelő háttérszínt
            pont++;
            valasztottOpcio.setBackgroundResource(R.drawable.jo_valasz);
        } else {
            // Ha a válasz helytelen, beállítjuk a megfelelő háttérszínt mindkét gombnak
            valasztottOpcio.setBackgroundResource(R.drawable.rossz_valasz);
            Button helyes = (Button) binding.Valaszlehetosegek.findViewWithTag(list.get(helyzet).getJovalasz());
            if (helyes != null) {
                helyes.setBackgroundResource(R.drawable.jo_valasz); // A helyes válasz jelzése
            }
        }
    }


    private void  Tema_feladat7() {
        list.add(new KerdesModel("What is the communicative approach in language teaching?",
                "Egy nyelvi környezetben való gyakorlást helyezi előtérbe", "Nyelvtani szabályok és memorizálás", "Szókincsfejlesztés", "Nyelvtani szabályok és memorizálás", "Egy nyelvi környezetben való gyakorlást helyezi előtérbe"));
        list.add(new KerdesModel("Melyik nyelvtanítási módszer híres a 'call and response' technikáról?",
                "Audio-lingual method", "Grammar-translation method", "Direct method", "Total Physical Response method", "Total Physical Response method"));
        list.add(new KerdesModel("What is the main focus of the Direct Method?",
                "Grammar explanations", "Translation exercises", "Vocabulary drills", "Communicative practice", "Communicative practice"));
        list.add(new KerdesModel("Melyik módszer hangsúlyozza a gyakorlati életből vett helyzeteket a tanításban?",
                "Communicative Language Teaching", "Grammar-translation method", "Audio-lingual method", "Direct Method", "Communicative Language Teaching"));
        list.add(new KerdesModel("What is the main goal of the Task-Based Learning method?",
                "Learning grammatical structures", "Improving vocabulary knowledge", "Developing fluency through completing tasks", "Memorizing dialogues", "Developing fluency through completing tasks"));
        list.add(new KerdesModel("Melyik módszer használja a szerepjátékokat és a szituációs gyakorlatokat a tanítás során?",
                "Grammar-translation method", "Communicative Language Teaching", "Total Physical Response method", "Direct Method", "Communicative Language Teaching"));
        list.add(new KerdesModel("What is the Silent Way method known for?",
                "Using translation exercises", "Emphasizing the role of the teacher as a facilitator", "Encouraging learner independence", "Focusing on communicative practice", "Encouraging learner independence"));
        list.add(new KerdesModel("Melyik módszerben használják az analógiákat és a vizuális segédeszközöket a tanítás során?",
                "Grammar-translation method", "Total Physical Response method", "Audio-lingual method", "The Silent Way", "The Silent Way"));
        list.add(new KerdesModel("What does the Total Physical Response method emphasize?",
                "Memorizing vocabulary lists", "Using translation exercises", "Using physical actions to convey meaning", "Writing essays", "Using physical actions to convey meaning"));
        list.add(new KerdesModel("Melyik módszerben nincs szükség a célnyelv használatára a tanítás során?",
                "Audio-lingual method", "Communicative Language Teaching", "Grammar-translation method", "The Silent Way", "Grammar-translation method"));
        list.add(new KerdesModel("What is the main focus of the Grammar-Translation method?",
                "Developing speaking skills", "Understanding grammatical structures and memorizing vocabulary", "Using authentic materials", "Engaging in communicative activities", "Understanding grammatical structures and memorizing vocabulary"));
        list.add(new KerdesModel("Melyik módszer hangsúlyozza az autentikus anyagok használatát a tanítás során?",
                "Total Physical Response method", "Grammar-translation method", "Direct Method", "Task-Based Learning method", "Task-Based Learning method"));
        list.add(new KerdesModel("What is the main characteristic of the Audio-lingual method?",
                "Using translation exercises", "Focusing on memorization of vocabulary lists", "Using drills and pattern practice", "Encouraging learner creativity", "Using drills and pattern practice"));
        list.add(new KerdesModel("Melyik módszerben hangsúlyozzák a diákok aktív részvételét a tanítás során?",
                "Grammar-translation method", "Communicative Language Teaching", "Direct Method", "Audio-lingual method", "Communicative Language Teaching"));
        list.add(new KerdesModel("What is the main principle of the Communicative Language Teaching method?",
                "Learning grammatical rules", "Focusing on vocabulary acquisition", "Using authentic communication in the target language", "Using translation exercises", "Using authentic communication in the target language"));

    }

    private void  Tema_feladat6() {
        list.add(new KerdesModel("What is the capital of France?",
                "Párizs", "Madrid", "Róma", "Berlin", "Párizs"));
        list.add(new KerdesModel("Hány nap van egy hétben?",
                "Seven", "Hat", "Öt", "Nyolc", "Seven"));
        list.add(new KerdesModel("Melyik állat rója az afrikai szavannákat?",
                "Elefánt", "Tigris", "Zebra", "Orángután", "Zebra"));
        list.add(new KerdesModel("Mi a jelentése a 'hello' szónak?",
                "Szia", "Viszlát", "Jó reggelt", "Köszönöm", "Szia"));
        list.add(new KerdesModel("Hány lába van egy póknak?",
                "Kettő", "Négy", "Hat", "Nyolc", "Nyolc"));
        list.add(new KerdesModel("What is the Hungarian word for 'apple'?",
                "Körte", "Banán", "Alma", "Szilva", "Alma"));
        list.add(new KerdesModel("Melyik évben kezdődött az első világháború?",
                "1914", "1918", "1920", "1905", "1914"));
        list.add(new KerdesModel("What color is the sky on a clear day?",
                "Kék", "Zöld", "Piros", "Sárga", "Kék"));
        list.add(new KerdesModel("Mi a legnagyobb folyó Európában?",
                "Tisz", "Duna", "Volga", "Szajna", "Volga"));
        list.add(new KerdesModel("What is the Hungarian word for 'goodbye'?",
                "Viszlát", "Helló", "Szia", "Köszönöm", "Viszlát"));
        list.add(new KerdesModel("Melyik kontinensen található Ausztrália?",
                "Európa", "Ázsia", "Afrika", "Ausztrália", "Ausztrália"));
        list.add(new KerdesModel("What is the English word for 'madár'?",
                "Bird", "Cat", "Dog", "Fish", "Bird"));
        list.add(new KerdesModel("Mi az angol megfelelője a 'könyv' szónak?",
                "Book", "Table", "Chair", "Desk", "Book"));
        list.add(new KerdesModel("What is the Hungarian word for 'water'?",
                "Lev", "Tűz", "Víz", "Föld", "Víz"));
        list.add(new KerdesModel("Mi az angol megfelelője a 'számítógép' szónak?",
                "Computer", "Television", "Telephone", "Radio", "Computer"));

    }

    private void  Tema_feladat5() {
        list.add(new KerdesModel("What is the past tense of the verb 'to go'?",
                "went", "gone", "goed", "go", "went"));
        list.add(new KerdesModel("Which word is a synonym for 'happy'?",
                "sad", "joyful", "angry", "frustrated", "joyful"));
        list.add(new KerdesModel("What is the plural form of 'child'?",
                "childs", "childrens", "childes", "children", "children"));
        list.add(new KerdesModel("Which word is an adverb?",
                "slow", "quick", "slowly", "quickly", "slowly"));
        list.add(new KerdesModel("What is the comparative form of 'big'?",
                "more big", "bigger", "biggest", "biger", "bigger"));
        list.add(new KerdesModel("Which tense is used to express actions that will happen in the future?",
                "present tense", "past tense", "future tense", "conditional tense", "future tense"));
        list.add(new KerdesModel("What is the opposite of 'hot'?",
                "cold", "warm", "cool", "heat", "cold"));
        list.add(new KerdesModel("Which word is a conjunction?",
                "and", "run", "quick", "yellow", "and"));
        list.add(new KerdesModel("What is the comparative form of 'good'?",
                "goodest", "better", "more good", "gooder", "better"));
        list.add(new KerdesModel("Which tense is used to express actions happening at the moment?",
                "past tense", "future tense", "present tense", "conditional tense", "present tense"));
        list.add(new KerdesModel("What is the opposite of 'fast'?",
                "slow", "quick", "rapid", "speedy", "slow"));
        list.add(new KerdesModel("Which word is a preposition?",
                "run", "in", "quick", "yellow", "in"));
        list.add(new KerdesModel("What is the comparative form of 'far'?",
                "farer", "more far", "farrer", "farther", "farther"));
        list.add(new KerdesModel("Which tense is used to express hypothetical situations?",
                "past tense", "future tense", "present tense", "conditional tense", "conditional tense"));
        list.add(new KerdesModel("What is the plural form of 'ox'?",
                "oxs", "oxen", "oxes", "oxons", "oxen"));


    }
    private void  Tema_feladat4() {
        list.add(new KerdesModel("Mi az angol megfelelője a \"kutya\"-nak?", "dog", "cat", "bird", "fish", "dog"));
        list.add(new KerdesModel("Hogyan mondjuk angolul a \"macska\" szót?", "cat", "dog", "rabbit", "hamster", "cat"));
        list.add(new KerdesModel("Mi az angol megfelelője a \"ház\"-nak?", "house", "apartment", "building", "cottage", "house"));
        list.add(new KerdesModel("Hogyan mondjuk angolul az \"asztal\" szót?", "table", "chair", "desk", "shelf", "table"));
        list.add(new KerdesModel("Mi az angol megfelelője a \"toll\"-nak?", "pen", "pencil", "marker", "brush", "pen"));
        list.add(new KerdesModel("Hogyan fordítjuk angolra a \"ceruza\" szót?", "pencil", "pen", "marker", "crayon", "pencil"));
        list.add(new KerdesModel("Mi az angol kifejezése a \"iskola\"-nak?", "school", "university", "college", "classroom", "school"));
        list.add(new KerdesModel("Mi az angol megfelelője a \"tanár\"-nak?", "teacher", "student", "professor", "lecturer", "teacher"));
        list.add(new KerdesModel("Hogyan mondjuk angolul a \"diák\" szót?", "student", "teacher", "pupil", "learner", "student"));
        list.add(new KerdesModel("Mi az angol kifejezése a \"számítás\"-nak?", "calculation", "equation", "computation", "mathematics", "calculation"));
        list.add(new KerdesModel("Hogyan mondjuk angolul az \"óra\" szót az időmérésre?", "clock", "watch", "time", "hour", "clock"));
        list.add(new KerdesModel("Mi az angol megfelelője a \"autó\"-nak?", "car", "bus", "train", "bike", "car"));
        list.add(new KerdesModel("Mi az angol kifejezése a \"repülőgép\"-nek?", "airplane", "helicopter", "rocket", "balloon", "airplane"));
        list.add(new KerdesModel("Hogyan fordítjuk angolra a \"számítógép\" szót?", "computer", "telephone", "television", "calculator", "computer"));
        list.add(new KerdesModel("Mi az angol megfelelője a \"telefon\"-nak?", "telephone", "computer", "television", "cellphone", "telephone"));

    }

    private void  Tema_feladat3() {
        list.add(new KerdesModel("Mi az angol megfelelője a \"könyv\"-nek?", "book", "newspaper", "magazine", "journal", "book"));
        list.add(new KerdesModel("Mi az angol kifejezése a \"számítógép\"-nek?", "computer", "telephone", "television", "calculator", "computer"));
        list.add(new KerdesModel("Hogyan mondjuk angolul a \"kutya\" szót?", "dog", "cat", "bird", "fish", "dog"));
        list.add(new KerdesModel("Hogyan fordítjuk angolra a \"macska\" szót?", "cat", "dog", "rabbit", "hamster", "cat"));
        list.add(new KerdesModel("Mi az angol megfelelője a \"ház\"-nak?", "house", "apartment", "building", "cottage", "house"));
        list.add(new KerdesModel("Hogyan mondjuk angolul az \"asztal\" szót?", "table", "chair", "desk", "shelf", "table"));
        list.add(new KerdesModel("Mi az angol megfelelője a \"toll\"-nak?", "pen", "pencil", "marker", "brush", "pen"));
        list.add(new KerdesModel("Hogyan fordítjuk angolra a \"ceruza\" szót?", "pencil", "pen", "marker", "crayon", "pencil"));
        list.add(new KerdesModel("Mi az angol kifejezése a \"iskola\"-nak?", "school", "university", "college", "classroom", "school"));
        list.add(new KerdesModel("Mi az angol megfelelője a \"tanár\"-nak?", "teacher", "student", "professor", "lecturer", "teacher"));
        list.add(new KerdesModel("Hogyan mondjuk angolul a \"diák\" szót?", "student", "teacher", "pupil", "learner", "student"));
        list.add(new KerdesModel("Mi az angol kifejezése a \"számítás\"-nak?", "calculation", "equation", "computation", "mathematics", "calculation"));
        list.add(new KerdesModel("Hogyan mondjuk angolul az \"óra\" szót az időmérésre?", "clock", "watch", "time", "hour", "clock"));
        list.add(new KerdesModel("Mi az angol megfelelője a \"autó\"-nak?", "car", "bus", "train", "bike", "car"));
        list.add(new KerdesModel("Mi az angol kifejezése a \"repülőgép\"-nek?", "airplane", "helicopter", "rocket", "balloon", "airplane"));

    }

    private void Tema_feladat2() {
        list.add(new KerdesModel("Mi az angol megfelelője a \"személyautó\"-nak?", "car", "bus", "train", "bike", "car"));
        list.add(new KerdesModel("Melyik mondat helyes az alábbiak közül: \"She go to school every day.\" vagy \"She goes to school every day.\"?", "She goes to school every day.", "She go to school every day.", "She go to school everyday.", "She going to school every day.", "She goes to school every day."));
        list.add(new KerdesModel("Hogyan kell helyesen használni a \"they\" személyes névmást az alábbi mondatban: \"Tom and Mary __ very happy.\"?", "are", "is", "am", "be", "are"));
        list.add(new KerdesModel("Melyik mondat helyes az alábbiak közül: \"I is a student.\" vagy \"I am a student.\"?", "I am a student.", "I is a student.", "I are a student.", "I be a student.", "I am a student."));
        list.add(new KerdesModel("Melyik mondat helyes az alábbiak közül: \"He are my friend.\" vagy \"He is my friend.\"?", "He is my friend.", "He are my friend.", "He am my friend.", "He be my friend.", "He is my friend."));
        list.add(new KerdesModel("Melyik mondat helyes az alábbiak közül: \"She don't like coffee.\" vagy \"She doesn't like coffee.\"?", "She doesn't like coffee.", "She don't like coffee.", "She didn't like coffee.", "She not like coffee.", "She doesn't like coffee."));
        list.add(new KerdesModel("Melyik időt használjuk a jövőbeli cselekvések kifejezésére az angol nyelvben?", "Future tense", "Present tense", "Past tense", "Continuous tense", "Future tense"));
        list.add(new KerdesModel("Mi a továbbiakban a 3. számú egyes számú igealak az angolban?", "He/She/It + alany + alapige + -s/t.", "He/She/It + do/does + alapige.", "I/You/We/They + alany + alapige.", "I/You/We/They + have/has + alapige.", "He/She/It + alany + alapige + -s/t."));
        list.add(new KerdesModel("Melyik az alábbiak közül az alany az angol nyelvben: \"The dog barks.\" vagy \"The cat chased the mouse.\"?", "The dog", "The cat", "barks", "chased", "The dog"));
        list.add(new KerdesModel("Melyik az alábbiak közül az alapige az angol nyelvben: \"She is playing.\" vagy \"He goes to school every day.\"?", "playing", "goes", "is", "school", "playing"));
        list.add(new KerdesModel("Melyik mondat helyes az alábbiak közül: \"I have a cat.\" vagy \"I has a cat.\"?", "I have a cat.", "I has a cat.", "I haves a cat.", "I having a cat.", "I have a cat."));
        list.add(new KerdesModel("Hogyan kell helyesen használni az \"am\" igét az alábbi mondatban: \"I __ happy.\"?", "am", "is", "are", "be", "am"));
        list.add(new KerdesModel("Melyik mondat helyes az alábbiak közül: \"You is my best friend.\" vagy \"You are my best friend.\"?", "You are my best friend.", "You is my best friend.", "You am my best friend.", "You be my best friend.", "You are my best friend."));
        list.add(new KerdesModel("Mi az angol megfelelője a \"szeret\"-nek az ige alakjaiban?", "like", "likes", "liked", "liking", "like"));
        list.add(new KerdesModel("Melyik mondat helyes az alábbiak közül: \"We has two cats.\" vagy \"We have two cats.\"?", "We have two cats.", "We has two cats.", "We have two cat.", "We haves two cats.", "We have two cats."));


    }

    private void Tema_feladat1() {
        list.add(new KerdesModel("Mi a magyar szó az angol \"car\"-re?", "kocsi", "ház", "alma", "kenyér", "kocsi"));

        list.add(new KerdesModel("Fordítsd le \"apple\"-t magyarra.", "alma", "fa", "könyv", "autó", "alma"));

        list.add(new KerdesModel("Mi az angol szó a \"könyv\"-re?", "book", "table", "chair", "pen", "book"));

        list.add(new KerdesModel("Fordítsd le az \"számítógép\" szót angolra.", "computer", "house", "dog", "phone", "computer"));

        list.add(new KerdesModel("Mi az angol szó a \"kutya\"-ra?", "dog", "cat", "horse", "bird", "dog"));

        list.add(new KerdesModel("Fordítsd le a \"virág\"-ot angolra.", "flower", "tree", "river", "mountain", "flower"));

        list.add(new KerdesModel("Mi a jelentése az \"almafa\"-nak angolul?", "apple tree", "orange tree", "banana tree", "cherry tree", "apple tree"));

        list.add(new KerdesModel("Fordítsd le a \"törölköző\"-t angolra.", "towel", "shirt", "pants", "shoes", "towel"));

        list.add(new KerdesModel("Mi az angol szó az \"asztal\"-ra?", "table", "chair", "bed", "sofa", "table"));

        list.add(new KerdesModel("Fordítsd le az \"ablak\"-ot angolra.", "window", "door", "roof", "floor", "window"));

        list.add(new KerdesModel("Mi a jelentése a \"tévé\"-nek angolul?", "TV", "radio", "computer", "phone", "TV"));

        list.add(new KerdesModel("Fordítsd le az \"iskola\"-t angolra.", "school", "hospital", "bank", "library", "school"));

        list.add(new KerdesModel("Mi az angol szó a \"ház\"-ra?", "house", "apartment", "building", "street", "house"));

        list.add(new KerdesModel("Fordítsd le a \"szék\"-et angolra.", "chair", "table", "bed", "sofa", "chair"));

        list.add(new KerdesModel("Mi a jelentése a \"folyó\"-nak angolul?", "river", "lake", "sea", "ocean", "river"));
    }

}
