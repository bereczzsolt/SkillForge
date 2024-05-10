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
                case "Algoritmusok és adatszerkezetek":
                    Tema_feladat10();
                    break;
                case "Mesterséges intelligencia és gépi tanulás":
                    Tema_feladat20();
                    break;
                case "Adatbázisrendszerek és adatkezelés":
                    Tema_feladat30();
                    break;
                case "Hálózati technológiák és számítógép-hálózatok":
                    Tema_feladat40();
                    break;
                case "Operációs rendszerek és rendszermenedzsment":
                    Tema_feladat50();
                    break;
                case "Biztonság és adatvédelem az informatikában":
                    Tema_feladat60();
                    break;
                case "Webfejlesztés és alkalmazások tervezése":
                    Tema_feladat70();
                    break;
                default:
                    // Ismeretlen téma kezelése
                    break;
            }
        }

        for (int i = 0; i < 4; i++) {

            binding.Valaszlehetosegek.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkAnswer((Button) view);
                }
            });
            playAnimation(binding.feltettkerdes, 0, list.get(helyzet).getKerdes());
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

                    Intent intent = new Intent(InformatikaKerdesek.this, PontActivity.class);
                    intent.putExtra("pont", pont);
                    intent.putExtra("osszes", list.size());
                    startActivity(intent);
                    finish();
                    return;
                }
                szamlalo = 0;
                playAnimation(binding.feltettkerdes,0, list.get(helyzet).getKerdes());



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
    private void  Tema_feladat70() {
        list.add(new KerdesModel("Mi a fő különbség a frontend és a backend fejlesztés között a webfejlesztésben?", "Frontend: a felhasználói felület fejlesztése, Backend: a szerveroldali logika fejlesztése", "Csak frontend fejlesztés létezik", "Csak backend fejlesztés létezik", "Csak adatbázisok kezelése", "Frontend: a felhasználói felület fejlesztése, Backend: a szerveroldali logika fejlesztése"));
        list.add(new KerdesModel("Mi a szerepe a HTML-nek a webfejlesztésben?", "Weboldal strukturálására és tartalmának megjelenítésére", "Csak adatok tárolása", "Csak alkalmazások futtatása", "Csak folyamatok kezelése", "Weboldal strukturálására és tartalmának megjelenítésére"));
        list.add(new KerdesModel("Melyik a legelterjedtebb stílusozási nyelv a webfejlesztésben?", "CSS (Cascading Style Sheets)", "HTML", "JavaScript", "Java", "CSS (Cascading Style Sheets)"));
        list.add(new KerdesModel("Mi a fő szerepe a JavaScriptnek a webfejlesztésben?", "Interaktív weboldalak készítése", "Csak adatok tárolása", "Csak folyamatok kezelése", "Csak hálózati kommunikáció", "Interaktív weboldalak készítése"));
        list.add(new KerdesModel("Mi a jelentősége a responsív webtervezésnek?", "A weboldalak megfelelő megjelenítése különböző eszközökön és méreteken", "Csak adatok tárolása", "Csak alkalmazások futtatása", "Csak hálózati kommunikáció", "A weboldalak megfelelő megjelenítése különböző eszközökön és méreteken"));
        list.add(new KerdesModel("Mi a fő különbség a statikus és a dinamikus weboldalak között?", "Statikus: állandó tartalom, Dinamikus: tartalom generálása futásidőben", "Csak statikus weboldalak léteznek", "Csak dinamikus weboldalak léteznek", "Csak adatbázisok kezelése", "Statikus: állandó tartalom, Dinamikus: tartalom generálása futásidőben"));
        list.add(new KerdesModel("Mi a fő szerepe az adatbázisoknak a webfejlesztésben?", "Adatok tárolása és kezelése", "Csak adatok tárolása", "Csak alkalmazások futtatása", "Csak folyamatok kezelése", "Adatok tárolása és kezelése"));
        list.add(new KerdesModel("Mi a jelentősége a felhasználói felület (UI) tervezésnek?", "A felhasználóbarát és intuitív felületek kialakítása", "Csak adatok tárolása", "Csak alkalmazások futtatása", "Csak hálózati kommunikáció", "A felhasználóbarát és intuitív felületek kialakítása"));
        list.add(new KerdesModel("Mi a fő szerepe a felhasználói élmény (UX) tervezésnek?", "A felhasználói interakciók és élmények javítása", "Csak adatok tárolása", "Csak alkalmazások futtatása", "Csak hálózati kommunikáció", "A felhasználói interakciók és élmények javítása"));
        list.add(new KerdesModel("Mi a jelentősége a webes biztonságnak a webfejlesztésben?", "Az adatok és felhasználók védelme illetéktelen hozzáférés és támadások ellen", "Csak adatok tárolása", "Csak alkalmazások futtatása", "Csak folyamatok kezelése", "Az adatok és felhasználók védelme illetéktelen hozzáférés és támadások ellen"));
        list.add(new KerdesModel("Mi a fő célja az alkalmazásarchitektúrának a webfejlesztésben?", "Az alkalmazás strukturális felépítésének meghatározása", "Csak adatok tárolása", "Csak alkalmazás futtatása", "Csak hálózati kommunikáció", "Az alkalmazás strukturális felépítésének meghatározása"));
        list.add(new KerdesModel("Milyen szerepe van az API-knak (Application Programming Interface) a webfejlesztésben?", "Adatok és szolgáltatások megosztása más alkalmazásokkal", "Csak adatok tárolása", "Csak alkalmazás futtatása", "Csak hálózati kommunikáció", "Adatok és szolgáltatások megosztása más alkalmazásokkal"));
        list.add(new KerdesModel("Mi a jelentősége az automatizált tesztelésnek a webfejlesztésben?", "A kód minőségének és funkcionalitásának ellenőrzése automatizált eszközökkel", "Csak adatok tárolása", "Csak alkalmazás futtatása", "Csak hálózati kommunikáció", "A kód minőségének és funkcionalitásának ellenőrzése automatizált eszközökkel"));
        list.add(new KerdesModel("Mi a fő szerepe a verziókezelésnek a webfejlesztésben?", "A kód változásainak nyomon követése és kezelése", "Csak adatok tárolása", "Csak alkalmazás futtatása", "Csak hálózati kommunikáció", "A kód változásainak nyomon követése és kezelése"));
        list.add(new KerdesModel("Mi a jelentősége az optimalizálásnak a webfejlesztésben?", "A weboldalak sebességének és teljesítményének javítása", "Csak adatok tárolása", "Csak alkalmazás futtatása", "Csak hálózati kommunikáció", "A weboldalak sebességének és teljesítményének javítása"));

    }

    private void  Tema_feladat60() {
        list.add(new KerdesModel("Mi a fő célja az informatikai biztonságnak?", "Az információk és rendszerek védelme illetéktelen hozzáférés és károkozás ellen", "Csak adatok tárolása", "Csak szoftverek futtatása", "Csak fájlok kezelése", "Az információk és rendszerek védelme illetéktelen hozzáférés és károkozás ellen"));
        list.add(new KerdesModel("Mi a jelentése a 'phishing' fogalomnak az informatikában?", "Adathalászat, hamis weboldalakon vagy e-mailekben személyes információk kiszedése", "Hardver hiba", "Csak adatok tárolása", "Csak szoftverek futtatása", "Adathalászat, hamis weboldalakon vagy e-mailekben személyes információk kiszedése"));
        list.add(new KerdesModel("Mi a fő különbség a vírus és a malware között?", "Vírus: önreprodukálódó, malware: általánosan kártékony szoftver", "Vírus: csak fájlokat fertőz, malware: csak hálózatokat támad", "Vírus: csak hálózatokat fertőz, malware: csak fájlokat támad", "Vírus: csak fájlokat és hálózatokat fertőz, malware: csak adatbázisokat támad", "Vírus: önreprodukálódó, malware: általánosan kártékony szoftver"));
        list.add(new KerdesModel("Mi a fő szerepe az antivirus szoftvereknek az informatikai biztonságban?", "Fertőzött fájlok és szoftverek felismerése és eltávolítása", "Csak adatok tárolása", "Csak hálózati kommunikáció", "Csak folyamatok kezelése", "Fertőzött fájlok és szoftverek felismerése és eltávolítása"));
        list.add(new KerdesModel("Mi jellemzi a tűzfalakat (firewall) az informatikában?", "Kontrollálja a hálózati forgalmat és védi a rendszert illetéktelen hozzáféréstől", "Csak adatok tárolása", "Csak fájlok kezelése", "Csak alkalmazások futtatása", "Kontrollálja a hálózati forgalmat és védi a rendszert illetéktelen hozzáféréstől"));
        list.add(new KerdesModel("Mi a fő célja a titkosításnak az informatikai biztonságban?", "Adatok védelme illetéktelen hozzáférés ellen", "Csak adatok tárolása", "Csak hálózati kommunikáció", "Csak folyamatok kezelése", "Adatok védelme illetéktelen hozzáférés ellen"));
        list.add(new KerdesModel("Mi a kétfaktoros azonosítás (2FA) szerepe az informatikai biztonságban?", "Extra védelem a felhasználók számára, mivel két különböző módon kell igazolniuk magukat", "Csak adatok tárolása", "Csak folyamatok kezelése", "Csak hálózati kommunikáció", "Extra védelem a felhasználók számára, mivel két különböző módon kell igazolniuk magukat"));
        list.add(new KerdesModel("Mi a fő célja az adatmentésnek az informatikában?", "Az adatok biztonságos másolatának készítése katasztrófák esetére", "Csak adatok tárolása", "Csak alkalmazások futtatása", "Csak folyamatok kezelése", "Az adatok biztonságos másolatának készítése katasztrófák esetére"));
        list.add(new KerdesModel("Mi a jelentősége a rendszeres frissítéseknek az informatikai biztonságban?", "Az operációs rendszer és alkalmazások biztonságának fenntartása", "Csak adatok tárolása", "Csak hálózati kommunikáció", "Csak folyamatok kezelése", "Az operációs rendszer és alkalmazások biztonságának fenntartása"));
        list.add(new KerdesModel("Mi jellemzi az adathalászatot az informatikában?", "Hamis weboldalakon vagy e-mailekben személyes információk kiszedése", "Csak adatok tárolása", "Csak fájlok kezelése", "Csak alkalmazások futtatása", "Hamis weboldalakon vagy e-mailekben személyes információk kiszedése"));
        list.add(new KerdesModel("Mi a fő szerepe a jelszavaknak az informatikai biztonságban?", "Felhasználók azonosítása és védelme", "Csak adatok tárolása", "Csak hálózati kommunikáció", "Csak folyamatok kezelése", "Felhasználók azonosítása és védelme"));
        list.add(new KerdesModel("Mi a fő célja a jogosultságkezelésnek az informatikai biztonságban?", "Hozzáférés korlátozása az adatokhoz és rendszerekhez", "Csak adatok tárolása", "Csak alkalmazások futtatása", "Csak hálózati kommunikáció", "Hozzáférés korlátozása az adatokhoz és rendszerekhez"));
        list.add(new KerdesModel("Mi a fő szerepe a rendszermonitorozásnak az informatikai biztonságban?", "Rendszeres figyelemmel kíséri a hálózati tevékenységet és lehetséges fenyegetéseket", "Csak adatok tárolása", "Csak alkalmazások futtatása", "Csak hálózati kommunikáció", "Rendszeres figyelemmel kíséri a hálózati tevékenységet és lehetséges fenyegetéseket"));
        list.add(new KerdesModel("Mi a fő szerepe a biztonsági mentésnek az informatikai biztonságban?", "Az adatok biztonságos másolatának készítése katasztrófák esetére", "Csak adatok tárolása", "Csak alkalmazások futtatása", "Csak folyamatok kezelése", "Az adatok biztonságos másolatának készítése katasztrófák esetére"));
        list.add(new KerdesModel("Mi a fő célja az adatkezelési irányelveknek az informatikai biztonságban?", "Az adatok biztonságos és jogos felhasználásának szabályozása", "Csak adatok tárolása", "Csak alkalmazások futtatása", "Csak folyamatok kezelése", "Az adatok biztonságos és jogos felhasználásának szabályozása"));

    }

    private void  Tema_feladat50() {
        list.add(new KerdesModel("Mi az operációs rendszer fő célja?", "Részletesen irányítja és felügyeli a számítógép hardverét és szoftverét", "Csak szoftverek futtatása", "Csak adatok tárolása", "Csak fájlok kezelése", "Részletesen irányítja és felügyeli a számítógép hardverét és szoftverét"));
        list.add(new KerdesModel("Milyen szerepe van a kernel-nek az operációs rendszerben?", "Alapvető funkciók és erőforrások kezelése", "Grafikus felhasználói felület biztosítása", "Csak fájlok tárolása", "Csak alkalmazások futtatása", "Alapvető funkciók és erőforrások kezelése"));
        list.add(new KerdesModel("Melyik az egyik legelterjedtebb operációs rendszer a személyi számítógépek terén?", "Windows", "MacOS", "Linux", "Unix", "Windows"));
        list.add(new KerdesModel("Mi a fő különbség a Windows és a Linux operációs rendszerek között?", "Windows: zárt forráskódú, Linux: nyílt forráskódú", "Windows: nyílt forráskódú, Linux: zárt forráskódú", "Windows: csak fizetős verziók, Linux: ingyenes", "Windows: csak Linux szerverekre telepíthető, Linux: csak Windows szerverekre telepíthető", "Windows: csak fizetős verziók, Linux: ingyenes"));
        list.add(new KerdesModel("Mi a fő szerepe a folyamatkezelésnek az operációs rendszerekben?", "Folyamatok létrehozása, végrehajtása és felügyelete", "Csak adatok tárolása", "Csak fájlok kezelése", "Csak hálózati kommunikáció", "Folyamatok létrehozása, végrehajtása és felügyelete"));
        list.add(new KerdesModel("Milyen típusú memóriát használ az operációs rendszer a folyamatok végrehajtásához?", "Virtuális memória", "Csak fizikai memória", "Csak cache memória", "Csak swap memória", "Virtuális memória"));
        list.add(new KerdesModel("Mi a fő szerepe a fájlrendszernek az operációs rendszerekben?", "Fájlok tárolása és kezelése", "Csak adatok tárolása", "Csak folyamatok kezelése", "Csak hálózati kommunikáció", "Fájlok tárolása és kezelése"));
        list.add(new KerdesModel("Mi az a GUI (Graphical User Interface) az operációs rendszerekben?", "Grafikus felhasználói felület", "Csak parancssoros felület", "Csak szöveges felület", "Csak menü alapú felület", "Grafikus felhasználói felület"));
        list.add(new KerdesModel("Melyik operációs rendszer használja alapértelmezésben a parancssoros felületet?", "Linux", "Windows", "MacOS", "Android", "Linux"));
        list.add(new KerdesModel("Mi a fő szerepe a rendszerleíró adatbázisnak az operációs rendszerekben?", "Konfigurációs információk tárolása", "Csak felhasználói adatok tárolása", "Csak fájlok tárolása", "Csak alkalmazások futtatása", "Konfigurációs információk tárolása"));
        list.add(new KerdesModel("Mi jellemzi a többszálú (multithreaded) folyamatokat az operációs rendszerekben?", "Egy folyamat több párhuzamos végrehajtott része", "Csak egy folyamat van", "Nincs párhuzamos végrehajtás", "Nem lehet futtatni a többszálú folyamatokat", "Egy folyamat több párhuzamos végrehajtott része"));
        list.add(new KerdesModel("Milyen szerepe van a szolgáltatásoknak az operációs rendszerekben?", "Különböző háttérben futó folyamatok biztosítása", "Csak fájlok tárolása", "Csak alkalmazások futtatása", "Csak adatok tárolása", "Különböző háttérben futó folyamatok biztosítása"));
        list.add(new KerdesModel("Mi a fő szerepe a processzkezelésnek az operációs rendszerekben?", "Folyamatok létrehozása, futtatása és felügyelete", "Csak adatok tárolása", "Csak fájlok kezelése", "Csak hálózati kommunikáció", "Folyamatok létrehozása, futtatása és felügyelete"));
        list.add(new KerdesModel("Mi a különbség a processz és a szál között az operációs rendszerekben?", "Egy folyamat több szálat tartalmazhat", "Nincs különbség, ugyanazt jelentik", "Csak egy szál lehet egy folyamatban", "Csak egy processz lehet egy szálban", "Egy folyamat több szálat tartalmazhat"));
        list.add(new KerdesModel("Milyen szerepe van a rendszereseményeknek az operációs rendszerekben?", "Fontos események jelzése és kezelése", "Csak fájlok tárolása", "Csak alkalmazások futtatása", "Csak adatok tárolása", "Fontos események jelzése és kezelése"));

    }
    private void  Tema_feladat40() {
        list.add(new KerdesModel("Mi a fő célja a hálózati technológiáknak?", "Kommunikáció lehetővé tétele számítógépek és eszközök között", "Csak adatok tárolása", "Csak szoftverek fejlesztése", "Csak adatbázisok létrehozása", "Kommunikáció lehetővé tétele számítógépek és eszközök között"));
        list.add(new KerdesModel("Melyik az egyik legelterjedtebb hálózati protokoll a világhálón?", "TCP/IP", "UDP", "HTTP", "FTP", "TCP/IP"));
        list.add(new KerdesModel("Milyen típusú hálózatot alkotnak a számítógépek és eszközök közötti közvetlen kapcsolatok?", "P2P (Peer-to-Peer)", "LAN (Local Area Network)", "WAN (Wide Area Network)", "MAN (Metropolitan Area Network)", "P2P (Peer-to-Peer)"));
        list.add(new KerdesModel("Mi a legfontosabb előnye a vezetékes hálózatoknak a vezeték nélküliekkel szemben?", "Magasabb sebesség és megbízhatóság", "Könnyebb telepítés", "Nagyobb mobilitás", "Kisebb költségek", "Magasabb sebesség és megbízhatóság"));
        list.add(new KerdesModel("Mi jellemzi a LAN (Local Area Network) hálózatokat?", "Kis területen belüli kommunikációt tesznek lehetővé", "Nagy területen belüli kommunikációt tesznek lehetővé", "Globális kommunikációt tesznek lehetővé", "Csak vezeték nélküli kommunikációt tesznek lehetővé", "Kis területen belüli kommunikációt tesznek lehetővé"));
        list.add(new KerdesModel("Mi a jelentése az IP (Internet Protocol) címnek?", "Egyedi azonosító a hálózati eszközöknek a hálózaton", "Weboldal neve", "E-mail cím", "Internet szolgáltató neve", "Egyedi azonosító a hálózati eszközöknek a hálózaton"));
        list.add(new KerdesModel("Mi a különbség a router és a switch között?", "Router: kapcsolatot teremt különböző hálózatok között, Switch: kapcsolatot teremt eszközök között ugyanabban a hálózatban", "Router: kapcsolatot teremt eszközök között ugyanabban a hálózatban, Switch: kapcsolatot teremt különböző hálózatok között", "Csak a switch használható vezeték nélküli hálózatokban", "Csak a router használható vezetékes hálózatokban", "Router: kapcsolatot teremt különböző hálózatok között, Switch: kapcsolatot teremt eszközök között ugyanabban a hálózatban"));
        list.add(new KerdesModel("Mi a fő szerepe a DHCP (Dynamic Host Configuration Protocol) szolgáltatásnak?", "Dinamikusan oszt IP-címeket a hálózati eszközöknek", "Biztonsági protokoll", "Weboldalak tárolása", "Adatbázisok létrehozása", "Dinamikusan oszt IP-címeket a hálózati eszközöknek"));
        list.add(new KerdesModel("Mi az az OSI (Open Systems Interconnection) modell?", "Hálózati kommunikációs modell rétegekkel", "Operációs rendszer", "Szoftverfejlesztési modell", "Adatbáziskezelési modell", "Hálózati kommunikációs modell rétegekkel"));
        list.add(new KerdesModel("Melyik réteg felelős a hálózati adatok fizikai továbbításáért?", "Fizikai réteg (Physical layer)", "Alkalmazási réteg (Application layer)", "Hálózati réteg (Network layer)", "Szállítási réteg (Transport layer)", "Fizikai réteg (Physical layer)"));
        list.add(new KerdesModel("Mi a fő szerepe az ARP (Address Resolution Protocol) protokollnak?", "IP-címek átalakítása hálózati eszközök fizikai címévé", "IP-címek átalakítása weboldalak címévé", "Fájlok tárolása hálózati eszközökön", "Weboldalak üzenetküldése", "IP-címek átalakítása hálózati eszközök fizikai címévé"));
        list.add(new KerdesModel("Milyen típusú hálózati topológiát alkalmaznak általában az irodai környezetben?", "Csillag (Star)", "Busz (Bus)", "Gyűrű (Ring)", "Hálózat nélküli (Ad-hoc)", "Csillag (Star)"));
        list.add(new KerdesModel("Mi a fő előnye a VPN (Virtual Private Network) használatának?", "Biztonságos, titkosított kommunikáció létrehozása nyilvános hálózatokon keresztül", "Gyorsabb internetkapcsolat biztosítása", "Ingyenes hálózati szolgáltatások nyújtása", "Nagyobb adatkapacitás biztosítása", "Biztonságos, titkosított kommunikáció létrehozása nyilvános hálózatokon keresztül"));
        list.add(new KerdesModel("Mi a fő szerepe a firewallnak a hálózati biztonságban?", "Szűri és ellenőrzi a hálózatra érkező és onnan távozó adatforgalmat", "Növeli az internetsebességet", "Tárolja az adatokat", "Nem befolyásolja a hálózati kommunikációt", "Szűri és ellenőrzi a hálózatra érkező és onnan távozó adatforgalmat"));
        list.add(new KerdesModel("Mi jellemzi a WAN (Wide Area Network) hálózatokat?", "Nagy területen belüli kommunikációt tesznek lehetővé", "Kis területen belüli kommunikációt tesznek lehetővé", "Csak vezeték nélküli kommunikációt tesznek lehetővé", "Csak szerverek kommunikációját teszik lehetővé", "Nagy területen belüli kommunikációt tesznek lehetővé"));

    }

    private void  Tema_feladat30() {
        list.add(new KerdesModel("Mi a fő célja az adatbázisrendszereknek?", "Hatékony adattárolás és kezelés", "Grafikus felhasználói felület kialakítása", "Weboldalak tervezése", "Alkalmazások fejlesztése", "Hatékony adattárolás és kezelés"));
        list.add(new KerdesModel("Mi a különbség az SQL és a NoSQL adatbázisok között?", "SQL: relációs adatbázisok, NoSQL: nem-relációs adatbázisok", "SQL: nem-relációs adatbázisok, NoSQL: relációs adatbázisok", "SQL: csak strukturált adatok kezelésére alkalmas, NoSQL: csak nem strukturált adatok kezelésére alkalmas", "SQL: csak nem strukturált adatok kezelésére alkalmas, NoSQL: csak strukturált adatok kezelésére alkalmas", "SQL: relációs adatbázisok, NoSQL: nem-relációs adatbázisok"));
        list.add(new KerdesModel("Milyen adattípusokat támogat az SQL?", "Szöveg, szám, dátum, idő stb.", "Csak számokat és szövegeket", "Képek és videók", "Csak strukturált adatokat", "Szöveg, szám, dátum, idő stb."));
        list.add(new KerdesModel("Mi a fő jellemzője a relációs adatbázisoknak?", "Táblák és kapcsolatok közötti összefüggések", "Nincsenek táblák, csak fájlok", "Csak egyetlen tábla van", "Nincs struktúrált adat", "Táblák és kapcsolatok közötti összefüggések"));
        list.add(new KerdesModel("Milyen típusú adatbázisokat sorolhatunk a NoSQL kategóriába?", "Dokumentum-alapú, kulcs-érték párok, oszlop-családok, gráf", "Csak relációs adatbázisok", "Csak táblázatos adatbázisok", "Csak objektum-alapú adatbázisok", "Dokumentum-alapú, kulcs-érték párok, oszlop-családok, gráf"));
        list.add(new KerdesModel("Mi az az ACID tulajdonságok a tranzakciókban?", "Atomicity, Consistency, Isolation, Durability", "Association, Consistency, Integration, Dependency", "Atomicity, Correctness, Integrity, Durability", "Association, Consistency, Isolation, Dependency", "Atomicity, Consistency, Isolation, Durability"));
        list.add(new KerdesModel("Milyen fő típusú tranzakciók vannak az adatbázisrendszerekben?", "Olvasás és írás", "Csak olvasás", "Csak írás", "Csak olvasás és írás", "Olvasás és írás"));
        list.add(new KerdesModel("Mi a szerepe az indexeknek az adatbázisokban?", "Gyorsítják a lekérdezéseket", "Csökkentik az adatbázis méretét", "Növelik az adatbázis sebességét", "Nem befolyásolják a lekérdezéseket", "Gyorsítják a lekérdezéseket"));
        list.add(new KerdesModel("Milyen fő típusú indexeket ismerünk az adatbázisokban?", "B-tree, hash, rendezett", "Csak hash", "Csak rendezett", "Csak B-tree", "B-tree, hash, rendezett"));
        list.add(new KerdesModel("Mi az a NoSQL adatbázisok fő előnye a relációs adatbázisokhoz képest?", "Skálázhatóság és rugalmasság", "Alacsonyabb költség", "Nagyobb adatbiztonság", "Könnyebb kezelhetőség", "Skálázhatóság és rugalmasság"));
        list.add(new KerdesModel("Milyen szerepe van a triggereknek az adatbázisokban?", "Automatikusan elindulnak bizonyos események bekövetkezésekor", "Csökkentik az adatbázis teljesítményét", "Csökkentik a tranzakciók biztonságát", "Nem befolyásolják az adatbázis működését", "Automatikusan elindulnak bizonyos események bekövetkezésekor"));
        list.add(new KerdesModel("Mi jellemzi a dokumentum-alapú adatbázisokat?", "Struktúrált adatok tárolása dokumentumok formájában", "Csak szöveges adatok tárolása", "Csak számok tárolása", "Nem strukturált adatok tárolása", "Struktúrált adatok tárolása dokumentumok formájában"));
        list.add(new KerdesModel("Mi a fő célja az adatbázisnormalizációnak?", "Csökkenti az adatbázis redundanciáját és inkonzisztenciáját", "Növeli az adatbázis teljesítményét", "Növeli az adatbázis méretét", "Nem befolyásolja az adatbázis szerkezetét", "Csökkenti az adatbázis redundanciáját és inkonzisztenciáját"));
        list.add(new KerdesModel("Mi jellemzi az oszlop-családokat a NoSQL adatbázisokban?", "Összetett adatstruktúrák tárolása", "Csak egy adattípus tárolása", "Csak szöveges adatok tárolása", "Nem strukturált adatok tárolása", "Összetett adatstruktúrák tárolása"));
        list.add(new KerdesModel("Milyen típusú adatbázisrendszert alkalmaznak főként pénzügyi tranzakciók rögzítésére?", "Tranzakcionális adatbázisrendszerek", "Dokumentum-alapú adatbázisrendszerek", "Kulcs-érték tárolók", "Oszlop-család adatbázisrendszerek", "Tranzakcionális adatbázisrendszerek"));

    }

    private void Tema_feladat20() {

            list.add(new KerdesModel("Mi az a mesterséges intelligencia (AI) célja?", "Gépek emberi gondolkodásának szimulálása", "Számítógépek javítása", "Adatbázisok létrehozása", "Weboldalak tervezése", "Gépek emberi gondolkodásának szimulálása"));
            list.add(new KerdesModel("Melyik algoritmus csoportozza össze az adatokat hasonlóság alapján anélkül, hogy előzetesen megadnánk a csoportok számát?", "K-means", "Döntési fák", "SVM", "Neurális hálózatok", "K-means"));
            list.add(new KerdesModel("Melyik típusú gépi tanulás használja az előre meghatározott bemeneti-jel/kimeneti-párokat a tanuláshoz?",
                    "Felügyelt tanulás", "Felügyelet nélküli tanulás", "Megerősítéses tanulás", "Fél-felügyelt tanulás", "Felügyelt tanulás"));
            list.add(new KerdesModel("Melyik gépi tanulási módszer segít a modell megértésében, hogy melyik bemenetek befolyásolják a kimenetet?",
                    "Interpretálható gépi tanulás", "Deep learning", "Reinforcement learning", "SVM", "Interpretálható gépi tanulás"));
            list.add(new KerdesModel("Mi a fő célja a felügyelt tanulásnak a gépi tanulásban?",
                    "A rendszer tanítása az előre meghatározott bemeneti-jel/kimeneti-párokból", "A rendszer tanítása bármilyen bemeneti adatból", "A rendszer tanítása új dolgok kitalálására", "A rendszer tanítása az előre meghatározott kimeneti értékekből", "A rendszer tanítása az előre meghatározott bemeneti-jel/kimeneti-párokból"));
            list.add(new KerdesModel("Melyik gépi tanulási technika használja a környezeti interakciót a tanuláshoz és a javításhoz?",
                    "Megerősítéses tanulás", "Unsupervised learning", "Supervised learning", "Semi-supervised learning", "Megerősítéses tanulás"));
            list.add(new KerdesModel("Melyik algoritmus használja az optikai neuronok működését a tanuláshoz?",
                    "Neurális hálózatok", "Döntési fák", "K-mean", "SVM", "Neurális hálózatok"));
            list.add(new KerdesModel("Mi az a Deep Learning?",
                    "Olyan gépi tanulási módszer, amely nagyon mély neurális hálózatokat használ", "Egyszerű gépi tanulási módszer", "Gépi tanulási módszer az emberek számára", "Alapvető gépi tanulási módszer", "Olyan gépi tanulási módszer, amely nagyon mély neurális hálózatokat használ"));
            list.add(new KerdesModel("Mi a fő célja a felügyelet nélküli tanulásnak a gépi tanulásban?",
                    "A rendszer tanítása bármilyen bemeneti adatból anélkül, hogy kimeneti értékeket adnánk meg", "A rendszer tanítása az előre meghatározott bemeneti-jel/kimeneti-párokból", "A rendszer tanítása az előre meghatározott kimeneti értékekből", "A rendszer tanítása az előre meghatározott bemeneti értékekből", "A rendszer tanítása bármilyen bemeneti adatból anélkül, hogy kimeneti értékeket adnánk meg"));
            list.add(new KerdesModel("Melyik gépi tanulási módszer használja az előre meghatározott bemeneti-jel/kimeneti-párokat a tanuláshoz, de csak részben kapott felügyeletet?",
                    "Fél-felügyelt tanulás", "Felügyelt tanulás", "Felügyelet nélküli tanulás", "Megerősítéses tanulás", "Fél-felügyelt tanulás"));
            list.add(new KerdesModel("Mi a fő célja a megerősítéses tanulásnak a gépi tanulásban?",
                    "A rendszer tanítása az ösztönző alapú viselkedés megtanulására és javítására a környezettel való interakció során", "A rendszer tanítása új dolgok kitalálására", "A rendszer tanítása az előre meghatározott bemeneti-jel/kimeneti-párokból", "A rendszer tanítása bármilyen bemeneti adatból", "A rendszer tanítása az ösztönző alapú viselkedés megtanulására és javítására a környezettel való interakció során"));
            list.add(new KerdesModel("Mi a fő célja a mély tanulásnak (Deep Learning) a gépi tanulásban?",
                    "Komplex feladatok megoldása többszintű, hierarchikus reprezentációk segítségével", "Egyszerű feladatok megoldása egy szintű reprezentáció segítségével", "Adatbázisok létrehozása", "Weboldalak tervezése", "Komplex feladatok megoldása többszintű, hierarchikus reprezentációk segítségével"));
            list.add(new KerdesModel("Melyik algoritmusok használják a Gradient Descent módszert a paraméterek optimalizálására?",
                "Neurális hálózatok", "K-means", "Döntési fák", "SVM", "Neurális hálózatok"));
            list.add(new KerdesModel("Mi a szerepe a validációnak a gépi tanulásban?",
                "Azért van, hogy értékeljük a modell teljesítményét olyan adatokon, amelyek nem szerepeltek a tanító adatokban", "Azért van, hogy tanítsuk a modellt", "Azért van, hogy előállítsuk a modellt", "Azért van, hogy vizsgáljuk a tanító adatokat", "Azért van, hogy értékeljük a modell teljesítményét olyan adatokon, amelyek nem szerepeltek a tanító adatokban"));
            list.add(new KerdesModel("Mi a fő célja a generatív modellezésnek a gépi tanulásban?",
                "Az adatok generálása új, hiteles adatok létrehozásához", "Az adatok elemzése", "Az adatok osztályozása", "Az adatok optimalizálása", "Az adatok generálása új, hiteles adatok létrehozásához"));
    }

    private void Tema_feladat10() {
        list.add(new KerdesModel("Mi az a bináris keresés?", "Effektív keresés rendezett listában", "Rendezetlen listák keresése", "Gyors keresés tetszőleges sorrendben", "Rendezetlen listák keresése", "Effektív keresés rendezett listában"));
        list.add(new KerdesModel("Mi a szerepe a veremnek?", "LIFO adatszerkezet", "FIFO adatszerkezet", "Összetett adatszerkezet", "Kereső algoritmus", "LIFO adatszerkezet"));
        list.add(new KerdesModel("Milyen műveletek végezhetők a veremmel?", "Push és Pop", "Add és Remove", "Get és Set", "Insert és Delete", "Push és Pop"));
        list.add(new KerdesModel("Mi jellemzi a buborékrendezést?", "Összehasonlításokon alapuló rendezési algoritmus", "O(1) futási idő", "Részben rendezett adatokra alkalmazható", "Nem igényel extra memóriát", "Összehasonlításokon alapuló rendezési algoritmus"));
        list.add(new KerdesModel("Mi az a lineáris keresés?", "Elem keresése listában sorrendben", "Gyors keresés rendezett listában", "Elem keresése rendezetlen listában", "Elem keresése rendezett listában", "Elem keresése listában sorrendben"));
        list.add(new KerdesModel("Mi a jelentősége a rekurziónak az algoritmusokban?", "Egyszerűsíti a problémákat", "Hatékonyabbá teszi az algoritmusokat", "Segíti az algoritmusok megértését", "Lehetővé teszi az önmagában hivatkozó algoritmusokat", "Lehetővé teszi az önmagában hivatkozó algoritmusokat"));
        list.add(new KerdesModel("Mi jellemzi a bináris fát?", "Két gyökérelemmel rendelkezik", "Bináris relációkat ábrázol", "Gyorsan keres rendezett adatokban", "Nem lehet üres", "Gyorsan keres rendezett adatokban"));
        list.add(new KerdesModel("Mi az az O(n) jelölés az algoritmusokban?", "Legrosszabb esetű futási idő", "Átlagos futási idő", "Legjobb esetű futási idő", "A futási idő nem befolyásolja", "Legrosszabb esetű futási idő"));
        list.add(new KerdesModel("Mi a szerepe a sorban?", "FIFO adatszerkezet", "LIFO adatszerkezet", "Részben rendezett adatszerkezet", "Összetett adatszerkezet", "FIFO adatszerkezet"));
        list.add(new KerdesModel("Milyen műveletek végezhetők a sorral?", "Add és Remove", "Push és Pop", "Enqueue és Dequeue", "Insert és Delete", "Enqueue és Dequeue"));
        list.add(new KerdesModel("Mi jellemzi a gyorsrendezést?", "Hatékony, osztott algoritmus", "Lassú, buborék algoritmus", "Kupacokat használ", "Nem igényel extra memóriát", "Hatékony, osztott algoritmus"));
        list.add(new KerdesModel("Mi az a hash függvény?", "Egyértelműen hozzárendel adatokat kulcsokhoz", "Adatokat tömörít", "Rendezett adatokat ad vissza", "Szétdobálja az adatokat", "Egyértelműen hozzárendel adatokat kulcsokhoz"));
        list.add(new KerdesModel("Milyen típusú problémákra alkalmazható a dinamikus programozás?", "Optimalizálási problémákra", "Lineáris keresésre", "Rekurzív problémákra", "Adatbázisokat kezel", "Optimalizálási problémákra"));
        list.add(new KerdesModel("Mi a szerepe az algoritmusok és adatszerkezetek hatékony megértésének?", "Optimalizálja a programokat", "Segít megérteni az algoritmusok működését", "Nem befolyásolja a programozást", "Csökkenti az algoritmusok rugalmasságát", "Segít megérteni az algoritmusok működését"));
        list.add(new KerdesModel("Melyik adatszerkezet tárolja az elemeket a FIFO (First In, First Out) elv alapján?", "Sor", "Verem", "Bináris fa", "Keresőfa", "Sor"));

    }

}