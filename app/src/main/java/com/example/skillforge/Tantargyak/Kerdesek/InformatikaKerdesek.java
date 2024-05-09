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
import com.example.skillforge.Tantargyak.Aktivitik.InformatikaActivity;
import com.example.skillforge.Tantargyak.Model.KerdesModel;

import com.example.skillforge.databinding.ActivityKerdesekBinding;

import java.util.ArrayList;


public class InformatikaKerdesek extends AppCompatActivity {
    ArrayList<KerdesModel> Infolist = new ArrayList<>();
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
        if (temanev.equals("TT")) {

            Tema_Hunok();

        } else if (temanev.equals("Magyar államalapítás")) {
            Tema_Magyar_allamalapitas();
        }else if (temanev.equals("Árpád-ház")) {
            Tema_Arpad_Haz();
        }else if (temanev.equals("Hunyadiak")) {
            Tema_Hunyadiak();
        }else if (temanev.equals("1956-os forradalom és szabadságharc")) {
            Tema_1956();
        }else if (temanev.equals("Első Világháború")) {
            Tema_Elso_VH();
        }else if (temanev.equals("Második Világháború")) {
            Tema_Masodik_VH();
        }






        for (int i = 0; i < 4; i++) {

            binding.Valaszlehetosegek.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkAnswer((Button) view);


                }

            });

            playAnimation(binding.feltettkerdes, 0, Infolist.get(helyzet).getKerdes());
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
                if (helyzet == Infolist.size()) {

                    Intent intent = new Intent(InformatikaKerdesek.this, PontActivity.class);
                    intent.putExtra("pont", pont);
                    intent.putExtra("osszes", Infolist.size());
                    startActivity(intent);
                    finish();
                    return;
                }
                szamlalo = 0;
                playAnimation(binding.feltettkerdes,0, Infolist.get(helyzet).getKerdes());



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
                Dialog dialog = new Dialog(InformatikaKerdesek.this);
                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.lejartido);
                dialog.findViewById(R.id.lejartidogomb).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(InformatikaKerdesek.this, InformatikaActivity.class);
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
                                option = Infolist.get(helyzet).getOpcio1();
                            } else if (szamlalo == 1) {
                                option = Infolist.get(helyzet).getOpcio2();
                            } else if (szamlalo == 2) {
                                option = Infolist.get(helyzet).getOpcio3();
                            } else if (szamlalo == 3) {
                                option = Infolist.get(helyzet).getOpcio4();
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
                                binding.kerdesekszama.setText(helyzet + 1 + "/" + Infolist.size());

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
        if (valasztottOpcio.getText().toString().equals(Infolist.get(helyzet).getJovalasz())) {
            // Ha a válasz helyes, növeljük a pontszámot és beállítjuk a megfelelő háttérszínt
            pont++;
            valasztottOpcio.setBackgroundResource(R.drawable.jo_valasz);
        } else {
            // Ha a válasz helytelen, beállítjuk a megfelelő háttérszínt mindkét gombnak
            valasztottOpcio.setBackgroundResource(R.drawable.rossz_valasz);
            Button helyes = (Button) binding.Valaszlehetosegek.findViewWithTag(Infolist.get(helyzet).getJovalasz());
            if (helyes != null) {
                helyes.setBackgroundResource(R.drawable.jo_valasz); // A helyes válasz jelzése
            }
        }
    }
    private void Tema_Masodik_VH() {
        Infolist.add(new KerdesModel("Mikor kezdődött a második világháború?",
                "1939-ben", "1941-ben", "1943-ban", "1945-ben", "1939-ben"));
        Infolist.add(new KerdesModel("Mi volt az Egyesült Államok hivatalos bekapcsolódási dátuma a második világháborúba?",
                "1941. december 7.", "1942. szeptember 1.", "1944. június 6.", "1945. május 8.", "1941. december 7."));
        Infolist.add(new KerdesModel("Melyik évben zajlott a d-day, a normandiai partraszállás?",
                "1944-ben", "1942-ben", "1943-ban", "1945-ben", "1944-ben"));
        Infolist.add(new KerdesModel("Ki volt az Egyesült Királyság miniszterelnöke a második világháború alatt?",
                "Winston Churchill", "Neville Chamberlain", "Clement Attlee", "Stanley Baldwin", "Winston Churchill"));
        Infolist.add(new KerdesModel("Melyik évben esett el Berlin a második világháborúban?",
                "1945-ben", "1942-ben", "1944-ben", "1946-ban", "1945-ben"));
        Infolist.add(new KerdesModel("Mi volt a második világháború leghosszabb csatája?",
                "A sztálingrádi csata", "A normandiai partraszállás", "A leningrádi blokád", "A kínai harctér", "A sztálingrádi csata"));
        Infolist.add(new KerdesModel("Ki volt a harmadik birodalom vezetője a második világháború alatt?",
                "Adolf Hitler", "Joseph Stalin", "Benito Mussolini", "Hirohito", "Adolf Hitler"));
        Infolist.add(new KerdesModel("Melyik évben kapott Truman elnök jóváhagyást az atombomba ledobására?",
                "1945-ben", "1943-ban", "1944-ben", "1946-ban", "1945-ben"));
        Infolist.add(new KerdesModel("Melyik évben kapitulált a japán hadsereg a második világháborúban?",
                "1945-ben", "1942-ben", "1944-ben", "1946-ban", "1945-ben"));
        Infolist.add(new KerdesModel("Mi volt a második világháború keleti frontjának legnagyobb csatája?",
                "Kurszki csata", "Leningrádi csata", "Szovjet-offenzíva", "El-Alamein csata", "Kurszki csata"));
        Infolist.add(new KerdesModel("Melyik évben írták alá a második világháború véget jelentő békeszerződést?",
                "1945-ben", "1942-ben", "1944-ben", "1946-ban", "1945-ben"));
        Infolist.add(new KerdesModel("Melyik évben tört ki a második világháború csendesebb időszaka, ismert mint a Phoney War?",
                "1939-ben", "1940-ben", "1941-ben", "1938-ban", "1939-ben"));
        Infolist.add(new KerdesModel("Ki volt az Amerikai Egyesült Államok elnöke a második világháború alatt?",
                "Franklin D. Roosevelt", "Harry S. Truman", "Dwight D. Eisenhower", "John F. Kennedy", "Franklin D. Roosevelt"));
        Infolist.add(new KerdesModel("Melyik évben tört ki a második világháború európai hadszínterén az éles harcok sorozata?",
                "1939-ben", "1940-ben", "1941-ben", "1942-ben", "1939-ben"));
        Infolist.add(new KerdesModel("Hány évig tartott a második világháború?",
                "6 év", "5 év", "4 év", "7 év", "6 év"));

    }

    private void Tema_Elso_VH() {

        Infolist.add(new KerdesModel("Mikor kezdődött az első világháború?",
                "1914-ben", "1916-ban", "1918-ban", "1920-ban", "1914-ben"));
        Infolist.add(new KerdesModel("Mi volt az első világháború kirobbanásának közvetlen oka?",
                "Franciaország inváziója Belgrádba", "Osztrák-Magyar Monarchia hadüzenete Szerbiának",
                "Az Egyesült Államok csatlakozása a háborúhoz", "Németország támadása Belgiumon keresztül",
                "Osztrák-Magyar Monarchia hadüzenete Szerbiának"));
        Infolist.add(new KerdesModel("Mi volt az első világháború egyik hírhedt csatatere Franciaország és Németország között?",
                "Verdun", "Somme", "Ypres", "Marne", "Verdun"));
        Infolist.add(new KerdesModel("Melyik ország lépett ki először az első világháborúból 1917-ben?",
                "Németország", "Ausztria-Magyar Monarchia", "Oroszország", "Olaszország", "Oroszország"));
        Infolist.add(new KerdesModel("Ki volt Németország császára az első világháború idején?",
                "II. Vilmos", "I. Ferenc József", "II. Miklós", "V. György", "II. Vilmos"));
        Infolist.add(new KerdesModel("Melyik évben tört ki az első világháborúban az ún. Osztrák-Szerb háború?",
                "1914-ben", "1916-ban", "1918-ban", "1920-ban", "1914-ben"));
        Infolist.add(new KerdesModel("Mi volt az első világháború legnagyobb tengeralattjáró-hadműveletének neve?",
                "Zimmermann-ügy", "Schlieffen-terv", "Lusitania incidens", "Gallipoli-csatlakozás", "Zimmermann-ügy"));
        Infolist.add(new KerdesModel("Melyik évben csatlakozott az Egyesült Államok az első világháborúhoz?",
                "1914-ben", "1916-ban", "1917-ben", "1918-ban", "1917-ben"));
        Infolist.add(new KerdesModel("Melyik ország vezetője írta alá a Brest-Litovszki békeszerződést Németországgal?",
                "Oroszország", "Németország", "Ausztria-Magyar Monarchia", "Törökország", "Oroszország"));
        Infolist.add(new KerdesModel("Hogyan nevezték az első világháború során azokat az árkokat, amelyekben a katonák harcoltak?",
                "Tűzvonal", "Körbevett városok", "Hídfőállások", "Névtelen sírok", "Tűzvonal"));
        Infolist.add(new KerdesModel("Melyik évben tört ki az első világháborúban az ún. Tengeri csata Jütlandnál?",
                "1914-ben", "1916-ban", "1918-ban", "1920-ban", "1916-ban"));
        Infolist.add(new KerdesModel("Ki volt az első világháborúban a legnagyobb központi hatalom?",
                "Németország", "Osztrák-Magyar Monarchia", "Olaszország", "Törökország", "Németország"));
        Infolist.add(new KerdesModel("Melyik esemény váltotta ki az Egyesült Államok bevonulását az első világháborúba?",
                "Pearl Harbor támadás", "Lusitania elsüllyesztése", "Hindenburg Line áttörése", "Schlieffen-terv végrehajtása",
                "Lusitania elsüllyesztése"));
        Infolist.add(new KerdesModel("Melyik ország lépett ki az első világháborúból a legkésőbb?",
                "Németország", "Ausztria-Magyar Monarchia", "Oroszország", "Olaszország", "Németország"));
        Infolist.add(new KerdesModel("Melyik ország volt az első világháború legnagyobb háromszövetségese?",
                "Németország", "Oroszország", "Franciaország", "Olaszország", "Németország"));
    }

    private void Tema_1956() {

        Infolist.add(new KerdesModel("Mikor és hol kezdődött az 1956-os forradalom?",
                "1956-ban, Budapesten", "1954-ben, Debrecenben", "1958-ban, Szegeden", "1960-ban, Pécsen", "1956-ban, Budapesten"));
        Infolist.add(new KerdesModel("Melyik esemény váltotta ki az 1956-os forradalmat?",
                "A magyar diákok felkelése", "A lengyel szolidaritási mozgalom", "A kommunista vezetés megszorító intézkedései", "Az orosz támadás a magyar határokon", "A kommunista vezetés megszorító intézkedései"));
        Infolist.add(new KerdesModel("Ki volt az 1956-os forradalom egyik vezető alakja?",
                "Imre Nagy", "Mátyás Rákosi", "János Kádár", "Ferenc Münnich", "Imre Nagy"));
        Infolist.add(new KerdesModel("Milyen beavatkozást hajtott végre a Szovjetunió az 1956-os forradalom leverése érdekében?",
                "Katonai intervenciót", "Gazdasági segítséget", "Politikai tárgyalásokat", "Diplomatikus feszültségeket", "Katonai intervenciót"));
        Infolist.add(new KerdesModel("Milyen volt az 1956-os forradalom végeredménye?",
                "A szovjetek visszavonultak, és demokratikus kormány alakult", "A forradalom leverése és a szovjet megszállás", "Teljes függetlenség az országnak", "Köztársasági rendszer visszaállítása", "A forradalom leverése és a szovjet megszállás"));
        Infolist.add(new KerdesModel("Mi volt az 1956-os forradalom jelmondata?",
                "Éljen a szabadság!", "Éljen a kommunizmus!", "Éljen a diktatúra!", "Éljen a béke!", "Éljen a szabadság!"));
        Infolist.add(new KerdesModel("Melyik ország támogatta az 1956-os magyar forradalmat?",
                "Egyesült Államok", "Szovjetunió", "Németország", "Franciaország", "Egyesült Államok"));
        Infolist.add(new KerdesModel("Mi volt az 1956-os forradalom célja a magyarok részéről?",
                "Függetlenség, demokrácia, szabadság", "Abszolutizmus visszaállítása", "Kommunizmus erősítése", "Monarchia visszaállítása", "Függetlenség, demokrácia, szabadság"));
        Infolist.add(new KerdesModel("Hány napon át tartott az 1956-os forradalom?",
                "Körülbelül két hétig", "Három hónapig", "Egy hónapig", "Négy napig", "Körülbelül két hétig"));
        Infolist.add(new KerdesModel("Mi volt az 1956-os forradalom kezdeti célja?",
                "Békés demonstrációk az orosz megszállás ellen", "Teljes függetlenség az országnak", "A kommunista vezetők lemondása", "A szovjetek kivonulása Magyarországról", "Békés demonstrációk az orosz megszállás ellen"));
        Infolist.add(new KerdesModel("Melyik nap kezdődött az 1956-os forradalom?",
                "Október 23.", "November 4.", "December 25.", "Szeptember 15.", "Október 23."));
        Infolist.add(new KerdesModel("Ki volt az 1956-os forradalom során a magyar miniszterelnök?",
                "Imre Nagy", "János Kádár", "Mátyás Rákosi", "Ferenc Münnich", "Imre Nagy"));
        Infolist.add(new KerdesModel("Melyik évben volt az 1956-os forradalom?",
                "1956-ban", "1954-ben", "1958-ban", "1960-ban", "1956-ban"));
        Infolist.add(new KerdesModel("Melyik ország segített Magyarországnak az 1956-os forradalomban?",
                "Nem szállt be segítségül más ország", "Egyesült Államok", "Németország", "Szovjetunió", "Nem szállt be segítségül más ország"));
        Infolist.add(new KerdesModel("Mi volt az 1956-os forradalom fő oka?",
                "Nemzeti szabadságharc a kommunista diktatúra ellen", "A gazdasági válság", "Nemzeti összetartás erősítése", "A politikai elnyomás megszüntetése", "Nemzeti szabadságharc a kommunista diktatúra ellen"));
        Infolist.add(new KerdesModel("Melyik évben oszlatták fel az 1956-os forradalmat a Szovjetunió csapatai?",
                "1956-ban", "1957-ben", "1958-ban", "1959-ben", "1956-ban"));
    }

    private void Tema_Hunyadiak() {
        Infolist.add(new KerdesModel("Ki volt Hunyadi János apja?",
                "Hunyadi Mátyás", "Hunyadi János", "Hunyadi László", "Hunyadi Péter", "Hunyadi Mátyás"));
        Infolist.add(new KerdesModel("Melyik történelmi csata tette híressé Hunyadi Jánost?",
                "Mohaicsata", "Lepantói csata", "Hastingsi csata", "Lengyelországi csata", "Mohaicsata"));
        Infolist.add(new KerdesModel("Melyik evangélikus naptárban ünnepeljük Hunyadi Mátyás napját?",
                "Április 24.", "Február 14.", "Június 12.", "Augusztus 20.", "Április 24."));
        Infolist.add(new KerdesModel("Hol született Hunyadi János?",
                "Hunyad vára", "Buda", "Kolozsvár", "Esztergom", "Hunyad vára"));
        Infolist.add(new KerdesModel("Hány évig volt Hunyadi János nádor?",
                "8 évig", "10 évig", "12 évig", "15 évig", "10 évig"));
        Infolist.add(new KerdesModel("Ki volt Hunyadi Mátyás édesanyja?",
                "Szilágyi Erzsébet", "Katalin", "Ilona", "Hedvig", "Szilágyi Erzsébet"));
        Infolist.add(new KerdesModel("Melyik ország királyaként ismerték el Hunyadi Jánost?",
                "Szerbia", "Horvátország", "Lengyelország", "Magyarország", "Szerbia"));
        Infolist.add(new KerdesModel("Hunyadi János melyik évben hunyt el?",
                "1456-ban", "1460-ban", "1470-ben", "1475-ben", "1456-ban"));
        Infolist.add(new KerdesModel("Hunyadi Mátyás melyik évben lett Magyarország királya?",
                "1458-ban", "1460-ban", "1464-ben", "1467-ben", "1458-ban"));
        Infolist.add(new KerdesModel("Ki volt Hunyadi Mátyás felesége?",
                "Beatrix", "Erzsébet", "Katalin", "Anna", "Beatrix"));
        Infolist.add(new KerdesModel("Melyik város volt Hunyadi János szülőhelye?",
                "Kolozsvár", "Buda", "Esztergom", "Pécs", "Kolozsvár"));
        Infolist.add(new KerdesModel("Melyik csatában szenvedett súlyos vereséget a török sereg Hunyadi Mátyás vezetése alatt?",
                "Kenyérmezei csata", "Mohaicsata", "Belgrádi csata", "Végvár csata", "Mohaicsata"));
        Infolist.add(new KerdesModel("Ki volt Hunyadi László?",
                "Hunyadi Mátyás testvére", "Hunyadi János fia", "Hunyadi Mátyás unokája", "Hunyadi János testvére", "Hunyadi Mátyás testvére"));
        Infolist.add(new KerdesModel("Melyik uralkodó hívta vissza Hunyadi Mátyást a török elleni hadjáratból?",
                "II. Ulászló", "III. Béla", "IV. Lajos", "V. István", "II. Ulászló"));
        Infolist.add(new KerdesModel("Hunyadi János melyik címmel vált ismertté a történelemben?",
                "Hunyadiak fejedelme", "Törökverő János", "Mohaicsata hőse", "Fehér János", "Törökverő János"));

    }

    private void Tema_Arpad_Haz() {
        Infolist.add(new KerdesModel("Ki volt az Árpád-ház alapítója?",
                "Álmos", "Árpád", "Koppány", "Lehel", "Árpád"));
        Infolist.add(new KerdesModel("Melyik Árpád-házi uralkodó neve kapcsolódik a Képes Krónikához?",
                "Szent István", "Szent László", "Könyves Kálmán", "Nagy Lajos", "Szent László"));
        Infolist.add(new KerdesModel("Melyik Árpád-házi uralkodó neve kapcsolódik az Aranybulla kiadásához?",
                "II. András", "II. Béla", "IV. Béla", "I. Károly", "II. Béla"));
        Infolist.add(new KerdesModel("Hány évig uralkodott I. (Szent) István?",
                "38 évig", "41 évig", "44 évig", "47 évig", "41 évig"));
        Infolist.add(new KerdesModel("Melyik Árpád-házi uralkodó neve kapcsolódik a Halotti beszédhez?",
                "Könyves Kálmán", "Szent László", "II. András", "I. (Szent) István", "I. (Szent) István"));
        Infolist.add(new KerdesModel("Hány évig uralkodott II. (Vak) Béla?",
                "20 évig", "22 évig", "24 évig", "26 évig", "20 évig"));
        Infolist.add(new KerdesModel("Melyik Árpád-házi uralkodó uralkodott a leghosszabb ideig?",
                "I. (Szent) István", "II. (Jász) Béla", "III. (Antiochiai) Béla", "II. András", "I. (Szent) István"));
        Infolist.add(new KerdesModel("Melyik Árpád-házi uralkodó volt a legidősebb trónra lépésekor?",
                "Könyves Kálmán", "Szent István", "I. (Szent) László", "II. András", "Szent István"));
        Infolist.add(new KerdesModel("Melyik Árpád-házi uralkodó volt a legfiatalabb trónra lépésekor?",
                "Könyves Kálmán", "II. (Jász) Béla", "I. (Szent) István", "I. (Szent) László", "II. (Jász) Béla"));
        Infolist.add(new KerdesModel("Ki volt Árpád-házi uralkodó a tatárjárás idején?",
                "II. András", "II. Béla", "IV. Béla", "I. (Szent) István", "IV. Béla"));
        Infolist.add(new KerdesModel("Melyik Árpád-házi uralkodó volt a legfiatalabb halálkorában?",
                "II. (Jász) Béla", "I. (Szent) István", "II. András", "II. Béla", "II. (Jász) Béla"));
        Infolist.add(new KerdesModel("Melyik Árpád-házi uralkodó neve kapcsolódik a Salamon-féle kiáltványhoz?",
                "I. (Szent) László", "I. (Szent) István", "II. (Jász) Béla", "II. András", "II. (Jász) Béla"));
        Infolist.add(new KerdesModel("Ki volt az utolsó Árpád-házi király?",
                "III. (Antiochiai) Béla", "III. (Venetoi) Béla", "IV. Béla", "II. András", "III. (Venetoi) Béla"));
        Infolist.add(new KerdesModel("Melyik Árpád-házi uralkodó uralkodott a leghosszabb ideig?",
                "I. (Szent) István", "II. (Jász) Béla", "III. (Antiochiai) Béla", "II. András", "I. (Szent) István"));
        Infolist.add(new KerdesModel("Melyik Árpád-házi uralkodó neve kapcsolódik a keresztes hadjáratokhoz?",
                "II. (Jász) Béla", "II. András", "I. (Szent) László", "III. (Venetoi) Béla", "I. (Szent) László"));
    }


    private void Tema_Magyar_allamalapitas() {
        Infolist.add(new KerdesModel("Ki volt Magyarország első fejedelme?",
                "Árpád", "Koppány", "Lehel", "Zoltán", "Árpád"));
        Infolist.add(new KerdesModel("Mikor alapították a magyar államot?",
                "895", "955", "1000", "1055", "895"));
        Infolist.add(new KerdesModel("Melyik évben született Szent István, Magyarország első királya?",
                "969", "975", "988", "1001", "969"));
        Infolist.add(new KerdesModel("Milyen vallást hozott Magyarországra Szent István?",
                "Kereszténység", "Iszlám", "Pogány hit", "Zsidó hit", "Kereszténység"));
        Infolist.add(new KerdesModel("Mi volt Szent István uralkodásának kezdeti jellegzetessége?",
                "Államalapítás", "Törzsi társadalom", "Abszolút monarchia", "Demokrácia", "Államalapítás"));
        Infolist.add(new KerdesModel("Melyik évben szentelték fel Szent István királyt?",
                "1000", "1001", "1002", "1003", "1000"));
        Infolist.add(new KerdesModel("Melyik évben temették el Szent István királyt?",
                "1038", "1040", "1050", "1063", "1038"));
        Infolist.add(new KerdesModel("Mikor nyerte vissza Magyarország az önállóságát a török uralom alól?",
                "1686", "1699", "1711", "1725", "1686"));
        Infolist.add(new KerdesModel("Melyik király uralma alatt került sor a kunok betelepítésére?",
                "II. András", "IV. Béla", "I. (Szent) István", "I. Károly", "II. András"));
        Infolist.add(new KerdesModel("Melyik magyar király irányításával alakult ki a kettős államszervezet?",
                "III. Béla", "IV. Béla", "I. (Szent) László", "II. András", "IV. Béla"));
        Infolist.add(new KerdesModel("Melyik király korában vált a magyar királyi hatalom véglegessé a 12. században?",
                "IV. Béla", "III. Béla", "II. András", "I. (Szent) István", "IV. Béla"));
        Infolist.add(new KerdesModel("Melyik magyar király irányításával alakult ki a kettős államszervezet?",
                "III. Béla", "IV. Béla", "I. (Szent) László", "II. András", "IV. Béla"));
        Infolist.add(new KerdesModel("Ki volt a III. Béla által alapított oktatási intézmények egyikének névadója?",
                "Gellért", "Árpád", "Esztergom", "Vajk", "Gellért"));
        Infolist.add(new KerdesModel("Melyik magyar király irányításával alakult ki a kettős államszervezet?",
                "III. Béla", "IV. Béla", "I. (Szent) László", "II. András", "IV. Béla"));
        Infolist.add(new KerdesModel("Ki volt a III. Béla által alapított oktatási intézmények egyikének névadója?",
                "Gellért", "Árpád", "Esztergom", "Vajk", "Gellért"));


    }

    private void Tema_Hunok() {
        Infolist.add(new KerdesModel("Ki volt a hunok legendás vezére?",
                "Attila", "Bleda", "Ruga", "Árpád", "Attila"));
        Infolist.add(new KerdesModel("Hol volt Attila hun birodalmának fővárosa?",
                "Buda", "Róma", "Konstantinápoly", "Németalföld", "Németalföld"));
        Infolist.add(new KerdesModel("Melyik évben hunok dúlták fel Rómát?",
                "410", "452", "476", "489", "452"));
        Infolist.add(new KerdesModel("Hogyan nevezik a hunok által alkotott aranyszarvú leletet?",
                "Aranybulla", "Szent Korona", "Nagy Kán aranyja", "Maros Vára", "Nagy Kán aranyja"));
        Infolist.add(new KerdesModel("Melyik birodalommal szövetkeztek a hunok a rómaiak elleni harcban?",
                "Görög", "Persza", "Vandál", "Gót", "Gót"));
        Infolist.add(new KerdesModel("Ki volt a hunok hírhedt vereségét elszenvedő római császár?",
                "Augustus", "Julius Caesar", "Valentinianus", "Marcus Aurelius", "Valentinianus"));
        Infolist.add(new KerdesModel("Milyen volt a hunok harci technikája?",
                "Lándzsás lovagok", "Gyalogos rohamcsapatok", "Íjász íjászok", "Páncélos lovagok", "Íjász íjászok"));
        Infolist.add(new KerdesModel("Milyen volt a hunok fővárosának körülményei?",
                "Kővár", "Fapalota", "Kaszárnya", "Falusi házak", "Fapalota"));
        Infolist.add(new KerdesModel("Mi volt a hun birodalom jellegzetes hadszíne?",
                "Piros", "Fekete", "Sárga", "Kék", "Sárga"));
        Infolist.add(new KerdesModel("Hogyan nevezik a hunok harci lovaikat?",
                "Tarpan", "Akhal-Teke", "Ló", "Közönséges paripa", "Akhal-Teke"));
        Infolist.add(new KerdesModel("Melyik törzs állt a hunok legyőzésének élére?",
                "Római", "Gót", "Szkíta", "Frank", "Frank"));
        Infolist.add(new KerdesModel("Mi volt a hunok fő ellensége a rómaiakon kívül?",
                "Szkíta", "Gót", "Vandál", "Frank", "Szkíta"));
        Infolist.add(new KerdesModel("Melyik helyen volt a hunok vezérközpontja?",
                "Közép-Ázsia", "Dunántúl", "Szíria", "Anatólia", "Közép-Ázsia"));
        Infolist.add(new KerdesModel("Mik voltak a hunok fegyverei?",
                "Tőr", "Csákány", "Isten ostora", "Lovagi páncél", "Isten ostora"));
        Infolist.add(new KerdesModel("Melyik város volt a legjelentősebb hun település?",
                "Szeged", "Szombathely", "Pécs", "Aquincum", "Aquincum"));
    }

}