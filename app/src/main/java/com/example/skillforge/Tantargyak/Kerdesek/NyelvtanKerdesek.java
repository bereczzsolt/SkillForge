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
import com.example.skillforge.Tantargyak.Aktivitik.NyelvtanActivity;
import com.example.skillforge.Tantargyak.Model.KerdesModel;
import com.example.skillforge.databinding.ActivityKerdesekBinding;
import java.util.ArrayList;

public class NyelvtanKerdesek extends AppCompatActivity {
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
                case "Főnevek és főnévkifejezések":
                    Tema_1();
                    break;
                case "Igeidők és igemódok":
                    Tema_2();
                    break;
                case "Melléknevek és mellékmondatok":
                    Tema_3();
                    break;
                case "Határozószók és határozói mellékmondatok":
                    Tema_4();
                    break;
                case "Igenevek és igekötők":
                    Tema_5();
                    break;
                case "Szófajok és szószerkezetek":
                    Tema_6();
                    break;
                case "Mondattan és mondatfajták":
                    Tema_7();
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
                    Intent intent = new Intent(NyelvtanKerdesek.this, PontActivity.class);
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
                Dialog dialog = new Dialog(NyelvtanKerdesek.this);
                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.lejartido);
                dialog.findViewById(R.id.lejartidogomb).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Újra kezdeti a kérdésekkel
                        Intent intent = new Intent(NyelvtanKerdesek.this, NyelvtanActivity.class);
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

    private void Tema_7() {
        list.add(new KerdesModel("Mi a mondat definíciója?", "A mondat az a nyelvi egység, amely önmagában is értelmes kijelentést tartalmaz", "Az a nyelvi egység, amely az alany és a tárgy közötti viszonyt fejezi ki", "Az a nyelvi egység, amely a főnév és a melléknév közötti kapcsolatot mutatja be", "Az a nyelvi egység, amely a főnév és a névszó közötti viszonyt fejezi ki", "A mondat az a nyelvi egység, amely önmagában is értelmes kijelentést tartalmaz"));
        list.add(new KerdesModel("Melyek a mondat fő elemei?", "Alany, állítmány, tárgy", "Főnév, melléknév, igekötő", "Tárgy, határozószó, igekötő", "Időhatározó, helyhatározó, módhatározó", "Alany, állítmány, tárgy"));
        list.add(new KerdesModel("Mi az alany szerepe a mondatban?", "A mondatban megnevezett vagy jelzett személy, állat vagy dolog, amelyről valamit állítanak", "A mondatban megnevezett vagy jelzett hely vagy tárgy", "A mondatban lévő cselekvést vagy állapotot fejezi ki", "A mondatban megjelölt idő vagy hely", "A mondatban megnevezett vagy jelzett személy, állat vagy dolog, amelyről valamit állítanak"));
        list.add(new KerdesModel("Mi az állítmány szerepe a mondatban?", "A mondatban megfogalmazott vagy kijelentett dolog, cselekvés vagy állapot", "A mondatban megnevezett vagy jelzett hely vagy tárgy", "A mondatban lévő cselekvést vagy állapotot fejezi ki", "A mondatban megjelölt idő vagy hely", "A mondatban megfogalmazott vagy kijelentett dolog, cselekvés vagy állapot"));
        list.add(new KerdesModel("Mi a tárgy szerepe a mondatban?", "Az az alany által végzett vagy az állapoton átélt cselekvés tárgya", "Az az alany által végzett vagy az állapoton átélt cselekvés végzője", "Az a hely vagy tárgy, amelyen vagy amelyre a cselekvés történik", "Az a hely vagy tárgy, ahol vagy amelyen a cselekvés történik", "Az az alany által végzett vagy az állapoton átélt cselekvés tárgya"));
        list.add(new KerdesModel("Mi a mondatfajták fő csoportosítása?", "Kijelentő, felszólító, kérdő", "Alanyi, tárgyas, állítmányos", "Egyszerű, összetett, tagolt", "Főnévi, melléknévi, igeneves", "Kijelentő, felszólító, kérdő"));
        list.add(new KerdesModel("Melyik mondatfajta a leggyakoribb a mindennapi kommunikációban?", "Kijelentő mondat", "Felszólító mondat", "Kérdő mondat", "Összetett mondat", "Kijelentő mondat"));
        list.add(new KerdesModel("Mi a kijelentő mondat szerepe?", "Valamit kijelent vagy állít", "Valamit megkérdez", "Valamire felszólít", "Valamit megtagad", "Valamit kijelent vagy állít"));
        list.add(new KerdesModel("Mi a felszólító mondat szerepe?", "Valamire felszólít vagy kér", "Valamit megkérdez", "Valamit kijelent vagy állít", "Valamit megtagad", "Valamire felszólít vagy kér"));
        list.add(new KerdesModel("Mi a kérdő mondat szerepe?", "Valamit megkérdez", "Valamire felszólít vagy kér", "Valamit kijelent vagy állít", "Valamit megtagad", "Valamit megkérdez"));
        list.add(new KerdesModel("Melyik mondat az alábbiak közül kérdő?", "Holnap lesz a parti?", "Gyere ide!", "Nézd meg ezt a filmet!", "Ma nem megyek suliba.", "Holnap lesz a parti?"));
        list.add(new KerdesModel("Melyik mondat az alábbiak közül felszólító?", "Gyere ide!", "Holnap lesz a parti?", "Nézd meg ezt a filmet!", "Ma nem megyek suliba.", "Gyere ide!"));
        list.add(new KerdesModel("Melyik mondat az alábbiak közül kijelentő?", "Ma nem megyek suliba.", "Holnap lesz a parti?", "Gyere ide!", "Nézd meg ezt a filmet!", "Ma nem megyek suliba."));
        list.add(new KerdesModel("Mi a leggyakoribb mondatfajta az irodalmi művekben?", "Kijelentő mondat", "Felszólító mondat", "Kérdő mondat", "Összetett mondat", "Kijelentő mondat"));
        list.add(new KerdesModel("Melyik mondatfajta közvetíti a legtöbb információt?", "Összetett mondat", "Kijelentő mondat", "Felszólító mondat", "Kérdő mondat", "Összetett mondat"));

    }
    private void Tema_6() {
        list.add(new KerdesModel("Mi a szófajok definíciója?", "A nyelvi elemek csoportosítása jelentésük és szerepük alapján", "Azonos jelentésű szavak csoportja", "A főnév és melléknév közötti különbség", "A nyelvi elemek helyes kiejtésének szabályozása", "A nyelvi elemek csoportosítása jelentésük és szerepük alapján"));
        list.add(new KerdesModel("Hány fő szófajt különböztetünk meg a magyar nyelvben?", "Tizenegyet", "Hétet", "Tízet", "Tizenkettőt", "Tizenegyet"));
        list.add(new KerdesModel("Mi a főnév szerepe a mondatban?", "Tárgy vagy alany szerepét tölti be", "Cselekvést vagy történést fejez ki", "Minőséget vagy tulajdonságot jelöl", "Módot vagy körülményt mutat meg", "Tárgy vagy alany szerepét tölti be"));
        list.add(new KerdesModel("Mi a főnév jellemzője?", "Tárgy vagy alany szerepét tölti be", "Minőséget vagy tulajdonságot jelöl", "Módot vagy körülményt mutat meg", "Cselekvést vagy történést fejez ki", "Tárgy vagy alany szerepét tölti be"));
        list.add(new KerdesModel("Mi a melléknév szerepe a mondatban?", "Minőséget vagy tulajdonságot jelöl", "Tárgy vagy alany szerepét tölti be", "Cselekvést vagy történést fejez ki", "Módot vagy körülményt mutat meg", "Minőséget vagy tulajdonságot jelöl"));
        list.add(new KerdesModel("Mi a melléknév jellemzője?", "Minőséget vagy tulajdonságot jelöl", "Tárgy vagy alany szerepét tölti be", "Cselekvést vagy történést fejez ki", "Módot vagy körülményt mutat meg", "Minőséget vagy tulajdonságot jelöl"));
        list.add(new KerdesModel("Mi a határozószók szerepe a mondatban?", "Módot vagy körülményt mutatnak meg", "Tárgy vagy alany szerepét töltik be", "Cselekvést vagy történést fejeznek ki", "Minőséget vagy tulajdonságot jelölnek", "Módot vagy körülményt mutatnak meg"));
        list.add(new KerdesModel("Mi a határozószók jellemzője?", "Módot vagy körülményt mutatnak meg", "Tárgy vagy alany szerepét töltik be", "Cselekvést vagy történést fejeznek ki", "Minőséget vagy tulajdonságot jelölnek", "Módot vagy körülményt mutatnak meg"));
        list.add(new KerdesModel("Mi a névszók szerepe a mondatban?", "Tárgy vagy alany szerepét töltik be", "Minőséget vagy tulajdonságot jelölnek", "Cselekvést vagy történést fejeznek ki", "Módot vagy körülményt mutatnak meg", "Tárgy vagy alany szerepét töltik be"));
        list.add(new KerdesModel("Mi a névszók jellemzője?", "Tárgy vagy alany szerepét töltik be", "Minőséget vagy tulajdonságot jelölnek", "Cselekvést vagy történést fejeznek ki", "Módot vagy körülményt mutatnak meg", "Tárgy vagy alany szerepét töltik be"));
        list.add(new KerdesModel("Mi a névmások szerepe a mondatban?", "Tárgy vagy alany szerepét töltik be", "Minőséget vagy tulajdonságot jelölnek", "Cselekvést vagy történést fejeznek ki", "Módot vagy körülményt mutatnak meg", "Tárgy vagy alany szerepét töltik be"));
        list.add(new KerdesModel("Mi a névmások jellemzője?", "Tárgy vagy alany szerepét töltik be", "Minőséget vagy tulajdonságot jelölnek", "Cselekvést vagy történést fejeznek ki", "Módot vagy körülményt mutatnak meg", "Tárgy vagy alany szerepét töltik be"));
        list.add(new KerdesModel("Mi a szószerkezetek jellemzője?", "Az egyes szavak közötti kapcsolatot és szerkezetet írják le", "A szavak hangzását és kiejtését írják le", "A szavak jelentését írják le", "A szavak helyes használatát írják le", "Az egyes szavak közötti kapcsolatot és szerkezetet írják le"));
        list.add(new KerdesModel("Mi a predikátum szerepe a mondatban?", "Az alanyról vagy tárgyról mondott állítást vagy cselekvést fejezi ki", "Az alany vagy tárgy megnevezését tartalmazza", "A mondat alanyának vagy tárgyának nevét jelöli", "A mondatban lévő egyes szavak közötti kapcsolatot és szerkezetet írja le", "Az alanyról vagy tárgyról mondott állítást vagy cselekvést fejezi ki"));
        list.add(new KerdesModel("Mi a predikátum jellemzője?", "Az alanyról vagy tárgyról mondott állítást vagy cselekvést fejezi ki", "Az alany vagy tárgy megnevezését tartalmazza", "A mondat alanyának vagy tárgyának nevét jelöli", "A mondatban lévő egyes szavak közötti kapcsolatot és szerkezetet írja le", "Az alanyról vagy tárgyról mondott állítást vagy cselekvést fejezi ki"));

    }
    private void Tema_5() {
        list.add(new KerdesModel("Mi az ige definíciója?", "Az a szófaj, amely cselekvést, állapotot vagy történést fejez ki", "Az a szófaj, amely személyeket, dolgokat, helyeket vagy fogalmakat jelöl", "Az a szófaj, amely tulajdonságokat vagy minőségeket fejez ki", "Az a szófaj, amely időt vagy helyet jelöl", "Az a szófaj, amely cselekvést, állapotot vagy történést fejez ki"));
        list.add(new KerdesModel("Mi az ige fő jellemzője?", "Cselekvést, állapotot vagy történést fejez ki", "Személyeket, dolgokat, helyeket vagy fogalmakat jelöl", "Tulajdonságokat vagy minőségeket fejez ki", "Időt vagy helyet jelöl", "Cselekvést, állapotot vagy történést fejez ki"));
        list.add(new KerdesModel("Mi az ige fő feladata?", "A mondatban lévő alany cselekvésének, állapotának vagy történéseinek kifejezése", "A mondatban lévő tárgyak jelölése", "A mondatban lévő tulajdonságok vagy minőségek megadása", "A mondatban lévő idő vagy hely megjelölése", "A mondatban lévő alany cselekvésének, állapotának vagy történéseinek kifejezése"));
        list.add(new KerdesModel("Mi az igenevek jellemzője?", "Az ige különböző alakjai, amelyek különböző személyekre, számban, időben és módban utalnak", "Az igékhez kapcsolódó melléknevek", "Az igékhez kapcsolódó határozószavak", "Az igék különböző időhatározói", "Az ige különböző alakjai, amelyek különböző személyekre, számban, időben és módban utalnak"));
        list.add(new KerdesModel("Mi az ige személyei?", "Az ige alakjai, amelyek az alanyhoz kapcsolódnak", "Az ige alakjai, amelyek az időt és a módot fejezik ki", "Az ige alakjai, amelyek a tárgyhoz kapcsolódnak", "Az ige alakjai, amelyek a helyhez kapcsolódnak", "Az ige alakjai, amelyek az alanyhoz kapcsolódnak"));
        list.add(new KerdesModel("Melyik az alábbi szó egy ige?", "Fut", "Futó", "Futott", "Futás", "Fut"));
        list.add(new KerdesModel("Melyik az alábbi szó egy ige?", "Repül", "Repülő", "Repült", "Repülés", "Repül"));
        list.add(new KerdesModel("Melyik az alábbi szó egy igenevet jelöl?", "Futott", "Fut", "Futó", "Futás", "Futott"));
        list.add(new KerdesModel("Mi a leggyakoribb igenevek típusa?", "Az ige alanyragja", "Az ige igealakja", "Az ige tárgyragja", "Az ige határozószava", "Az ige alanyragja"));
        list.add(new KerdesModel("Mi a fő feladata az igekötőknek?", "Az igealakokhoz kapcsolódnak, és segítenek az ige személyének, számának, időének és módjának kifejezésében", "Az igék jelentését változtatják meg", "Az igék kiejtését segítik", "Az igék hangszínét változtatják meg", "Az igealakokhoz kapcsolódnak, és segítenek az ige személyének, számának, időének és módjának kifejezésében"));
        list.add(new KerdesModel("Melyik az alábbi szó egy igekötő?", "Kell", "Fut", "Futó", "Futott", "Kell"));
        list.add(new KerdesModel("Melyik az alábbi szó egy igekötő?", "Volt", "Fut", "Futó", "Futott", "Volt"));
        list.add(new KerdesModel("Mi a leggyakoribb igekötő a magyar nyelvben?", "Van", "Lesz", "Volt", "Vannak", "Van"));
        list.add(new KerdesModel("Mi a fő feladata az igekötőnek a mondatban?", "Az igealakokhoz kapcsolódva segítik az ige személyének, számának, időének és módjának kifejezését", "Módosítják az ige jelentését", "A mondat szórendjét változtatják", "Az ige hangszínét változtatják", "Az igealakokhoz kapcsolódva segítik az ige személyének, számának, időének és módjának kifejezését"));
        list.add(new KerdesModel("Mi a leggyakoribb igekötő a múlt időben a magyar nyelvben?", "Volt", "Lesz", "Van", "Vannak", "Volt"));

    }
    private void Tema_4() {
        list.add(new KerdesModel("Mi a határozószók definíciója?", "Azok a szavak, amelyek időt, helyet, módját vagy körülményeit fejezik ki egy cselekvésnek vagy állapotnak", "Azok a szavak, amelyek tulajdonságokat vagy minőségeket fejeznek ki", "Azok a szavak, amelyek cselekvést vagy állapotot fejeznek ki", "Azok a szavak, amelyek helyet vagy időt jelölnek", "Azok a szavak, amelyek időt, helyet, módját vagy körülményeit fejezik ki egy cselekvésnek vagy állapotnak"));
        list.add(new KerdesModel("Mi a határozói mellékmondat definíciója?", "Olyan mondatrész, amely egy másik mondatot kiegészítve kifejezi az időt, a helyet, a módját vagy a körülményeit egy cselekvésnek vagy állapotnak", "Olyan mondatrész, amely önállóan is értelmes mondatot alkot", "Olyan mondatrész, amely a főmondatot ismétli meg más szavakkal", "Olyan mondatrész, amely csak a főmondatot ismétli meg", "Olyan mondatrész, amely egy másik mondatot kiegészítve kifejezi az időt, a helyet, a módját vagy a körülményeit egy cselekvésnek vagy állapotnak"));
        list.add(new KerdesModel("Mi a határozószók leggyakoribb típusa?", "Időhatározók", "Helyhatározók", "Tulajdonsághatározók", "Alanyhatározók", "Időhatározók"));
        list.add(new KerdesModel("Mi a helyhatározók feladata?", "Megadni egy cselekvés vagy állapot helyét vagy irányát", "Megadni egy cselekvés vagy állapot idejét", "Megadni egy cselekvés vagy állapot módját vagy körülményeit", "Megadni egy cselekvés vagy állapot alanyát", "Megadni egy cselekvés vagy állapot helyét vagy irányát"));
        list.add(new KerdesModel("Mi a módhatározók szerepe?", "Megadni egy cselekvés vagy állapot módját vagy körülményeit", "Megadni egy cselekvés vagy állapot helyét vagy irányát", "Megadni egy cselekvés vagy állapot idejét", "Megadni egy cselekvés vagy állapot alanyát", "Megadni egy cselekvés vagy állapot módját vagy körülményeit"));
        list.add(new KerdesModel("Mi a helyhatározók közös jellemzője?", "Megadják egy cselekvés vagy állapot helyét vagy irányát", "Megadják egy cselekvés vagy állapot idejét", "Megadják egy cselekvés vagy állapot módját vagy körülményeit", "Megadják egy cselekvés vagy állapot alanyát", "Megadják egy cselekvés vagy állapot helyét vagy irányát"));
        list.add(new KerdesModel("Mi a módhatározók közös jellemzője?", "Megadják egy cselekvés vagy állapot módját vagy körülményeit", "Megadják egy cselekvés vagy állapot helyét vagy irányát", "Megadják egy cselekvés vagy állapot idejét", "Megadják egy cselekvés vagy állapot alanyát", "Megadják egy cselekvés vagy állapot módját vagy körülményeit"));
        list.add(new KerdesModel("Melyik az alábbi szó egy határozószó? \"májusban\"", "Időhatározó", "Helyhatározó", "Módhatározó", "Alanyhatározó", "Időhatározó"));
        list.add(new KerdesModel("Melyik az alábbi szó egy helyhatározó? \"alatt\"", "Helyhatározó", "Időhatározó", "Módhatározó", "Alanyhatározó", "Helyhatározó"));
        list.add(new KerdesModel("Melyik az alábbi szó egy módhatározó? \"távol\"", "Módhatározó", "Időhatározó", "Helyhatározó", "Alanyhatározó", "Módhatározó"));
        list.add(new KerdesModel("Melyik az alábbi szó egy időhatározó? \"gyakran\"", "Időhatározó", "Helyhatározó", "Módhatározó", "Alanyhatározó", "Időhatározó"));
        list.add(new KerdesModel("Mi a határozói mellékmondatok két fő típusa?", "Időhatározói és módhatározói mellékmondatok", "Helyhatározói és alanyhatározói mellékmondatok", "Módhatározói és alanyhatározói mellékmondatok", "Időhatározói és helyhatározói mellékmondatok", "Időhatározói és módhatározói mellékmondatok"));
        list.add(new KerdesModel("Mi a határozói mellékmondatok jellemzője?", "Egy másik mondatot kiegészítve kifejezik az időt, a helyet, a módját vagy a körülményeit egy cselekvésnek vagy állapotnak", "Önállóan is értelmes mondatok", "Csak a főmondatot ismétlik meg más szavakkal", "Csak a főmondatban szereplő szavakat tartalmazzák", "Egy másik mondatot kiegészítve kifejezik az időt, a helyet, a módját vagy a körülményeit egy cselekvésnek vagy állapotnak"));
        list.add(new KerdesModel("Milyen kérdéseket válaszolnak meg az időhatározói mellékmondatok?", "Mikor? Hány ideig? Mikor kezdődik vagy végződik az esemény?", "Hol? Milyen távolságra? Milyen irányba?", "Hogyan? Milyen módon?", "Ki? Mit? Kire vagy miről van szó?", "Mikor? Hány ideig? Mikor kezdődik vagy végződik az esemény?"));
        list.add(new KerdesModel("Milyen kérdéseket válaszolnak meg a helyhatározói mellékmondatok?", "Hol? Milyen távolságra? Milyen irányba?", "Mikor? Hány ideig? Mikor kezdődik vagy végződik az esemény?", "Hogyan? Milyen módon?", "Ki? Mit? Kire vagy miről van szó?", "Hol? Milyen távolságra? Milyen irányba?"));

    }
    private void Tema_3() {
        list.add(new KerdesModel("Mi a melléknév definíciója?", "Azok a szavak, amelyek tulajdonságokat vagy minőségeket fejeznek ki", "Azok a szavak, amelyek cselekvést vagy állapotot fejeznek ki", "Azok a szavak, amelyek helyet vagy időt jelölnek", "Azok a szavak, amelyek személyeket, dolgokat, helyeket vagy fogalmakat jelölnek", "Azok a szavak, amelyek tulajdonságokat vagy minőségeket fejeznek ki"));
        list.add(new KerdesModel("Mi a konkrét melléknév?", "Olyan melléknév, amely valós, érzékelhető tulajdonságokat vagy minőségeket fejez ki", "Olyan melléknév, amely absztrakt fogalmakat vagy érzéseket fejez ki", "Olyan melléknév, amely időt vagy helyet jelöl", "Olyan melléknév, amely személyeket vagy dolgokat jelöl", "Olyan melléknév, amely valós, érzékelhető tulajdonságokat vagy minőségeket fejez ki"));
        list.add(new KerdesModel("Mi az absztrakt melléknév?", "Olyan melléknév, amely absztrakt fogalmakat vagy érzéseket fejez ki", "Olyan melléknév, amely valós, érzékelhető tulajdonságokat vagy minőségeket fejez ki", "Olyan melléknév, amely időt vagy helyet jelöl", "Olyan melléknév, amely személyeket vagy dolgokat jelöl", "Olyan melléknév, amely absztrakt fogalmakat vagy érzéseket fejez ki"));
        list.add(new KerdesModel("Mi a határozott melléknév?", "A melléknév, amely egy meghatározott tulajdonságot vagy minőséget fejez ki", "A melléknév, amely általános dolgokat vagy tulajdonságokat jelöl", "A melléknév, amely időt vagy helyet jelöl", "A melléknév, amely személyeket vagy dolgokat jelöl", "A melléknév, amely egy meghatározott tulajdonságot vagy minőséget fejez ki"));
        list.add(new KerdesModel("Mi a határozatlan melléknév?", "A melléknév, amely általános dolgokat vagy tulajdonságokat jelöl", "A melléknév, amely egy meghatározott tulajdonságot vagy minőséget fejez ki", "A melléknév, amely időt vagy helyet jelöl", "A melléknév, amely személyeket vagy dolgokat jelöl", "A melléknév, amely általános dolgokat vagy tulajdonságokat jelöl"));
        list.add(new KerdesModel("Mi a közös melléknevek másik neve?", "Általános melléknév", "Határozott melléknév", "Tulajdonmelléknevek", "Középmelléknevek", "Általános melléknév"));
        list.add(new KerdesModel("Mi a melléknevek kategóriái közé tartozik?", "Színek", "Érzelmek", "Tulajdonságok", "Időpontok", "Tulajdonságok"));
        list.add(new KerdesModel("Mi a melléknév és a főnév kapcsolata?", "A melléknév kiegészíti vagy leírja a főnevet", "A melléknév és a főnév azonos jelentésűek", "A melléknév és a főnév összetett szó", "A melléknév és a főnév ellentétes jelentésűek", "A melléknév kiegészíti vagy leírja a főnevet"));
        list.add(new KerdesModel("Mi a jelzős szerkezet jellemzője?", "Olyan szerkezet, amely melléknévből és főnévből áll", "Olyan szerkezet, amely csak főnévből áll", "Olyan szerkezet, amely csak igéből áll", "Olyan szerkezet, amely csak névelőből áll", "Olyan szerkezet, amely melléknévből és főnévből áll"));
        list.add(new KerdesModel("Mi a mellékmondat jellemzője?", "Olyan mondat, amely a főmondatban van, és kiegészíti annak jelentését", "Olyan mondat, amely önállóan áll, és nem függ más mondatoktól", "Olyan mondat, amely csak egy igei szerkezet", "Olyan mondat, amely csak egy főnévi szerkezet", "Olyan mondat, amely a főmondatban van, és kiegészíti annak jelentését"));
        list.add(new KerdesModel("Mi a jelzői mellékmondat jellemzője?", "A főmondatot kiegészítő mondat, amelyben a mellékmondat a főnévre vagy melléknévre vonatkozik", "A főmondatot kiegészítő mondat, amelyben a mellékmondat az igére vagy igeidőre vonatkozik", "A főmondatot kiegészítő mondat, amelyben a mellékmondat a határozószóra vagy határozatlan névre vonatkozik", "A főmondatot kiegészítő mondat, amelyben a mellékmondat az alanyra vagy tárgyra vonatkozik", "A főmondatot kiegészítő mondat, amelyben a mellékmondat a főnévre vagy melléknévre vonatkozik"));
        list.add(new KerdesModel("Mi a teljes szerkezet jellemzője?", "A főmondatot kiegészítő mondat, amely önállóan is értelmes", "A főmondatot kiegészítő mondat, amely csak a főmondatra épül", "A főmondatot kiegészítő mondat, amelyet mindig az utolsó mondat után kell elhelyezni", "A főmondatot kiegészítő mondat, amely egy mondatrész", "A főmondatot kiegészítő mondat, amely önállóan is értelmes"));
        list.add(new KerdesModel("Mi a határozói mellékmondat jellemzője?", "A főmondatot kiegészítő mondat, amelyben a mellékmondat a határozószóra vagy határozatlan névre vonatkozik", "A főmondatot kiegészítő mondat, amelyben a mellékmondat a főnévre vagy melléknévre vonatkozik", "A főmondatot kiegészítő mondat, amelyben a mellékmondat az alanyra vagy tárgyra vonatkozik", "A főmondatot kiegészítő mondat, amelyben a mellékmondat az igére vagy igeidőre vonatkozik", "A főmondatot kiegészítő mondat, amelyben a mellékmondat a határozószóra vagy határozatlan névre vonatkozik"));
        list.add(new KerdesModel("Mi a körülírt mellékmondat jellemzője?", "A főmondatot kiegészítő mondat, amelyben a mellékmondat a főmondatra vonatkozik", "A főmondatot kiegészítő mondat, amely önállóan is értelmes", "A főmondatot kiegészítő mondat, amely csak a főmondatra épül", "A főmondatot kiegészítő mondat, amely csak a főmondatban szereplő szavakat tartalmazza", "A főmondatot kiegészítő mondat, amelyben a mellékmondat a főmondatra vonatkozik"));
        list.add(new KerdesModel("Mi a módhatározó mellékmondat szerepe?",
                "A főmondatban leírt cselekvés vagy állapot módját, körülményeit fejezi ki",
                "A főmondatban leírt dolog vagy személy tulajdonságait írja le",
                "A főmondat alanyának vagy tárgyának azon tulajdonságát fejezi ki, amely alapján az azonosítható",
                "A főmondatban leírt cselekvés vagy állapot célját mutatja be","A főmondatban leírt cselekvés vagy állapot módját, körülményeit fejezi ki"));

    }
    private void Tema_2() {
        list.add(new KerdesModel("Mi az igeidő definíciója?", "Az ige alakjának az időtartama", "Az ige módját jelző nyelvtani kategória", "Az ige alapvető jelentése", "Az igeidő jelzése a mondatban", "Az ige alakjának az időtartama"));
        list.add(new KerdesModel("Melyik az alábbi szó ige?", "Fut", "Ház", "Gyorsan", "Szép", "Fut"));
        list.add(new KerdesModel("Mi az alábbi kifejezés igeidője?", "Futott", "Fut", "Futás", "Futással", "Futott"));
        list.add(new KerdesModel("Mi a múlt idő jellemzője?", "Olyan idő, amely a múltban történt eseményeket vagy cselekedeteket jelöl", "Olyan idő, amely a jelenben zajló eseményeket vagy cselekedeteket jelöl", "Olyan idő, amely a jövőben bekövetkező eseményeket vagy cselekedeteket jelöl", "Olyan idő, amely a lehetőségeket vagy kívánságokat jelöl", "Olyan idő, amely a múltban történt eseményeket vagy cselekedeteket jelöl"));
        list.add(new KerdesModel("Mi a jelen idő jellemzője?", "Olyan idő, amely a jelenben zajló eseményeket vagy cselekedeteket jelöl", "Olyan idő, amely a múltban történt eseményeket vagy cselekedeteket jelöl", "Olyan idő, amely a jövőben bekövetkező eseményeket vagy cselekedeteket jelöl", "Olyan idő, amely a lehetőségeket vagy kívánságokat jelöl", "Olyan idő, amely a jelenben zajló eseményeket vagy cselekedeteket jelöl"));
        list.add(new KerdesModel("Mi a jövő idő jellemzője?", "Olyan idő, amely a jövőben bekövetkező eseményeket vagy cselekedeteket jelöl", "Olyan idő, amely a múltban történt eseményeket vagy cselekedeteket jelöl", "Olyan idő, amely a jelenben zajló eseményeket vagy cselekedeteket jelöl", "Olyan idő, amely a lehetőségeket vagy kívánságokat jelöl", "Olyan idő, amely a jövőben bekövetkező eseményeket vagy cselekedeteket jelöl"));
        list.add(new KerdesModel("Mi az alábbi mondat időmódja?", "Lehet, hogy esik az eső.", "Eszik az eső.", "Esőre van szükség.", "Esőben járunk.", "Lehet, hogy esik az eső."));
        list.add(new KerdesModel("Mi a feltételes mód jellemzője?", "Olyan időmód, amely a lehetőségeket vagy kívánságokat jelöli", "Olyan időmód, amely a múltban történt eseményeket vagy cselekedeteket jelöli", "Olyan időmód, amely a jelenben zajló eseményeket vagy cselekedeteket jelöli", "Olyan időmód, amely a jövőben bekövetkező eseményeket vagy cselekedeteket jelöli", "Olyan időmód, amely a lehetőségeket vagy kívánságokat jelöli"));
        list.add(new KerdesModel("Mi a következő mondat időmódja: \"Ha lenne időm, elmennék a koncertre.\"?", "Feltételes mód", "Múlt idő", "Jelen idő", "Jövő idő", "Feltételes mód"));
        list.add(new KerdesModel("Mi a kijelentő mód jellemzője?", "Olyan időmód, amely állításokat vagy tényeket közöl", "Olyan időmód, amely a lehetőségeket vagy kívánságokat jelöli", "Olyan időmód, amely a jövőben bekövetkező eseményeket vagy cselekedeteket jelöli", "Olyan időmód, amely a múltban történt eseményeket vagy cselekedeteket jelöli", "Olyan időmód, amely állításokat vagy tényeket közöl"));
        list.add(new KerdesModel("Mi az alábbi mondat időmódja?", "Minden reggel edzek.", "Edzek minden reggel.", "Edzve minden reggel.", "Edzés minden reggel.", "Minden reggel edzek."));
        list.add(new KerdesModel("Mi a felszólító mód jellemzője?", "Olyan időmód, amely utasításokat, kéréseket vagy felszólításokat közöl", "Olyan időmód, amely a lehetőségeket vagy kívánságokat jelöli", "Olyan időmód, amely a jelenben zajló eseményeket vagy cselekedeteket jelöli", "Olyan időmód, amely a jövőben bekövetkező eseményeket vagy cselekedeteket jelöli", "Olyan időmód, amely utasításokat, kéréseket vagy felszólításokat közöl"));
        list.add(new KerdesModel("Mi a parancsoló mód jellemzője?", "Olyan időmód, amely parancsokat vagy utasításokat közöl", "Olyan időmód, amely a lehetőségeket vagy kívánságokat jelöli", "Olyan időmód, amely a múltban történt eseményeket vagy cselekedeteket jelöli", "Olyan időmód, amely a jövőben bekövetkező eseményeket vagy cselekedeteket jelöli", "Olyan időmód, amely parancsokat vagy utasításokat közöl"));
        list.add(new KerdesModel("Mi az imperatív mód másik neve?", "Parancsoló mód", "Feltételes mód", "Kijelentő mód", "Felszólító mód", "Parancsoló mód"));
        list.add(new KerdesModel("Mi az alábbi mondat időmódja?", "Kérem, nyissa ki az ablakot!", "Nyitja ki az ablakot!", "Nyitva van az ablak.", "Nyitva lesz az ablak.", "Kérem, nyissa ki az ablakot!"));

    }
    private void Tema_1() {
        list.add(new KerdesModel("Mi a főnév definíciója?", "Azok a szavak, amelyek személyeket, dolgokat, helyeket vagy fogalmakat jelölnek", "Azok a szavak, amelyek cselekvést vagy állapotot fejeznek ki", "Azok a szavak, amelyek tulajdonságokat vagy minőségeket fejeznek ki", "Azok a szavak, amelyek helyet vagy időt jelölnek", "Azok a szavak, amelyek személyeket, dolgokat, helyeket vagy fogalmakat jelölnek"));
        list.add(new KerdesModel("Melyik az alábbi szó főnév?", "Ház", "Fut", "Gyorsan", "Szeret", "Ház"));
        list.add(new KerdesModel("Mi az alábbi kifejezés főnévkifejezés?", "A kék autó", "Fut az úton", "Gyorsan fut", "Szeretem a kutyákat", "A kék autó"));
        list.add(new KerdesModel("Mi a határozott főnév?", "A főnév, amely egy meghatározott dologra vagy személyre vonatkozik", "A főnév, amely általános dolgokat vagy személyeket jelöl", "A főnév, amely időt vagy helyet jelöl", "A főnév, amely valamely minőséget vagy tulajdonságot fejez ki", "A főnév, amely egy meghatározott dologra vagy személyre vonatkozik"));
        list.add(new KerdesModel("Mi a határozatlan főnév?", "A főnév, amely általános dolgokat vagy személyeket jelöl", "A főnév, amely egy meghatározott dologra vagy személyre vonatkozik", "A főnév, amely időt vagy helyet jelöl", "A főnév, amely valamely minőséget vagy tulajdonságot fejez ki", "A főnév, amely általános dolgokat vagy személyeket jelöl"));
        list.add(new KerdesModel("Mi a főnév főneve?", "A főnév, amely más főnevekből származik", "A főnév, amely valamely minőséget vagy tulajdonságot fejez ki", "A főnév, amely általános dolgokat vagy személyeket jelöl", "A főnév, amely egy meghatározott dologra vagy személyre vonatkozik", "A főnév, amely más főnevekből származik"));
        list.add(new KerdesModel("Melyik az alábbi kifejezés főnévkifejezés?", "A szép virágok", "Szépen jár", "Gyorsan fut", "Szeretem a kutyákat", "A szép virágok"));
        list.add(new KerdesModel("Mi a közös főnév?", "Általános dolgokat vagy személyeket jelölő főnév", "Egy meghatározott dologra vagy személyre vonatkozó főnév", "Valamely minőséget vagy tulajdonságot fejező főnév", "Időt vagy helyet jelölő főnév", "Általános dolgokat vagy személyeket jelölő főnév"));
        list.add(new KerdesModel("Mi a tulajdonnév?", "Egy meghatározott dologra vagy személyre vonatkozó főnév", "Általános dolgokat vagy személyeket jelölő főnév", "Valamely minőséget vagy tulajdonságot fejező főnév", "Időt vagy helyet jelölő főnév", "Egy meghatározott dologra vagy személyre vonatkozó főnév"));
        list.add(new KerdesModel("Mi a konkrét főnév?", "Olyan főnév, amely valós, érzékelhető tárgyakra vagy személyekre utal", "Olyan főnév, amely absztrakt fogalmakat vagy érzéseket fejez ki", "Olyan főnév, amely időt vagy helyet jelöl", "Olyan főnév, amely valamely minőséget vagy tulajdonságot fejez ki", "Olyan főnév, amely valós, érzékelhető tárgyakra vagy személyekre utal"));
        list.add(new KerdesModel("Mi az absztrakt főnév?", "Olyan főnév, amely absztrakt fogalmakat vagy érzéseket fejez ki", "Olyan főnév, amely valós, érzékelhető tárgyakra vagy személyekre utal", "Olyan főnév, amely időt vagy helyet jelöl", "Olyan főnév, amely valamely minőséget vagy tulajdonságot fejez ki", "Olyan főnév, amely absztrakt fogalmakat vagy érzéseket fejez ki"));
        list.add(new KerdesModel("Mi a közös nevek másik neve?", "Általános főnév", "Határozott főnév", "Tulajdonnév", "Középnév", "Általános főnév"));
        list.add(new KerdesModel("Mi a főnév kategóriái közé tartozik?", "Állatok", "Érzelmek", "Tárgyak", "Cselekvések", "Tárgyak"));
        list.add(new KerdesModel("Mi a főnév és a melléknév kapcsolata?", "A melléknév leírja vagy kiegészíti a főnevet", "A főnév és a melléknév szinonimái egymásnak", "A főnév és a melléknév azonos jelentéssel bír", "A főnév és a melléknév teljesen különböző fogalmakat fejez ki", "A melléknév leírja vagy kiegészíti a főnevet"));
        list.add(new KerdesModel("Mi a tulajdonsága a főneveknek?", "Azok a szavak, amelyek személyeket, dolgokat, helyeket vagy fogalmakat jelölnek", "Azok a szavak, amelyek cselekvést vagy állapotot fejeznek ki", "Azok a szavak, amelyek tulajdonságokat vagy minőségeket fejeznek ki", "Azok a szavak, amelyek helyet vagy időt jelölnek", "Azok a szavak, amelyek személyeket, dolgokat, helyeket vagy fogalmakat jelölnek"));

    }


}