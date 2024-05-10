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
import com.example.skillforge.Tantargyak.Aktivitik.VegyesKerdesekActivity;
import com.example.skillforge.Tantargyak.Model.KerdesModel;
import com.example.skillforge.databinding.ActivityKerdesekBinding;
import java.util.ArrayList;

public class VegyesKerdesek extends AppCompatActivity {
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
                case "Irodalom + Nyelvtan":
                    Tema_001();
                    break;
                case "Irodalom + Történelem":
                    Tema_002();
                    break;
                case "Érettségi tantárgyak vegyes kérdések: Első Teszt":
                    Tema_003();
                    break;
                case "Érettségi tantárgyak vegyes kérdések: Második Teszt":
                    Tema_004();
                    break;
                case "Érettségi tantárgyak vegyes kérdések: Harmadik Teszt":
                    Tema_005();
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
                    Intent intent = new Intent(VegyesKerdesek.this, PontActivity.class);
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
                Dialog dialog = new Dialog(VegyesKerdesek.this);
                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.lejartido);
                dialog.findViewById(R.id.lejartidogomb).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Újra kezdeti a kérdésekkel
                        Intent intent = new Intent(VegyesKerdesek.this, VegyesKerdesekActivity.class);
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
    private void Tema_005() {
        list.add(new KerdesModel("Ki írta a Harry Potter könyveket?",
                "J.K. Rowling", "J.R.R. Tolkien", "C.S. Lewis", "George R.R. Martin", "J.K. Rowling"));

        list.add(new KerdesModel("Melyik évben kezdődött a második világháború?",
                "1939", "1941", "1945", "1943", "1939"));

        list.add(new KerdesModel("Melyik állam a legnagyobb területű az Amerikai Egyesült Államokban?",
                "Alaska", "Texas", "Kalifornia", "Montana", "Alaska"));

        list.add(new KerdesModel("Ki volt a legutolsó dinasztia a kínai császárságban?",
                "Qing-dinasztia", "Ming-dinasztia", "Han-dinasztia", "Tang-dinasztia", "Qing-dinasztia"));

        list.add(new KerdesModel("Melyik évszázadban volt a reneszánsz?",
                "15. század", "14. század", "16. század", "17. század", "15. század"));

        list.add(new KerdesModel("Melyik film nyerte meg az Oscar-díjat a legtöbb kategóriában?",
                "Ben Hur", "Titanic", "Gyűrűk Ura: A király visszatér", "A Gatsby fényei", "Gyűrűk Ura: A király visszatér"));

        list.add(new KerdesModel("Ki írta az Odyssey-t?",
                "Homer", "Plató", "Sokrates", "Euripidész", "Homer"));

        list.add(new KerdesModel("Melyik szó helyesen íródik?",
                "Conscious", "Concious", "Consciouss", "Conscius", "Conscious"));

        list.add(new KerdesModel("Melyik évben volt az első Holdra szállás?",
                "1969", "1965", "1972", "1975", "1969"));

        list.add(new KerdesModel("Melyik ország Európa legkisebb területű állama?",
                "Vatikán", "Monaco", "San Marino", "Andorra", "Vatikán"));

        list.add(new KerdesModel("Melyik évben alapították az Apple céget?",
                "1976", "1980", "1985", "1990", "1976"));

        list.add(new KerdesModel("Ki volt a legfiatalabb Beatles tag?",
                "George Harrison", "Paul McCartney", "John Lennon", "Ringo Starr", "George Harrison"));

        list.add(new KerdesModel("Melyik évben fejeződött be az első világháború?",
                "1918", "1916", "1914", "1920", "1918"));

        list.add(new KerdesModel("Melyik évben volt a berlini fal leomlása?",
                "1989", "1990", "1985", "1995", "1989"));

        list.add(new KerdesModel("Melyik város a legnépesebb a világon?",
                "Tokió", "Shanghai", "Delhi", "Mumbai", "Tokió"));

    }
    private void Tema_004() {
        list.add(new KerdesModel("Melyik szó helyesen íródik?",
                "Occurrence", "Occurence", "Ocurrance", "Occurenc", "Occurrence"));

        list.add(new KerdesModel("Ki volt az Amerikai Egyesült Államok első elnöke?",
                "George Washington", "Abraham Lincoln", "Thomas Jefferson", "John F. Kennedy", "George Washington"));

        list.add(new KerdesModel("Melyik regényt írta Gabriel García Márquez?",
                "Száz év magány", "Az Éhezők viadala", "Az idő rabjai", "A Katedrális", "Száz év magány"));

        list.add(new KerdesModel("Mennyi az 5×6?",
                "30", "25", "35", "40", "30"));

        list.add(new KerdesModel("Mi a HTML rövidítése?",
                "Hyper Text Markup Language", "High Tech Modern Language", "Home Tool Markup Language", "Hyperlinking Text Mode Language", "Hyper Text Markup Language"));

        list.add(new KerdesModel("Melyik szó helyesen íródik?",
                "Receive", "Recieve", "Recive", "Receve", "Receive"));

        list.add(new KerdesModel("Melyik évben alakult meg az Egyesült Nemzetek Szervezete (ENSZ)?",
                "1945", "1950", "1939", "1940", "1945"));

        list.add(new KerdesModel("Ki írta a Júdás Máriáját?",
                "Babits Mihály", "Jókai Mór", "Arany János", "Szabó Magda", "Babits Mihály"));

        list.add(new KerdesModel("Mennyi az 5 négyzetgyöke?",
                "√5", "20", "25", "2.5", "√5"));

        list.add(new KerdesModel("Melyik az egyik legnépszerűbb adatbázis-kezelő rendszer?",
                "MySQL", "MongoDB", "PostgreSQL", "SQLite", "MySQL"));
        list.add(new KerdesModel("Melyik mondat helyesen íródik? 'She ________ to Paris last summer.'",
                "went", "gone", "goes", "go", "went"));

        list.add(new KerdesModel("Melyik szó helyesen íródik? 'The weather is _______ today.'",
                "nice", "nicer", "more nice", "nicest", "nice"));

        list.add(new KerdesModel("Melyik mondat tartalmaz főnévi igenév formát?",
                "Swimming is my favorite hobby.", "She went swimming yesterday.", "They are swimming right now.", "He likes to swim every morning.", "Swimming is my favorite hobby."));

        list.add(new KerdesModel("Milyen szó helyesen íródik?",
                "Occurrence", "Occurence", "Ocurrance", "Occurenc", "Occurrence"));

        list.add(new KerdesModel("Melyik évben volt az első világháború vége?",
                "1918", "1916", "1914", "1920", "1918"));

    }

    private void Tema_003() {
        list.add(new KerdesModel("Ki alkotta meg a C++ programozási nyelvet?",
                "Bjarne Stroustrup", "Dennis Ritchie", "Linus Torvalds", "Tim Berners-Lee", "Bjarne Stroustrup"));
        list.add(new KerdesModel("Melyik operációs rendszer az alapja az Apple termékeknek, mint például az iPhone és a MacBook?",
                "macOS", "Windows", "Linux", "Android", "macOS"));
        list.add(new KerdesModel("Melyik keresőmotort hozta létre a Google?",
                "Google", "Yahoo", "Bing", "Ask.com", "Google"));
        list.add(new KerdesModel("Melyik mondat helyes az alábbiak közül? 'She ________ to Paris last summer.'",
                "went", "gone", "goes", "go", "went"));
        list.add(new KerdesModel("Melyik szó helyes az alábbiak közül? 'The weather is _______ today.'",
                "nice", "nicer", "more nice", "nicest", "nice"));
        list.add(new KerdesModel("Melyik mondat tartalmaz főnévi igenév formát?",
                "Swimming is my favorite hobby.", "She went swimming yesterday.", "They are swimming right now.", "He likes to swim every morning.", "Swimming is my favorite hobby."));
        list.add(new KerdesModel("Melyik évben volt az első világháború vége?",
                "1918", "1916", "1914", "1920", "1918"));
        list.add(new KerdesModel("Ki volt Magyarország utolsó királya?",
                "IV. Károly", "I. Ferenc József", "I. Lipót", "I. István", "IV. Károly"));
        list.add(new KerdesModel("Melyik évben volt az amerikai polgárháború?",
                "1861–1865", "1776–1783", "1914–1918", "1939–1945", "1861–1865"));
        list.add(new KerdesModel("Mennyi az 5 és 9 közötti számok összege?",
                "45", "35", "50", "55", "45"));
        list.add(new KerdesModel("Mi a négyzetgyök 144-ből?",
                "12", "11", "13", "14", "12"));
        list.add(new KerdesModel("Mennyi az 5×8?",
                "40", "30", "35", "45", "40"));
        list.add(new KerdesModel("Melyik költő alkotásához kapcsolódik a Bánk bán?",
                "Katona József", "Petőfi Sándor", "Arany János", "József Attila", "Katona József"));
        list.add(new KerdesModel("Melyik évben alakult meg az Európai Unió?",
                "1993", "1989", "2001", "1975", "1993"));
        list.add(new KerdesModel("Ki volt a legutolsó dinasztia a kínai császárságban?",
                "Qing-dinasztia", "Ming-dinasztia", "Han-dinasztia", "Tang-dinasztia", "Qing-dinasztia"));
    }


    private void Tema_002() {
        list.add(new KerdesModel("Ki írta az Anjouk trilógiát?",
                "Mór Jókai", "Kosztolányi Dezső", "Arany János", "Szabó Magda", "Mór Jókai"));

        list.add(new KerdesModel("Melyik történelmi korszakhoz tartozik a Vörös és Fehér című regény?",
                "Rákóczi-szabadságharc", "Az első világháború", "Az aranykor", "Az államalapítás kora", "Rákóczi-szabadságharc"));

        list.add(new KerdesModel("Melyik történelmi esemény idején játszódik a Toldi-trilógia?",
                "Luxemburgi Zsigmond uralkodása alatt", "Rákóczi-szabadságharc idején", "Mohácsi csata után", "I. Ferdinánd uralkodása alatt", "Luxemburgi Zsigmond uralkodása alatt"));

        list.add(new KerdesModel("Melyik regénynek a története játszódik a 19. század elején, a velencei karnevál idején?",
                "A két Bajtárs", "Vörös és fehér", "Az aranyember", "Egy magyar nábob", "A két Bajtárs"));

        list.add(new KerdesModel("Melyik regény főszereplője Zsigmond király?",
                "Egri csillagok", "A két Bajtárs", "Az arany ember", "A vörös postakocsi", "Az arany ember"));

        list.add(new KerdesModel("Melyik könyv tartalmazza a Hősök tere című elbeszélést?",
                "Az elhagyott falu", "Az eladott lány", "Toldi", "Arany László élete", "Az elhagyott falu"));

        list.add(new KerdesModel("Ki az a magyar író, aki az Ember tragédiája című drámát írta?",
                "Madách Imre", "Arany János", "Petőfi Sándor", "Jókai Mór", "Madách Imre"));

        list.add(new KerdesModel("Melyik költő alkotásához kapcsolódik a Szózat?",
                "Vörösmarty Mihály", "Arany János", "Petőfi Sándor", "József Attila", "Vörösmarty Mihály"));

        list.add(new KerdesModel("Melyik regényben találkozhatunk Toldi Miklóssal?",
                "Toldi", "Aranyember", "Az arany palást", "Egy magyar nábob", "Toldi"));

        list.add(new KerdesModel("Ki az a lengyel író, aki a Rokonok című drámát írta?",
                "Aleksander Fredro", "Adam Mickiewicz", "Juliusz Słowacki", "Stanisław Wyspiański", "Aleksander Fredro"));

        list.add(new KerdesModel("Melyik mű a magyar irodalom első színdarabja?",
                "A beszélő köntös", "A tragédia könyve", "Csigavér", "A fehér tigris", "A beszélő köntös"));

        list.add(new KerdesModel("Melyik költő alkotásához kapcsolódik a Bánk bán?",
                "Katona József", "Petőfi Sándor", "Arany János", "József Attila", "Katona József"));

        list.add(new KerdesModel("Melyik irodalmi mű kezdi a következő mondatokkal: 'A harciasan kedélyes lengyel ember volt Maciej Plichta, de annál őszintébb'?",
                "Fekete város", "A fekete obeliszk", "Tüskevár", "A Pál utcai fiúk", "Fekete város"));

        list.add(new KerdesModel("Ki írta a Fekete város című regényt?",
                "Szabó Magda", "Móricz Zsigmond", "József Attila", "Márai Sándor", "Móricz Zsigmond"));

        list.add(new KerdesModel("Melyik irodalmi korszak jellemzője a romantika?",
                "Az érzelmi túlfűtöttség és az egyéniségi vágy megjelenése", "A racionalizmus és az ész diktatúrája", "Az antikvitás visszatérése és az idealizmus", "Az elidegenedés és a modernizmus", "Az érzelmi túlfűtöttség és az egyéniségi vágy megjelenése"));


    }

    private void Tema_001() {
        list.add(new KerdesModel("Melyik irodalmi korszakot nevezzük a magyar irodalom aranykorának?",
                "Az irodalom aranykorának nevezzük", "A romantikát", "A realizmust", "A klasszicizmust", "Az irodalom aranykorának nevezzük"));
        list.add(new KerdesModel("Melyik költőnk műveit szokták az Arany évszázadhoz sorolni?",
                "Petőfi Sándor", "Ady Endre", "József Attila", "Arany János", "Arany János"));
        list.add(new KerdesModel("Ki írta az Egri csillagok című regényt?",
                "Gárdonyi Géza", "Móricz Zsigmond", "Kosztolányi Dezső", "Füst Milán", "Gárdonyi Géza"));
        list.add(new KerdesModel("Melyik műfajba soroljuk a Don Quijote-t?",
                "Regény", "Dráma", "Vers", "Elbeszélés", "Regény"));
        list.add(new KerdesModel("Ki az az amerikai írónő, aki a Szeleburdi családot írta?",
                "Esther Forbes", "J.K. Rowling", "Louisa May Alcott", "Mária Szepes", "Esther Forbes"));
        list.add(new KerdesModel("Ki a szerzője az Isten ostora című regénynek?",
                "György István", "Márai Sándor", "Szabó Magda", "Réz Pál", "György István"));

        list.add(new KerdesModel("Ki írta a Száz év magány című regényt?",
                "Gabriel García Márquez", "Mario Vargas Llosa", "Jorge Luis Borges", "Isabel Allende", "Gabriel García Márquez"));
        list.add(new KerdesModel("Melyik irodalmi irányzatot nevezik a régi görögök tragédiájának?",
                "Tragédia", "Komédia", "Eposz", "Líra", "Tragédia"));
        list.add(new KerdesModel("Ki írta az Édes Anna című regényt?",
                "Kosztolányi Dezső", "Móricz Zsigmond", "Kálmán Mikszáth", "Füst Milán", "Kosztolányi Dezső"));
        list.add(new KerdesModel("Melyik klasszikus irodalmi mű kezdi a következő sorokkal: 'Az emberiséghez képest iszonyúan nagy világűrben élünk egy iszonyúan kis bolygón'?",
                "Galaxis útikalauz stopposoknak", "A Gyűrűk Ura", "Az Idő rövid története", "Az élet kitaszítottjai", "Galaxis útikalauz stopposoknak"));
        list.add(new KerdesModel("Ki írta az Odüsszeiát?",
                "Homéros", "Szophoklész", "Euripidész", "Hesiodos", "Homéros"));
        list.add(new KerdesModel("Melyik irodalmi mű kezdődik a következő mondatokkal: 'Hamarosan beállt a csend. Csak a hóesés moraja hallatszott az elhagyatott kis kunyhóban. '",
                "Egri csillagok", "Egri csillagok", "Isten ostora", "Édes Anna", "Édes Anna"));
        list.add(new KerdesModel("Ki írta a Macskajátékot?",
                "Szigligeti Ede", "Jókai Mór", "Molnár Ferenc", "Móricz Zsigmond", "Szigligeti Ede"));
        list.add(new KerdesModel("Melyik költő írta a Himnuszt?",
                "Kölcsey Ferenc", "Arany János", "Petőfi Sándor", "Ady Endre", "Kölcsey Ferenc"));
        list.add(new KerdesModel("Ki az a francia író, aki az Az aranyhaj című regényt írta?",
                "Jean-Jacques Rousseau", "Charles Perrault", "Victor Hugo", "Jules Verne", "Charles Perrault"));


    }

}
