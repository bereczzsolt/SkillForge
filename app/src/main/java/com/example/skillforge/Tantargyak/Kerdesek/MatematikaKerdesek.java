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
import com.example.skillforge.Tantargyak.Aktivitik.MatematikaActivity;
import com.example.skillforge.Tantargyak.Model.KerdesModel;
import com.example.skillforge.databinding.ActivityKerdesekBinding;
import java.util.ArrayList;

public class MatematikaKerdesek extends AppCompatActivity {
    ArrayList<KerdesModel> list = new ArrayList<>();
    ActivityKerdesekBinding binding;

    private int szamlalo = 0;
    private int helyzet = 0;
    private int pont = 0;
    CountDownTimer ido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Nézet inflálása a binding segítségével
        binding = ActivityKerdesekBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Időzítő inicializálása és indítása
        idoujraindito();
        ido.start();
        // Intentből való téma kinyerés
        String temanev = getIntent().getStringExtra("tema");
        // A kiválasztott témának feltöltése a listából, ha a lista eleme megegyezik akkor feltölti

        if (temanev != null) {
            switch (temanev) {
                case "Algebra":
                    Tema_Algebra();
                    break;
                case "Geometria":
                    Tema_Geometria();
                    break;
                case "Számelmélet":
                    Tema_szamelmelet();
                    break;
                case "Analízis":
                    Tema_Analizis();
                    break;
                case "Valószínűségszámítás és statisztika":
                    Tema_Valo();
                    break;
                case "Diszkrét matematika":
                    Tema_diszkret();
                    break;
                case "Lineáris algebra":
                    Tema_Linearis();
                    break;
                default:
                    // Ismeretlen téma kezelése
                    break;
            }
        }


        // Kattintásfigyelők hozzáadása a válaszlehetőségekhez
        for (int i = 0; i < 4; i++) {

            binding.Valaszlehetosegek.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkAnswer((Button) view);
                }
            });

            // Animáció lejátszása a feltett kérdésre
            playAnimation(binding.feltettkerdes, 0, list.get(helyzet).getKerdes());
        }
        // Következő kérdés gomb
        binding.kevetkezokerdes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Minden kérdésnél újrainduk az időzítő
                if(ido != null){
                    ido.cancel();
                }
                ido.start();
                // Gomb letiltása és átlátszóságának beállítása
                binding.kevetkezokerdes.setEnabled(false);
                binding.kevetkezokerdes.setAlpha((float) 0.3);
                // Válaszlehetőségek elérhetőségének beállítása
                ElerhetoValasz(true);
                // Helyzet növelése és ellenőrzés, hogy a lista végére értünk-e
                helyzet++;
                if (helyzet == list.size()) {
                    // Pontok és összes kérdés átadása a PontActivity-nek
                    Intent intent = new Intent(MatematikaKerdesek.this, PontActivity.class);
                    intent.putExtra("pont", pont);
                    intent.putExtra("osszes", list.size());
                    startActivity(intent);
                    finish();
                    return;
                }
                szamlalo = 0;
                // Animáció lejátszása a következő kérdésre
                playAnimation(binding.feltettkerdes,0,list.get(helyzet).getKerdes());
            }
        });
    }
    // Időzítő inicializálása
    private void idoujraindito() {

        ido = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long l) {
                binding.ido.setText(String.valueOf(l/1000));
            }

            @Override
            public void onFinish() {
                // Lejárt idő esetén dialógus megjelenítése
                Dialog dialog = new Dialog(MatematikaKerdesek.this);
                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.lejartido);
                dialog.findViewById(R.id.lejartidogomb).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Újra kezdeti a kérdésekkel
                        Intent intent = new Intent(MatematikaKerdesek.this, MatematikaActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                dialog.show();
            }
        };
    }
    // Animáció lejátszása
    private void playAnimation(View view, int ertek, String kerdes) {

        view.animate().alpha(ertek).scaleX(ertek).scaleY(ertek).setDuration(500).setStartDelay(100).setInterpolator(new DecelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(@NonNull Animator animation) {
                        // Ellenőrzi, hogy az animáció elindult-e, és van-e még válaszlehetőség
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
                            // Ellenőrzi, hogy az animáció véget ért-e
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
    // Elérhető válaszlehetőségek beállítása
    private void ElerhetoValasz(boolean elerheto) {

        for (int i= 0; i<4; i++)
        {
            binding.Valaszlehetosegek.getChildAt(i).setEnabled(elerheto);
            if(elerheto){
                binding.Valaszlehetosegek.getChildAt(i).setBackgroundResource(R.drawable.background);

            }

        }

    }
    // Ellenőrzi a választ és frissíti a pontszámot
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

    // Lista kitöltése, kérdés, 4 válaszopció, és a jó válasz

    private void Tema_Linearis() {
        list.add(new KerdesModel("Mi a mátrixszorzás jellemzője?",
                "Nem kommutatív", "Kommutatív", "Asszociatív", "Disztributív", "Nem kommutatív"));

        list.add(new KerdesModel("Mi a determináns definíciója egy 2x2-es mátrix esetén?",
                "ad-bc", "a+b+c+d", "ab+cd", "ab-cd", "ad-bc"));

        list.add(new KerdesModel("Mi a determináns definíciója egy 3x3-as mátrix esetén?",
                "a(ei − fh) − b(di − fg) + c(dh − eg)", "abc+def", "ad-bc", "ab+cd", "a(ei − fh) − b(di − fg) + c(dh − eg)"));

        list.add(new KerdesModel("Mi az inverz mátrix tulajdonsága?",
                "Az inverze az eredeti mátrixszal szorozva az egységmátrixot adja eredményül", "Az inverze az egységmátrix", "Az inverze a nullmátrix", "Az inverze a transzponált mátrix", "Az inverze az eredeti mátrixszal szorozva az egységmátrixot adja eredményül"));

        list.add(new KerdesModel("Mi az identitás-mátrix jellemzője?",
                "Az átlóban csak 1-esek vannak, minden más elem 0", "Az átlóban csak 0-k vannak, minden más elem 1", "Az összes elem csak 1", "Az összes elem csak 0", "Az átlóban csak 1-esek vannak, minden más elem 0"));

        list.add(new KerdesModel("Mi a transzponált mátrix jellemzője?",
                "Az eredeti mátrix sorait cseréli oszlopokkal", "Az eredeti mátrix oszlopait cseréli sorokkal", "Az eredeti mátrixot tükrözi", "Az eredeti mátrixot forgatja", "Az eredeti mátrix sorait cseréli oszlopokkal"));

        list.add(new KerdesModel("Mi a vektor definíciója a lineáris algebrában?",
                "Egy olyan mennyiség, amelyet irány és hosszúság jellemez", "Egy olyan mennyiség, amelyet irány jellemez", "Egy olyan mennyiség, amelyet hosszúság jellemez", "Egy olyan mennyiség, amelyet terület jellemez", "Egy olyan mennyiség, amelyet irány és hosszúság jellemez"));

        list.add(new KerdesModel("Mi az a skalárszorzat két vektor között?",
                "A két vektor közötti szorzat és az eredmény egy skalár", "A két vektor közötti szorzat és az eredmény egy vektor", "Az összes vektor összege", "Az összes vektor szorzata", "A két vektor közötti szorzat és az eredmény egy skalár"));

        list.add(new KerdesModel("Mi az a lineáris egyenletrendszer?",
                "Olyan egyenletek halmaza, amelyek megoldásai ugyanazon változók értékei", "Olyan egyenletek halmaza, amelyek nem függenek egymástól", "Olyan egyenletek halmaza, amelyek különböző változókat tartalmaznak", "Olyan egyenletek halmaza, amelyek azonos változókat tartalmaznak", "Olyan egyenletek halmaza, amelyek megoldásai ugyanazon változók értékei"));

        list.add(new KerdesModel("Mi az a lineáris kombináció?",
                "A vektorok skalárszorzatainak összege", "A vektorok összege", "A vektorok szorzata", "A vektorok szorzatának összege", "A vektorok skalárszorzatainak összege"));

        list.add(new KerdesModel("Mi az a sajátérték és sajátvektor?",
                "Egy mátrix sajátértéke és hozzá tartozó sajátvektorja olyan, hogy amikor a sajátvektort a mátrixhoz szorozzuk, az eredmény a sajátértzegszorosa lesz a sajátvektornak", "Egy vektor sajátértéke és hozzá tartozó sajátvektorja olyan, hogy amikor a sajátvektort a mátrixhoz szorozzuk, az eredmény a sajátérték lesz", "Egy mátrix sajátértéke és hozzá tartozó sajátvektorja olyan, hogy amikor a sajátvektort a mátrixhoz hozzáadjuk, az eredmény a sajátérték lesz", "Egy vektor sajátértéke és hozzá tartozó sajátvektorja olyan, hogy amikor a sajátvektort a mátrixhoz hozzáadjuk, az eredmény a sajátértzegszorosa lesz a sajátvektornak", "Egy mátrix sajátértéke és hozzá tartozó sajátvektorja olyan, hogy amikor a sajátvektort a mátrixhoz szorozzuk, az eredmény a sajátértzegszorosa lesz a sajátvektornak"));

        list.add(new KerdesModel("Mi az a determináns 0-ban egy mátrix esetén?",
                "A mátrixnak nincs inverze", "A mátrixnak van inverze", "A mátrix minden eleme 0", "A mátrix minden eleme 1", "A mátrixnak nincs inverze"));

        list.add(new KerdesModel("Mi a rangja egy mátrixnak?",
                "A mátrix oszlopszámai közül a nemnulla sorok száma", "A mátrix összes eleme", "A mátrix oszlopszámai", "A mátrix sorainak száma", "A mátrix oszlopszámai közül a nemnulla sorok száma"));

        list.add(new KerdesModel("Mi a távolság formulája két pont között a síkon?",
                "A gyök az (x2-x1)^2 + (y2-y1)^2 összegből", "A (x2-x1) * (y2-y1) szorzat", "A (x2+x1) * (y2+y1) szorzat", "A (x2+x1)^2 + (y2+y1)^2 összegből", "A gyök az (x2-x1)^2 + (y2-y1)^2 összegből"));

        list.add(new KerdesModel("Mi az általános egyenes egyenlete a síkon?",
                "Ax + By + C = 0", "Ax + By = C", "A + B = C", "A + B + C = 0", "Ax + By + C = 0"));


    }
    private void Tema_diszkret() {
        list.add(new KerdesModel("Mi az abszolút érték definíciója?",
                "Egy szám távolsága a nullától", "Egy szám távolsága a 0-tól", "Egy szám abszolút értéke", "Egy szám távolsága", "Egy szám távolsága a nullától"));

        list.add(new KerdesModel("Mi a legkisebb közös többszörös (LKKT) definíciója?",
                "A legkisebb szám, amely osztható minden adott számmal", "A legkisebb szám, amely osztható két adott számmal", "A két adott szám legnagyobb közös osztójának többszöröse", "A két adott szám legnagyobb közös osztója", "A legkisebb szám, amely osztható minden adott számmal"));

        list.add(new KerdesModel("Mi a legnagyobb közös osztó (LNKO) definíciója?",
                "A legnagyobb szám, amely osztható mindkét adott számmal", "A legkisebb szám, amely osztható mindkét adott számmal", "A két adott szám legkisebb közös többszöröse", "A két adott szám legkisebb közös többszöröse", "A legnagyobb szám, amely osztható mindkét adott számmal"));

        list.add(new KerdesModel("Mi az összegképzés definíciója a kombinatorikában?",
                "Azon módszer, amely szerint összeadjuk az összes lehetséges kimenetelt", "Azon módszer, amely szerint összeszorozzuk az összes lehetséges kimenetelt", "Azon módszer, amely szerint megszámoljuk az összes lehetséges kimenetelt", "Azon módszer, amely szerint meghatározzuk az összes lehetséges kimenetelt", "Azon módszer, amely szerint összeadjuk az összes lehetséges kimenetelt"));

        list.add(new KerdesModel("Mi a kombináció definíciója a kombinatorikában?",
                "Egy olyan választás, amelyben a sorrend nem számít", "Egy olyan választás, amelyben a sorrend számít", "Az összes lehetséges kimenetel meghatározása", "Az összes lehetséges kimenetel összeszorzása", "Egy olyan választás, amelyben a sorrend nem számít"));

        list.add(new KerdesModel("Mi a permutáció definíciója a kombinatorikában?",
                "Az összes lehetséges sorrend meghatározása", "Az összes lehetséges kimenetel meghatározása", "Az összes lehetséges kimenetel összeszorzása", "Az összes lehetséges kimenetel megszámlálása", "Az összes lehetséges sorrend meghatározása"));

        list.add(new KerdesModel("Mi az a szita definíciója?",
                "Egy algoritmus, amely meghatározza a prímszámokat", "Egy algoritmus, amely meghatározza a páros számokat", "Egy algoritmus, amely meghatározza a két hatványú számokat", "Egy algoritmus, amely meghatározza a háromszög számokat", "Egy algoritmus, amely meghatározza a prímszámokat"));

        list.add(new KerdesModel("Mi a diszkrét matematika fő célja?",
                "A diszkrét struktúrák és azok tulajdonságainak vizsgálata", "A folytonos struktúrák és azok tulajdonságainak vizsgálata", "Az algebrai egyenletek megoldása", "A geometriai alakzatok vizsgálata", "A diszkrét struktúrák és azok tulajdonságainak vizsgálata"));

        list.add(new KerdesModel("Mi az inverz művelet definíciója a csoportelméletben?",
                "Az az m * n egész szám, amelynek eredménye az egységes elem", "Az az elem, amelynek művelettel történő kombinációja az egységes elem", "Az az elem, amely a szorzattal történő kombinációja az egységes elem", "Az az m * n egész szám, amely a szorzat eredménye", "Az az elem, amely a szorzattal történő kombinációja az egységes elem"));

        list.add(new KerdesModel("Mi az egységes elem definíciója a csoportelméletben?",
                "Az az elem, amely minden más elemmel végzett művelet eredményeként ugyanazt az elemet adja vissza", "Az az elem, amely a művelet eredményeként az egységes elemet adja vissza", "Az az elem, amely a szorzat eredménye", "Az az elem, amely minden más elemmel végzett művelet eredményeként az egységes elemet adja vissza", "Az az elem, amely minden más elemmel végzett művelet eredményeként ugyanazt az elemet adja vissza"));

        list.add(new KerdesModel("Mi a kommutativitás definíciója a csoportelméletben?",
                "A művelet két operandusa felcserélhető", "A művelet eredménye azonos mindkét operandussal", "A művelet két operandusa összeadható", "A művelet két operandusa szorzható", "A művelet két operandusa felcserélhető"));

        list.add(new KerdesModel("Mi a diszjunkció definíciója a logikában?",
                "Két állítás közötti „vagy” kapcsolat", "Két állítás közötti „és” kapcsolat", "Két állítás közötti „nem” kapcsolat", "Két állítás közötti „illetve” kapcsolat", "Két állítás közötti „vagy” kapcsolat"));

        list.add(new KerdesModel("Mi a konjunkció definíciója a logikában?",
                "Két állítás közötti „és” kapcsolat", "Két állítás közötti „vagy” kapcsolat", "Két állítás közötti „nem” kapcsolat", "Két állítás közötti „illetve” kapcsolat", "Két állítás közötti „és” kapcsolat"));

        list.add(new KerdesModel("Mi a komplementer esemény definíciója a valószínűségszámításban?",
                "Az esemény nem bekövetkezése", "Az esemény bekövetkezése", "Az esemény valószínűsége", "Az eseményt kiegészítő esemény", "Az esemény nem bekövetkezése"));


    }
    private void Tema_Valo() {
        list.add(new KerdesModel("Mi az esemény valószínűsége, ha egy kockával dobunk, és 3-ast dobunk?",
                "1/6", "1/3", "1/2", "1/4", "1/6"));

        list.add(new KerdesModel("Mi a relatív gyakoriság definíciója?",
                "Az adott esemény bekövetkezésének gyakorisága az összes kísérlet során", "Az esemény bekövetkezésének valószínűsége", "Az esemény bekövetkezésének gyakorisága egy adott kísérlet során", "Az adott esemény valószínűsége", "Az adott esemény bekövetkezésének gyakorisága az összes kísérlet során"));

        list.add(new KerdesModel("Mi a marginális eloszlás?",
                "Az egyik változó eloszlása függetlenül a többitől", "Két vagy több változó együttes eloszlása", "Az összes változó eloszlása", "A változók közötti kapcsolat", "Az egyik változó eloszlása függetlenül a többitől"));

        list.add(new KerdesModel("Mi a kumulatív eloszlásfüggvény?",
                "Az adott pontig terjedő valószínűséget adja meg", "Az adott pontban lévő valószínűséget adja meg", "Az adott intervallum valószínűségét adja meg", "Az adott intervallumban lévő valószínűséget adja meg", "Az adott pontig terjedő valószínűséget adja meg"));

        list.add(new KerdesModel("Mi a valószínűségi tér?",
                "Az összes lehetséges kimenetel halmaza", "Az események halmaza", "Az adott kimenetel halmaza", "Az adott események halmaza", "Az összes lehetséges kimenetel halmaza"));

        list.add(new KerdesModel("Mi az átlag?",
                "Az összes adat értékének átlaga", "A leggyakrabban előforduló érték", "A medián", "A modusz", "Az összes adat értékének átlaga"));

        list.add(new KerdesModel("Mi a szórás?",
                "Az adatok eloszlásának mértéke", "Az adatok közötti különbség", "Az adatok középértéke", "Az adatok mediánja", "Az adatok eloszlásának mértéke"));

        list.add(new KerdesModel("Mi a medián?",
                "Az adatok középső értéke", "Az adatok középértéke", "Az adatok leggyakoribb értéke", "Az adatok eloszlásának mértéke", "Az adatok középső értéke"));

        list.add(new KerdesModel("Mi a kvartilis?",
                "Az adatok negyedik és hetedik percentilise", "Az adatok középső értéke", "Az adatok negyedik és ötödik percentilise", "Az adatok átlaga", "Az adatok negyedik és hetedik percentilise"));

        list.add(new KerdesModel("Mi a modusz?",
                "Az adatok leggyakrabban előforduló értéke", "Az adatok középértéke", "Az adatok átlaga", "Az adatok mediánja", "Az adatok leggyakrabban előforduló értéke"));

        list.add(new KerdesModel("Mi a binomiális eloszlás?",
                "Azonos feltételekkel ismételt kísérletek esetén alkalmazott eloszlás", "Két vagy több független változó együttes eloszlása", "Az adatok közötti kapcsolat", "Az egyik változó eloszlása függetlenül a többitől", "Azonos feltételekkel ismételt kísérletek esetén alkalmazott eloszlás"));

        list.add(new KerdesModel("Mi a Poisson-eloszlás?",
                "Ritka események bekövetkezéseinek eloszlása", "Az esemény bekövetkezésének valószínűsége", "Az adatok eloszlása", "Az adatok közötti kapcsolat", "Ritka események bekövetkezéseinek eloszlása"));

        list.add(new KerdesModel("Mi a normál eloszlás?",
                "Szimmetrikus, csúcsos eloszlás", "Aszimmetrikus, lelapult eloszlás", "Az adatok közötti kapcsolat", "Az egyik változó eloszlása függetlenül a többitől", "Szimmetrikus, csúcsos eloszlás"));

        list.add(new KerdesModel("Mi a hipergeometriai eloszlás?",
                "A mintavétel során a visszahelyezés nélküli kísérletek eloszlása", "Az azonos feltételekkel ismételt kísérletek eloszlása", "Az adatok közötti kapcsolat", "Az egyik változó eloszlása függetlenül a többitől", "A mintavétel során a visszahelyezés nélküli kísérletek eloszlása"));

        list.add(new KerdesModel("Mi a kapcsolat a kovariancia és a korreláció között?",
                "A korreláció a normalizált kovariancia", "A kovariancia a normalizált korreláció", "Nincs kapcsolat közöttük", "Mindkettő ugyanazt jelenti", "A korreláció a normalizált kovariancia"));

    }
    private void Tema_Analizis() {
        list.add(new KerdesModel("Mi az 1-től 10-ig terjedő egész számok összege?",
                "55", "50", "45", "60", "55"));

        list.add(new KerdesModel("Mi az x^2 deriváltja?",
                "2x", "x", "2", "1", "2x"));

        list.add(new KerdesModel("Mennyi az ∫(2x) dx értéke?",
                "x^2 + C", "x^2", "2x", "2", "x^2 + C"));

        list.add(new KerdesModel("Mi az x^3 integrálja?",
                "(1/4)x^4 + C", "(1/3)x^3", "x^4", "3x^2", "(1/4)x^4 + C"));

        list.add(new KerdesModel("Mi az ∫(3x^2 + 2x) dx értéke?",
                "(x^3 + x^2) + C", "3x^3 + x^2", "(3/2)x^3 + x^2", "(3/2)x^3", "(x^3 + x^2) + C"));

        list.add(new KerdesModel("Mennyi az ∫(e^x) dx értéke?",
                "e^x + C", "e^x", "x", "1", "e^x + C"));

        list.add(new KerdesModel("Mi az e^x deriváltja?",
                "e^x", "x", "1", "0", "e^x"));

        list.add(new KerdesModel("Mi az ∫(sin(x)) dx értéke?",
                "-cos(x) + C", "-cos(x)", "cos(x)", "sin(x)", "-cos(x) + C"));

        list.add(new KerdesModel("Mi az sin(x) deriváltja?",
                "cos(x)", "sin(x)", "-cos(x)", "-sin(x)", "cos(x)"));


        list.add(new KerdesModel("Mi az ln(x) deriváltja?",
                "1/x", "ln(x)", "x", "0", "1/x"));

        list.add(new KerdesModel("Mi az ∫(cos(x)) dx értéke?",
                "sin(x) + C", "sin(x)", "-sin(x)", "cos(x)", "sin(x) + C"));

        list.add(new KerdesModel("Mi az cos(x) deriváltja?",
                "-sin(x)", "cos(x)", "sin(x)", "0", "-sin(x)"));

        list.add(new KerdesModel("Mi az ∫(x^2 + 3x - 2) dx értéke?",
                "(1/3)x^3 + (3/2)x^2 - 2x + C", "x^3 + 3x^2 - 2x", "(1/3)x^3 + (3/2)x^2 - 2x", "x^3 + 3x^2 - 2x + C", "(1/3)x^3 + (3/2)x^2 - 2x + C"));

        list.add(new KerdesModel("Mennyi az lim(x->∞) (1/x) értéke?",
                "0", "1", "∞", "nincs határérték", "0"));

        list.add(new KerdesModel("Mi az lim(x->0) (sin(x)/x) értéke?",
                "1", "0", "∞", "nincs határérték", "1"));

    }
    private void Tema_szamelmelet() {
        list.add(new KerdesModel("Mi az első 5 prímszám összege?",
                "28", "15", "20", "25", "28"));

        list.add(new KerdesModel("Mennyi az 5! értéke?",
                "120", "60", "24", "720", "120"));

        list.add(new KerdesModel("Mi a legnagyobb közös osztója (LNKO) 24 és 36 között?",
                "12", "6", "3", "4", "12"));

        list.add(new KerdesModel("Mennyi az 1000 és 999 közötti prímszámok száma?",
                "168", "150", "200", "120", "168"));

        list.add(new KerdesModel("Mi az első 10 pozitív egész szám összege?",
                "55", "45", "50", "60", "55"));

        list.add(new KerdesModel("Mi a 18 és 24 legkisebb közös többszöröse?",
                "72", "36", "18", "24", "72"));

        list.add(new KerdesModel("Mi a 24 osztható-e 6-tal?",
                "Igen", "Nem", "", "", "Igen"));

        list.add(new KerdesModel("Mennyi az 1-től 100-ig terjedő páros számok összege?",
                "2550", "2500", "2600", "2400", "2550"));

        list.add(new KerdesModel("Mi az első 6 pozitív egész szám szorzata?",
                "720", "360", "120", "5040", "720"));

        list.add(new KerdesModel("Mennyi az 1-től 10-ig terjedő négyzetszámok összege?",
                "385", "330", "420", "455", "385"));

        list.add(new KerdesModel("Mi az 1-től 100-ig terjedő prímszámok összege?",
                "1060", "1020", "1000", "1100", "1060"));

        list.add(new KerdesModel("Mi az első 3 pozitív egész szám átlaga?",
                "2", "3", "1.5", "2.5", "2"));

        list.add(new KerdesModel("Mennyi az 1 és 100 közötti páros számok száma?",
                "50", "49", "51", "48", "50"));

        list.add(new KerdesModel("Mi az 1-től 10-ig terjedő páros számok összege?",
                "30", "25", "35", "20", "30"));

        list.add(new KerdesModel("Mi az első 7 prímszám összege?",
                "100", "90", "110", "120", "100"));

    }


    private void Tema_Geometria() {
        list.add(new KerdesModel("Mi a területe egy téglalapnak, ha az oldalai 5 és 8 egység hosszúak?",
                "40 egység^2", "13 egység^2", "26 egység^2", "15 egység^2", "40 egység^2"));

        list.add(new KerdesModel("Mi a kerülete egy négyzetnek, ha az oldala 6 egység hosszú?",
                "24 egység", "12 egység", "36 egység", "18 egység", "24 egység"));

        list.add(new KerdesModel("Mennyi a kör kerülete, ha sugara 4 egység?",
                "8π egység", "16π egység", "4π egység", "π egység", "8π egység"));

        list.add(new KerdesModel("Mi a területe egy háromszögnek, ha a magassága 10 egység, az alapja pedig 6 egység?",
                "30 egység^2", "20 egység^2", "12 egység^2", "15 egység^2", "30 egység^2"));

        list.add(new KerdesModel("Mennyi a térfogata egy kockának, ha az oldala 3 egység hosszú?",
                "27 egység^3", "9 egység^3", "18 egység^3", "6 egység^3", "27 egység^3"));

        list.add(new KerdesModel("Mi a területe egy körnek, ha sugarának hossza 5 egység?",
                "25π egység^2", "10π egység^2", "15π egység^2", "5π egység^2", "25π egység^2"));

        list.add(new KerdesModel("Mi a térfogata egy gömbnek, ha sugarának hossza 2 egység?",
                "8π/3 egység^3", "4π egység^3", "16π egység^3", "2π egység^3", "8π/3 egység^3"));

        list.add(new KerdesModel("Mennyi a kerülete egy téglalapnak, ha az oldalai 4 és 10 egység hosszúak?",
                "28 egység", "40 egység", "14 egység", "24 egység", "28 egység"));

        list.add(new KerdesModel("Mi a területe egy trapéznek, ha az alapjai 6 és 8 egység hosszúak, a magassága pedig 5 egység?",
                "35 egység^2", "30 egység^2", "25 egység^2", "40 egység^2", "35 egység^2"));

        list.add(new KerdesModel("Mi a kerülete egy derékszögű háromszögnek, ha az egyik befogója 3, a másik pedig 4 egység hosszú?",
                "12 egység", "10 egység", "5 egység", "24 egység", "12 egység"));

        list.add(new KerdesModel("Mi a térfogata egy négyzetes alapú hasáboknak, ha az alapja területe 16 egység^2, a magassága pedig 5 egység?",
                "80 egység^3", "60 egység^3", "20 egység^3", "40 egység^3", "80 egység^3"));

        list.add(new KerdesModel("Mi a területe egy rombusznak, ha az egyik átlója 8 egység hosszú, a másik pedig 6 egység?",
                "24 egység^2", "48 egység^2", "36 egység^2", "16 egység^2", "24 egység^2"));

        list.add(new KerdesModel("Mi a térfogata egy hengernek, ha sugarának hossza 3, a magassága pedig 7 egység?",
                "63π egység^3", "21π egység^3", "42π egység^3", "9π egység^3", "63π egység^3"));

        list.add(new KerdesModel("Mi a területe egy kockának, ha az éleinek hossza 4 egység?",
                "96 egység^2", "64 egység^2", "16 egység^2", "24 egység^2", "96 egység^2"));

        list.add(new KerdesModel("Mi a kerülete egy körnek, ha sugarának hossza 7 egység?",
                "14π egység", "21π egység", "49π egység", "7π egység", "14π egység"));

    }

    private void Tema_Algebra() {
        list.add(new KerdesModel("Mi a deriváltja az x^2 függvénynek?",
                "2x", "x^2", "2", "1", "2x"));

        list.add(new KerdesModel("Mi a másodfokú egyenlet egyik gyöke, ha a = 1, b = -5, c = 6?",
                "3", "-2", "2", "1", "3"));

        list.add(new KerdesModel("Mennyi az x értéke az x - 2 = 0 egyenletnek?",
                "2", "0", "-2", "1", "2"));

        list.add(new KerdesModel("Mi a (a + b)^2 kifejezés kibontott alakja?",
                "a^2 + 2ab + b^2", "a^2 - 2ab + b^2", "a^2 + ab + b^2", "a^2 - ab + b^2", "a^2 + 2ab + b^2"));

        list.add(new KerdesModel("Mi a log₂8 értéke?",
                "3", "2", "4", "1", "3"));

        list.add(new KerdesModel("Mi a 3x + 2y = 8 egyenlet megoldása, ha x = 2?",
                "y = 2", "y = 1", "y = 3", "y = 4", "y = 2"));

        list.add(new KerdesModel("Mi a 6! értéke?",
                "720", "120", "7200", "620", "720"));

        list.add(new KerdesModel("Mennyi az (x + 3)(x - 2) kifejezés kibontott alakja?",
                "x^2 + x - 6", "x^2 - x - 6", "x^2 + 3x - 2", "x^2 - 3x + 2", "x^2 + x - 6"));

        list.add(new KerdesModel("Mennyi a log₄16 értéke?",
                "2", "4", "1", "8", "2"));

        list.add(new KerdesModel("Mi a 2x + 3y = 10 egyenlet megoldása, ha y = 2?",
                "x = 2", "x = 1", "x = 3", "x = 4", "x = 2"));

        list.add(new KerdesModel("Mennyi az (x - 1)(x + 1) kifejezés kibontott alakja?",
                "x^2 - 1", "x^2 + 1", "x^2 - x + 1", "x^2 + x + 1", "x^2 - 1"));

        list.add(new KerdesModel("Mi a log₅25 értéke?",
                "2", "5", "1", "6", "2"));

        list.add(new KerdesModel("Mennyi az 5^3 értéke?",
                "125", "25", "500", "15", "125"));

        list.add(new KerdesModel("Mennyi a (x + 2)^2 kifejezés kibontott alakja?",
                "x^2 + 4x + 4", "x^2 + 4x + 2", "x^2 - 4x + 4", "x^2 - 4x + 2", "x^2 + 4x + 4"));

        list.add(new KerdesModel("Mi a log₃81 értéke?",
                "4", "3", "2", "5", "4"));

    }

}
