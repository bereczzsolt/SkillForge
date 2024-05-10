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
import com.example.skillforge.Tantargyak.Aktivitik.IrodalomActivity;
import com.example.skillforge.Tantargyak.Model.KerdesModel;

import com.example.skillforge.databinding.ActivityKerdesekBinding;

import java.util.ArrayList;


public class IrodalomKerdesek extends AppCompatActivity {


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
                case "Petőfi Sándor":
                    Tema_petofi();
                    break;
                case "Jókai Mór":
                    Tema_Jokai();
                    break;
                case "Ady Endre":
                    Tema_Ady();
                    break;
                case "Kosztolányi Dezső":
                    Tema_Kosztolanyi();
                    break;
                case "Móricz Zsigmond":
                    Tema_Moricz();
                    break;
                case "Karinthy Frigyes":
                    Tema_Karinthy();
                    break;
                case "Szabó Magda ":
                    Tema_Szabo();
                    break;
                default:
                    // Ismeretlen téma kezelése
                    break;
            }
        }


        playAnimation(binding.feltettkerdes, 0, list.get(helyzet).getKerdes());

        for (int i = 0; i < 4; i++) {

            binding.Valaszlehetosegek.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkAnswer((Button) view);


                }

            });

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

                    Intent intent = new Intent(IrodalomKerdesek.this, PontActivity.class);
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
                Dialog dialog = new Dialog(IrodalomKerdesek.this);
                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.lejartido);
                dialog.findViewById(R.id.lejartidogomb).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(IrodalomKerdesek.this, IrodalomActivity.class);
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
    private void Tema_Szabo() {
        list.add(new KerdesModel("Mikor született Szabó Magda?",
                "1917-ben", "1920-ban", "1923-ban", "1925-ben", "1917-ben"));
        list.add(new KerdesModel("Melyik volt Szabó Magda első regénye?",
                "Freskó", "Abigél", "Az ajtó", "Régimódi történet", "Freskó"));
        list.add(new KerdesModel("Melyik művéért kapott Szabó Magda Kossuth-díjat?",
                "Az ajtó", "Abigél", "Régimódi történet", "Für Elise", "Für Elise"));
        list.add(new KerdesModel("Mi volt Szabó Magda egyik legismertebb regénye, amelyből később film is készült?",
                "Az ajtó", "Abigél", "Freskó", "Régimódi történet", "Az ajtó"));
        list.add(new KerdesModel("Melyik Szabó Magda regény alapján készült Jancsó Miklós azonos című filmje?",
                "Az ajtó", "Abigél", "Freskó", "Régimódi történet", "Az ajtó"));
        list.add(new KerdesModel("Melyik Szabó Magda műve játszódik egy budapesti gimnáziumban az 1950-es években?",
                "Az őz", "Régimódi történet", "Abigél", "Az ajtó", "Abigél"));
        list.add(new KerdesModel("Melyik Szabó Magda regénye szól egy házasságról, amely a világháború előtt és után zajlik?",
                "Für Elise", "Az ajtó", "Régimódi történet", "Abigél", "Für Elise"));
        list.add(new KerdesModel("Melyik Szabó Magda művének címe a következő: 'A pillanat éve'?",
                "Az ajtó", "Abigél", "Az őz", "A pillanat éve", "A pillanat éve"));
        list.add(new KerdesModel("Melyik Szabó Magda regényében követjük bevezetődését Bözsi nénihez, aki a bárt mondja el a történetet?",
                "Az őz", "Az ajtó", "Für Elise", "Abigél", "Für Elise"));
        list.add(new KerdesModel("Melyik Szabó Magda műveiben fedezhetjük fel az anyaság, a női identitás és az önérvényesítés témáit?",
                "Freskó", "Az ajtó", "Régimódi történet", "Abigél", "Abigél"));
        list.add(new KerdesModel("Melyik Szabó Magda regény története köré épül a kérdés, hogy ki a felelős egy csecsemő haláláért?",
                "Régimódi történet", "Az ajtó", "Für Elise", "Abigél", "Régimódi történet"));
        list.add(new KerdesModel("Melyik Szabó Magda műve egy házasság meghatározó pillanatairól szól?",
                "Az ajtó", "Az őz", "Abigél", "Für Elise", "Az ajtó"));
        list.add(new KerdesModel("Melyik Szabó Magda regény főszereplője Zsófi, aki a lányát a nevelőintézetbe helyezi?",
                "Az ajtó", "Az őz", "Für Elise", "Régimódi történet", "Az ajtó"));
        list.add(new KerdesModel("Melyik Szabó Magda regényében találkozhatunk a Nagy Lajos gimnáziummal?",
                "Abigél", "Az őz", "Az ajtó", "Für Elise", "Abigél"));
        list.add(new KerdesModel("Melyik Szabó Magda regényében találkozhatunk a Füst Milán Általános iskolával?",
                "Régimódi történet", "Az ajtó", "Für Elise", "Az őz", "Régimódi történet"));
    }
    private void Tema_Karinthy() {
        list.add(new KerdesModel("Mikor született Karinthy Frigyes?",
                "1887-ben", "1892-ben", "1894-ben", "1900-ban", "1887-ben"));
        list.add(new KerdesModel("Melyik volt Karinthy Frigyes első regénye?",
                "Tanár úr kérem", "Utazás Faremidóba", "A cirkusz", "Capillária", "Tanár úr kérem"));
        list.add(new KerdesModel("Melyik művéért kapott Karinthy Frigyes Baumgarten-díjat?",
                "Utazás Faremidóba", "Capillária", "A cirkusz", "Tanár úr kérem", "Utazás Faremidóba"));
        list.add(new KerdesModel("Mi volt Karinthy Frigyes egyik legismertebb műve, amelyből később film is készült?",
                "Tanár úr kérem", "Utazás Faremidóba", "Capillária", "A cirkusz", "Tanár úr kérem"));
        list.add(new KerdesModel("Melyik Karinthy Frigyes mű alapján készült Dargay Attila azonos című animációs filmje?",
                "Utazás Faremidóba", "Tanár úr kérem", "Capillária", "A cirkusz", "Utazás Faremidóba"));
        list.add(new KerdesModel("Melyik Karinthy Frigyes műve játszódik egy különös szigeten, ahol különös állatok élnek?",
                "Utazás Faremidóba", "A cirkusz", "Tanár úr kérem", "Capillária", "Utazás Faremidóba"));
        list.add(new KerdesModel("Melyik Karinthy Frigyes regénye szól egy budapesti gimnáziumról és az ott tanuló diákokról?",
                "Tanár úr kérem", "A cirkusz", "Utazás Faremidóba", "Capillária", "Tanár úr kérem"));
        list.add(new KerdesModel("Melyik Karinthy Frigyes művének címe a következő: 'Útkeresés otthon és vidéken'?",
                "A cirkusz", "Utazás Faremidóba", "Tanár úr kérem", "Capillária", "A cirkusz"));
        list.add(new KerdesModel("Melyik Karinthy Frigyes regényében találkozhatunk az álarcos költő, Léda, és a híres cirkusz arénájával?",
                "A cirkusz", "Utazás Faremidóba", "Tanár úr kérem", "Capillária", "A cirkusz"));
        list.add(new KerdesModel("Melyik Karinthy Frigyes műveiben találkozhatunk az abszurd humorral és a filozofikus gondolatokkal?",
                "Capillária", "A cirkusz", "Utazás Faremidóba", "Tanár úr kérem", "Capillária"));
        list.add(new KerdesModel("Melyik Karinthy Frigyes regény története köré épül az emberi létezés és a halál filozofikus kérdése?",
                "Capillária", "A cirkusz", "Utazás Faremidóba", "Tanár úr kérem", "Capillária"));
        list.add(new KerdesModel("Melyik Karinthy Frigyes műve egy társasjáték különös szabályaival?",
                "Capillária", "A cirkusz", "Utazás Faremidóba", "Tanár úr kérem", "Capillária"));
        list.add(new KerdesModel("Melyik Karinthy Frigyes regény főszereplője szereplője keresi a világ legkülönösebb betegségét?",
                "Capillária", "A cirkusz", "Utazás Faremidóba", "Tanár úr kérem", "Capillária"));
        list.add(new KerdesModel("Melyik Karinthy Frigyes regényében találkozhatunk a két alapvető társasjátékkal, a shatranjjal és a goval?",
                "Capillária", "A cirkusz", "Utazás Faremidóba", "Tanár úr kérem", "Capillária"));
    }
    private void Tema_Moricz() {
        list.add(new KerdesModel("Mikor született Móricz Zsigmond?",
                "1879-ben", "1880-ban", "1881-ben", "1882-ben", "1879-ben"));
        list.add(new KerdesModel("Melyik volt Móricz Zsigmond első regénye?",
                "Rokonok", "Légy jó mindhalálig", "Sárarany", "Egy polgár vallomásai", "Rokonok"));
        list.add(new KerdesModel("Melyik Móricz Zsigmond műve alapján készült Jancsó Miklós azonos című filmje?",
                "Sárarany", "Légy jó mindhalálig", "Rokonok", "Egy polgár vallomásai", "Légy jó mindhalálig"));
        list.add(new KerdesModel("Melyik Móricz Zsigmond regénye játszódik a 19. század végén, ahol a magyar alföld parasztvilágát mutatja be?",
                "Rokonok", "Sárarany", "Légy jó mindhalálig", "Egy polgár vallomásai", "Rokonok"));
        list.add(new KerdesModel("Melyik Móricz Zsigmond művében találkozhatunk a főszereplővel, Gábor átka karakterrel?",
                "Sárarany", "Rokonok", "Légy jó mindhalálig", "Egy polgár vallomásai", "Sárarany"));
        list.add(new KerdesModel("Melyik Móricz Zsigmond regényében találkozhatunk egy polgár hiteles önéletrajzával?",
                "Egy polgár vallomásai", "Sárarany", "Rokonok", "Légy jó mindhalálig", "Egy polgár vallomásai"));
        list.add(new KerdesModel("Melyik Móricz Zsigmond művében találkozhatunk egy fiatal fiúvel, aki az anyagi világból kilépve kereste önmagát?",
                "Rokonok", "Egy polgár vallomásai", "Légy jó mindhalálig", "Sárarany", "Légy jó mindhalálig"));
        list.add(new KerdesModel("Melyik Móricz Zsigmond regénye szól egy török kori történelmi téma feldolgozásáról?",
                "Egy polgár vallomásai", "Sárarany", "Légy jó mindhalálig", "Rokonok", "Egy polgár vallomásai"));
        list.add(new KerdesModel("Melyik Móricz Zsigmond regényében találkozhatunk a hős Kaposvárott, akinek az életébe belekontárkodik egy titokzatos idegen?",
                "Sárarany", "Rokonok", "Légy jó mindhalálig", "Egy polgár vallomásai", "Sárarany"));
        list.add(new KerdesModel("Melyik Móricz Zsigmond művében találkozhatunk a főszereplővel, aki lóversenyen gazdagodik meg?",
                "Légy jó mindhalálig", "Rokonok", "Sárarany", "Egy polgár vallomásai", "Légy jó mindhalálig"));
        list.add(new KerdesModel("Melyik Móricz Zsigmond regényében találkozhatunk egy parasztcsaláddal, akik nagyratörő álmokat dédelgetnek?",
                "Légy jó mindhalálig", "Rokonok", "Sárarany", "Egy polgár vallomásai", "Légy jó mindhalálig"));
        list.add(new KerdesModel("Melyik Móricz Zsigmond művében találkozhatunk a főszereplővel, aki a visszaélések, becstelenségek árnyékában próbál meg boldogulni?",
                "Egy polgár vallomásai", "Rokonok", "Sárarany", "Légy jó mindhalálig", "Egy polgár vallomásai"));
        list.add(new KerdesModel("Melyik Móricz Zsigmond regényében találkozhatunk a főszereplővel, aki a bűnösök megbüntetésére vállalkozik?",
                "Egy polgár vallomásai", "Rokonok", "Sárarany", "Légy jó mindhalálig", "Egy polgár vallomásai"));
        list.add(new KerdesModel("Melyik Móricz Zsigmond művében találkozhatunk a főszereplővel, aki a végletekig becsületes és tisztességes?",
                "Egy polgár vallomásai", "Rokonok", "Sárarany", "Légy jó mindhalálig", "Egy polgár vallomásai"));
        list.add(new KerdesModel("Melyik Moricz Zsigmond regényében találkozhatunk a főszereplővel, aki egy nagyvárosban, Budapest utcáin keresi boldogságát?",
                "Egy polgár vallomásai", "Rokonok", "Sárarany", "Légy jó mindhalálig", "Egy polgár vallomásai"));

    }

    private void Tema_Kosztolanyi() {
        list.add(new KerdesModel("Mikor született Kosztolányi Dezső?",
                "1885-ben", "1887-ben", "1889-ben", "1891-ben", "1885-ben"));
        list.add(new KerdesModel("Melyik Kosztolányi Dezső művében találkozhatunk a híres hosszú monológjával, melyben főszereplőjének lelki vívódásait követhetjük nyomon?",
                "Esti Kornél", "Pacsirta", "Nero", "Aranka", "Esti Kornél"));
        list.add(new KerdesModel("Melyik Kosztolányi Dezső műve a legismertebb gyermekversek közé tartozik, melyeket édesanyjának írt?",
                "Lengyelország", "Halottak napi nóták", "Esti Kornél", "Tótágas", "Lengyelország"));
        list.add(new KerdesModel("Melyik Kosztolányi Dezső regényében követhetjük nyomon az első világháború kegyetlen valóságát, valamint a háború előtti időszak pesti bohém életét?",
                "Pacsirta", "Aranka", "Esti Kornél", "Nero", "Pacsirta"));
        list.add(new KerdesModel("Melyik Kosztolányi Dezső művében olvashatjuk az „Isten és ember” című verset?",
                "Aranka", "Esti Kornél", "Pacsirta", "Nero", "Aranka"));
        list.add(new KerdesModel("Melyik Kosztolányi Dezső regényében követhetjük a főszereplő, aki nem éli túl a századfordulót, életét és halálát?",
                "Nero", "Aranka", "Esti Kornél", "Pacsirta", "Nero"));
        list.add(new KerdesModel("Melyik Kosztolányi Dezső művében találkozhatunk az énekes lúddal és seregélygel, akik elindulnak, hogy megtalálják az élet értelmét?",
                "Esti Kornél", "Pacsirta", "Nero", "Halottak napi nóták", "Esti Kornél"));
        list.add(new KerdesModel("Melyik Kosztolányi Dezső művében olvashatjuk az „Esti Dal” című verset?",
                "Pacsirta", "Esti Kornél", "Nero", "Halottak napi nóták", "Pacsirta"));
        list.add(new KerdesModel("Melyik Kosztolányi Dezső regényében követhetjük egy ifjú festő sorsát, aki a nagyvilágba indul, hogy híres művész legyen?",
                "Halottak napi nóták", "Esti Kornél", "Nero", "Pacsirta", "Halottak napi nóták"));
        list.add(new KerdesModel("Melyik Kosztolányi Dezső művében találkozhatunk egy kisfiúval, aki mindenre kíváncsi és mindent tudni akar?",
                "Tótágas", "Pacsirta", "Esti Kornél", "Halottak napi nóták", "Tótágas"));
        list.add(new KerdesModel("Melyik Kosztolányi Dezső művében találkozhatunk egy éjszakai fuvarral, amelyen egy fiatalasszony és egy taxis beszélgetése áll a középpontban?",
                "Aranka", "Nero", "Esti Kornél", "Halottak napi nóták", "Aranka"));
        list.add(new KerdesModel("Melyik Kosztolányi Dezső regényében olvashatjuk a főszereplő életének teljes krónikáját, beleértve az egész életében hozott döntéseket és azok következményeit?",
                "Aranka", "Nero", "Pacsirta", "Esti Kornél", "Aranka"));
        list.add(new KerdesModel("Melyik Kosztolányi Dezső művében találkozhatunk egy fiatalemberrel, aki váratlanul meghal és utazásra indul az éj sötétjében?",
                "Esti Kornél", "Pacsirta", "Aranka", "Nero", "Esti Kornél"));
        list.add(new KerdesModel("Melyik Kosztolányi Dezső regényében olvashatjuk a főszereplő, a híres költő összetört szívét, és élete utolsó pillanatait?",
                "Pacsirta", "Nero", "Esti Kornél", "Aranka", "Pacsirta"));
        list.add(new KerdesModel("Melyik Kosztolányi Dezső regényében olvashatjuk a főszereplő, aki egy budapesti csendőrőrsön dolgozik, és egy éjszaka során váratlan események sora zavarja meg a nyugalmat?",
                "Skylark", "Esti Kornél", "Pacsirta", "Nero", "Skylark"));
    }

    private void Tema_Ady() {
        list.add(new KerdesModel("Mely évben született Ady Endre?",
                "1877-ben", "1887-ben", "1897-ben", "1907-ben", "1877-ben"));
        list.add(new KerdesModel("Mi volt Ady Endre születési neve?",
                "Ady András", "Ady Mihály", "Ady Endre", "Ady Gyula", "Ady András"));
        list.add(new KerdesModel("Melyik Ady Endre költői művében található a \"Zajos örömök örök tava\" kifejezés?",
                "Uramisten", "Az Illés szekerén", "Az én regényem", "Az elveszett alkotmány", "Uramisten"));
        list.add(new KerdesModel("Melyik Ady Endre költeménye egyúttal nevét is viseli?",
                "Az Illés szekerén", "Uramisten", "Az én regényem", "Az elveszett alkotmány", "Az Illés szekerén"));
        list.add(new KerdesModel("Melyik Ady Endre versében található a \"jajgatnak az ezer meg ezer élet-öregek\" sor?",
                "Az elveszett alkotmány", "Az Illés szekerén", "Uramisten", "Az én regényem", "Az elveszett alkotmány"));
        list.add(new KerdesModel("Melyik Ady Endre költeményében szerepel a \"szívemet mindig hazugság terheli\" sor?",
                "Az én regényem", "Az elveszett alkotmány", "Az Illés szekerén", "Uramisten", "Az én regényem"));
        list.add(new KerdesModel("Melyik Ady Endre műve jelent meg 1907-ben, és sokak szerint a modern magyar költészet kezdetét jelenti?",
                "Új versek", "Versek", "Az én regényem", "Az elveszett alkotmány", "Új versek"));
        list.add(new KerdesModel("Melyik Ady Endre versében olvasható a „nagy fehér folyó, a férfiszív” sor?",
                "Az én regényem", "Az elveszett alkotmány", "Az Illés szekerén", "Uramisten", "Az én regényem"));
        list.add(new KerdesModel("Melyik Ady Endre költeményében található a „hétköznapok királya” kifejezés?",
                "Az én regényem", "Az elveszett alkotmány", "Az Illés szekerén", "Uramisten", "Az én regényem"));
        list.add(new KerdesModel("Melyik Ady Endre műve adott nevet a „kék könyv”-nek?",
                "Az elveszett alkotmány", "Az én regényem", "Az Illés szekerén", "Uramisten", "Az elveszett alkotmány"));
        list.add(new KerdesModel("Melyik Ady Endre versében olvasható a „nem tudom, hol vagy, de mindenütt vagyok” sor?",
                "Az elveszett alkotmány", "Az én regényem", "Az Illés szekerén", "Uramisten", "Az elveszett alkotmány"));
        list.add(new KerdesModel("Melyik Ady Endre költeményében található a „Nem én lőttem elsőnek” sor?",
                "Az elveszett alkotmány", "Az én regényem", "Az Illés szekerén", "Uramisten", "Az elveszett alkotmány"));
        list.add(new KerdesModel("Melyik Ady Endre versében olvasható a „fűzfák susognak az út mentén” sor?",
                "Az elveszett alkotmány", "Az én regényem", "Az Illés szekerén", "Uramisten", "Az elveszett alkotmány"));
        list.add(new KerdesModel("Melyik Ady Endre költeményében található a „győzelem, mely alattad csukódik be minden nagy ablak” sor?",
                "Az elveszett alkotmány", "Az én regényem", "Az Illés szekerén", "Uramisten", "Az elveszett alkotmány"));
        list.add(new KerdesModel("Melyik Ady Endre versében található a „fekete gyászfolt az alkonyi égen” sor?",
                "Az elveszett alkotmány", "Az én regényem", "Az Illés szekerén", "Uramisten", "Az elveszett alkotmány"));

    }
    private void Tema_Jokai() {
        list.add(new KerdesModel("Mely évben született Jókai Mór?",
                "1825-ben", "1835-ben", "1845-ben", "1855-ben", "1825-ben"));
        list.add(new KerdesModel("Melyik magyar író volt az 1848-as forradalom és szabadságharc alatt a legnépszerűbb szerző?",
                "Jókai Mór", "Arany János", "Petőfi Sándor", "Vörösmarty Mihály", "Jókai Mór"));
        list.add(new KerdesModel("Melyik Jókai Mór regényében találkozhatunk a Kalapos úrral?",
                "Az arany ember", "Egy magyar nábob", "Fekete gyémántok", "A kőszívű ember fiai", "Az arany ember"));
        list.add(new KerdesModel("Melyik Jókai Mór regényében találkozhatunk Baradlay Károllyal és Ráday Mihállyal?",
                "Az arany ember", "Egy magyar nábob", "Fekete gyémántok", "A kőszívű ember fiai", "Az arany ember"));
        list.add(new KerdesModel("Melyik Jókai Mór regényének főszereplője a címben is szerepel, és \"doboz nélküli kalapos\"?",
                "A dobzse", "Egy magyar nábob", "Fekete gyémántok", "A kőszívű ember fiai", "A dobzse"));
        list.add(new KerdesModel("Melyik Jókai Mór regényében találkozhatunk egy álmokkal és víziókkal teli kastéllyal, és az azon belüli összeesküvésekkel?",
                "Fekete gyémántok", "Az arany ember", "Egy magyar nábob", "A kőszívű ember fiai", "Fekete gyémántok"));
        list.add(new KerdesModel("Melyik Jókai Mór regényében találkozhatunk a „vöröskirályné” által vezetett asszonyi csapatokkal?",
                "Az arany ember", "Fekete gyémántok", "Egy magyar nábob", "A kőszívű ember fiai", "Az arany ember"));
        list.add(new KerdesModel("Melyik Jókai Mór regényében találkozhatunk egy vörösruhás rejtélyes asszonnyal, aki a végsőkig titkolja kilétét?",
                "A kőszívű ember fiai", "Az arany ember", "Fekete gyémántok", "Egy magyar nábob", "A kőszívű ember fiai"));
        list.add(new KerdesModel("Melyik Jókai Mór regényében játszódik a történet az 1848-49-es forradalom idején, és egy ifjú lovag fogságának és kalandjainak leírásával indul?",
                "A kőszívű ember fiai", "Az arany ember", "Fekete gyémántok", "Egy magyar nábob", "A kőszívű ember fiai"));
        list.add(new KerdesModel("Melyik Jókai Mór regényében találkozhatunk egy szegény, de jószívű ifjúval, aki felfedez egy hatalmas kincset, de annak megszerzése nem megy könnyen?",
                "Az arany ember", "Fekete gyémántok", "Egy magyar nábob", "A kőszívű ember fiai", "Az arany ember"));
        list.add(new KerdesModel("Melyik Jókai Mór regényében játszódik a történet egy betyárbanda és egy árva fiú kalandjainak megírásával?",
                "Egy magyar nábob", "Az arany ember", "Fekete gyémántok", "A kőszívű ember fiai", "Egy magyar nábob"));
        list.add(new KerdesModel("Melyik Jókai Mór regényében találkozhatunk egy nyomorult nemesemberrel, aki feleségével és leányával együtt egy egész falu lakosságát terhesíti meg a félelemmel?",
                "Az arany ember", "Fekete gyémántok", "Egy magyar nábob", "A kőszívű ember fiai", "Fekete gyémántok"));
        list.add(new KerdesModel("Melyik Jókai Mór regényében találkozhatunk egy jóakaró özvegyasszonnyal, aki meghaladja korát, és az igazságosságért és a jó cselekedetekért küzd?",
                "Az arany ember", "Fekete gyémántok", "Egy magyar nábob", "A kőszívű ember fiai", "Az arany ember"));
        list.add(new KerdesModel("Melyik Jókai Mór regényében találkozhatunk egy fiatal nővel, aki egy titokzatos kék szemű férfitól kapott levelet visz vissza a valóságba?",
                "Az arany ember", "Fekete gyémántok", "Egy magyar nábob", "A kőszívű ember fiai", "Az arany ember"));
        list.add(new KerdesModel("Melyik Jókai Mór regényében találkozhatunk egy ifjú nővel, aki egy aranyalakot keresve vesz részt egy titokzatos kalandban?",
                "Fekete gyémántok", "Az arany ember", "Egy magyar nábob", "A kőszívű ember fiai", "Fekete gyémántok"));

    }

    private void Tema_petofi() {
        list.add(new KerdesModel("Mely évben született Petőfi Sándor?",
                "1823-ban", "1830-ban", "1840-ben", "1823-ban", "1823-ban"));
        list.add(new KerdesModel("Melyik Petőfi Sándor verse lett a magyar nemzeti himnusz?",
                "Nemzeti dal", "János vitéz", "Talpra magyar", "Apa Lajos", "Talpra magyar"));
        list.add(new KerdesModel("Melyik évben tűnt el nyomtalanul Petőfi Sándor?",
                "1849-ben", "1850-ben", "1856-ban", "1849-ben", "1849-ben"));
        list.add(new KerdesModel("Melyik évben nyilvánították meg Petőfi Sándor költői műveit nemzeti kincsnek?",
                "1848-ban", "1850-ben", "1867-ben", "1848-ban", "1848-ban"));
        list.add(new KerdesModel("Melyik Petőfi Sándor verse kezdődik a következő sorokkal: „A Tiszán innen, Dunán túl…”?",
                "Az apostol", "Nemzeti dal", "János vitéz", "Talpra magyar", "Nemzeti dal"));
        list.add(new KerdesModel("Melyik Petőfi Sándor versében található a „Szép vagy, mint egy gyöngyvirág…” sor?",
                "Szeptember végén", "A helység kalapácsa", "A párbaj", "Az apostol", "Szeptember végén"));
        list.add(new KerdesModel("Melyik Petőfi Sándor versében található a „Szabadság, szerelem…” sor?",
                "Talpra magyar", "Nemzeti dal", "A gavallér", "A helység kalapácsa", "Nemzeti dal"));
        list.add(new KerdesModel("Melyik Petőfi Sándor verse kezdődik a következő sorokkal: „Esténként, ha bolyongok…\"",
                "Esti dal", "János vitéz", "Nemzeti dal", "Szeptember végén", "Esti dal"));
        list.add(new KerdesModel("Melyik Petőfi Sándor verse kezdődik a következő sorokkal: „Szép vagy, mint a hajnalcsillag…”?",
                "A gavallér", "Nemzeti dal", "Talpra magyar", "Az apostol", "A gavallér"));
        list.add(new KerdesModel("Melyik Petőfi Sándor versében található a „Szemben a nap alatt…” sor?",
                "Az apostol", "Szeptember végén", "A helység kalapácsa", "Az ifjúság kora", "Szeptember végén"));
        list.add(new KerdesModel("Melyik Petőfi Sándor verse kezdődik a következő sorokkal: „Rózsám, rózsám…”?",
                "Az ifjúság kora", "Az apostol", "A gavallér", "A helység kalapácsa", "Az ifjúság kora"));
        list.add(new KerdesModel("Melyik Petőfi Sándor versében található a „Fönn a magas hegyeken…” sor?",
                "Az ifjúság kora", "A gavallér", "Az apostol", "Szeptember végén", "Az ifjúság kora"));
        list.add(new KerdesModel("Melyik Petőfi Sándor verse kezdődik a következő sorokkal: „Mikor a madarak…”?",
                "A helység kalapácsa", "Szeptember végén", "Az apostol", "Az ifjúság kora", "A helység kalapácsa"));
        list.add(new KerdesModel("Melyik Petőfi Sándor versében található a „Hogyha rózsát adnál…” sor?",
                "Az apostol", "Az ifjúság kora", "Szeptember végén", "A gavallér", "Az apostol"));
        list.add(new KerdesModel("Melyik Petőfi Sándor verse kezdődik a következő sorokkal: „Szomorú vagyok, mint a tavaszi eső…”?",
                "Nemzeti dal", "Szeptember végén", "Az ifjúság kora", "Az apostol", "Szeptember végén"));
    }
}