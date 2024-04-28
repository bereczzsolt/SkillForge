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
        if (temanev.equals("Angol1")) {

            Tema_feladat1();

        } else if (temanev.equals("Angol2")) {
            Tema_feladat2();

        }else if (temanev.equals("Angol3")) {
            Tema_feladat3();
        }else if (temanev.equals("Angol4")) {
            Tema_feladat4();
        }else if (temanev.equals("Angol5")) {
            Tema_feladat5();
        }else if (temanev.equals("Angol6")) {
            Tema_feladat6();
        }else if (temanev.equals("Angol7")) {
            Tema_feladat7();
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
                        Intent intent = new Intent(AngolKerdesek.this, TortenelemActivity.class);
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
        list.add(new KerdesModel("Mi az angol megfelelője a \"autóbusz\"-nak?",
                "bus", "car", "train", "plane", "bus"));
        list.add(new KerdesModel("Mi az angol megfelelője a \"autóbusz\"-nak?",
                "bus", "car", "train", "plane", "bus"));

        list.add(new KerdesModel("Fordítsd le angolra a \"számítógép\" szót.",
                "computer", "television", "phone", "tablet", "computer"));

        list.add(new KerdesModel("Mi az angol megfelelője a \"macska\"-nak?",
                "cat", "dog", "horse", "bird", "cat"));

        list.add(new KerdesModel("Fordítsd le angolra a \"iskola\" szót.",
                "school", "hospital", "library", "bank", "school"));
    }

    private void  Tema_feladat6() {
        list.add(new KerdesModel("Mi az angol megfelelője a \"autóbusz\"-nak?",
                "bus", "car", "train", "plane", "bus"));
        list.add(new KerdesModel("Mi az angol megfelelője a \"autóbusz\"-nak?",
                "bus", "car", "train", "plane", "bus"));

        list.add(new KerdesModel("Fordítsd le angolra a \"számítógép\" szót.",
                "computer", "television", "phone", "tablet", "computer"));

        list.add(new KerdesModel("Mi az angol megfelelője a \"macska\"-nak?",
                "cat", "dog", "horse", "bird", "cat"));

        list.add(new KerdesModel("Fordítsd le angolra a \"iskola\" szót.",
                "school", "hospital", "library", "bank", "school"));
        }

    private void  Tema_feladat5() {
        list.add(new KerdesModel("Mi az angol megfelelője a \"autóbusz\"-nak?",
                "bus", "car", "train", "plane", "bus"));
        list.add(new KerdesModel("Mi az angol megfelelője a \"autóbusz\"-nak?",
                "bus", "car", "train", "plane", "bus"));

        list.add(new KerdesModel("Fordítsd le angolra a \"számítógép\" szót.",
                "computer", "television", "phone", "tablet", "computer"));

        list.add(new KerdesModel("Mi az angol megfelelője a \"macska\"-nak?",
                "cat", "dog", "horse", "bird", "cat"));

        list.add(new KerdesModel("Fordítsd le angolra a \"iskola\" szót.",
                "school", "hospital", "library", "bank", "school"));
    }
    private void  Tema_feladat3() {
        list.add(new KerdesModel("Mi az angol megfelelője a \"autóbusz\"-nak?",
                "bus", "car", "train", "plane", "bus"));
        list.add(new KerdesModel("Mi az angol megfelelője a \"autóbusz\"-nak?",
                "bus", "car", "train", "plane", "bus"));

        list.add(new KerdesModel("Fordítsd le angolra a \"számítógép\" szót.",
                "computer", "television", "phone", "tablet", "computer"));

        list.add(new KerdesModel("Mi az angol megfelelője a \"macska\"-nak?",
                "cat", "dog", "horse", "bird", "cat"));

        list.add(new KerdesModel("Fordítsd le angolra a \"iskola\" szót.",
                "school", "hospital", "library", "bank", "school"));
    }

    private void  Tema_feladat4() {
        list.add(new KerdesModel("Mi az angol megfelelője a \"autóbusz\"-nak?",
                "bus", "car", "train", "plane", "bus"));

        list.add(new KerdesModel("Fordítsd le angolra a \"számítógép\" szót.",
                "computer", "television", "phone", "tablet", "computer"));

        list.add(new KerdesModel("Mi az angol megfelelője a \"macska\"-nak?",
                "cat", "dog", "horse", "bird", "cat"));

        list.add(new KerdesModel("Fordítsd le angolra a \"iskola\" szót.",
                "school", "hospital", "library", "bank", "school"));

        list.add(new KerdesModel("Mi az angol megfelelője a \"szék\"-nek?",
                "chair", "table", "bed", "sofa", "chair"));

        list.add(new KerdesModel("Fordítsd le angolra a \"virág\" szót.",
                "flower", "tree", "river", "mountain", "flower"));

        list.add(new KerdesModel("Mi az angol megfelelője a \"ház\"-nak?",
                "house", "apartment", "building", "street", "house"));

        list.add(new KerdesModel("Fordítsd le angolra a \"könyv\" szót.",
                "book", "pen", "pencil", "paper", "book"));

        list.add(new KerdesModel("Mi az angol megfelelője a \"asztal\"-nak?",
                "table", "chair", "bed", "sofa", "table"));

        list.add(new KerdesModel("Fordítsd le angolra a \"folyó\" szót.",
                "river", "lake", "sea", "ocean", "river"));

        list.add(new KerdesModel("Mi az angol megfelelője az \"alma\"-nak?",
                "apple", "banana", "orange", "pear", "apple"));

        list.add(new KerdesModel("Fordítsd le angolra a \"törölköző\" szót.",
                "towel", "shirt", "pants", "shoes", "towel"));

        list.add(new KerdesModel("Mi az angol megfelelője a \"tévé\"-nek?",
                "TV", "radio", "computer", "phone", "TV"));

        list.add(new KerdesModel("Fordítsd le angolra a \"kutya\" szót.",
                "dog", "cat", "horse", "bird", "dog"));

        list.add(new KerdesModel("Mi az angol megfelelője a \"virág\"-nak?",
                "flower", "tree", "river", "mountain", "flower"));

    }

    private void Tema_feladat2() {
        list.add(new KerdesModel("Mi az a nyelvtani fogalom, amely meghatározza, hogy egy mondatban kinek vagy mire mutat egy cselekvés vagy állapot?",
                "Alany", "Tárgy", "Ige", "Melléknév", "Alany"));

        list.add(new KerdesModel("Melyik az a nyelvtani elem, amely a cselekvés időpontját vagy folyamatát határozza meg?",
                "Igeidő", "Alany", "Tárgy", "Melléknév", "Igeidő"));

        list.add(new KerdesModel("Melyik a következő mondatok közül egy szabályos angol ige?",
                "Run", "Runed", "Runing", "Runned", "Run"));

        list.add(new KerdesModel("Mi az a nyelvtani elem, amely az igéhez kapcsolódik és kifejezi a cselekvés vagy állapot körülményeit?",
                "Igekötő", "Melléknév", "Igeidő", "Alany", "Igekötő"));

        list.add(new KerdesModel("Melyik az alábbi mondatok közül egy birtokos jelző?",
                "My car is fast.", "I am happy.", "She likes music.", "The sky is blue.", "My car is fast."));

        list.add(new KerdesModel("Melyik az a nyelvtani elem, amely egy személy, állat vagy dolog tulajdonságait, tulajdonát, mennyiségét, fajtáját vagy működését fejezi ki?",
                "Melléknév", "Főnév", "Ige", "Igekötő", "Melléknév"));

        list.add(new KerdesModel("Mi a főnév angol megfelelője?",
                "Noun", "Verb", "Adjective", "Adverb", "Noun"));

        list.add(new KerdesModel("Melyik a következő mondatok közül egy főnév?",
                "Dog", "Running", "Beautiful", "Quickly", "Dog"));

        list.add(new KerdesModel("Mi az az alapvető nyelvtani elem, amely egy cselekvést vagy állapotot fejez ki?",
                "Ige", "Melléknév", "Főnév", "Igekötő", "Ige"));

        list.add(new KerdesModel("Melyik a következő mondatok közül egy melléknév?",
                "Happy", "Jumping", "Fast", "Quickly", "Happy"));

        list.add(new KerdesModel("Mi az a nyelvtani fogalom, amely meghatározza, hogy milyen időben történik egy cselekvés vagy állapot?",
                "Igeidő", "Főnév", "Melléknév", "Igekötő", "Igeidő"));

        list.add(new KerdesModel("Melyik a következő mondatok közül egy igekötő?",
                "Is", "Beautiful", "Jumping", "Quickly", "Is"));

        list.add(new KerdesModel("Mi az az alapvető nyelvtani elem, amely egy cselekvés vagy állapot időbeli kapcsolatát fejezi ki?",
                "Igeidő", "Főnév", "Melléknév", "Igekötő", "Igeidő"));

        list.add(new KerdesModel("Melyik a következő mondatok közül egy igekötő?",
                "Are", "Big", "Running", "Slowly", "Are"));

        list.add(new KerdesModel("Mi az a nyelvtani fogalom, amely meghatározza, hogy egy cselekvés vagy állapot ki vagy mi által történik?",
                "Tárgy", "Ige", "Melléknév", "Igeidő", "Tárgy"));


    }

    private void Tema_feladat1() {
        list.add(new KerdesModel("Mi a magyar szó az angol \"car\"-re?",
                "kocsi", "ház", "alma", "kenyér", "kocsi"));

        list.add(new KerdesModel("Fordítsd le \"apple\"-t magyarra.",
                "alma", "fa", "könyv", "autó", "alma"));

        list.add(new KerdesModel("Mi az angol szó a \"könyv\"-re?",
                "book", "table", "chair", "pen", "book"));

        list.add(new KerdesModel("Fordítsd le az \"számítógép\" szót angolra.",
                "computer", "house", "dog", "phone", "computer"));

        list.add(new KerdesModel("Mi az angol szó a \"kutya\"-ra?",
                "dog", "cat", "horse", "bird", "dog"));

        list.add(new KerdesModel("Fordítsd le a \"virág\"-ot angolra.",
                "flower", "tree", "river", "mountain", "flower"));

        list.add(new KerdesModel("Mi a jelentése az \"almafa\"-nak angolul?",
                "apple tree", "orange tree", "banana tree", "cherry tree", "apple tree"));

        list.add(new KerdesModel("Fordítsd le a \"törölköző\"-t angolra.",
                "towel", "shirt", "pants", "shoes", "towel"));

        list.add(new KerdesModel("Mi az angol szó az \"asztal\"-ra?",
                "table", "chair", "bed", "sofa", "table"));

        list.add(new KerdesModel("Fordítsd le az \"ablak\"-ot angolra.",
                "window", "door", "roof", "floor", "window"));

        list.add(new KerdesModel("Mi a jelentése a \"tévé\"-nek angolul?",
                "TV", "radio", "computer", "phone", "TV"));

        list.add(new KerdesModel("Fordítsd le az \"iskola\"-t angolra.",
                "school", "hospital", "bank", "library", "school"));

        list.add(new KerdesModel("Mi az angol szó a \"ház\"-ra?",
                "house", "apartment", "building", "street", "house"));

        list.add(new KerdesModel("Fordítsd le a \"szék\"-et angolra.",
                "chair", "table", "bed", "sofa", "chair"));

        list.add(new KerdesModel("Mi a jelentése a \"folyó\"-nak angolul?",
                "river", "lake", "sea", "ocean", "river"));

    }

}
