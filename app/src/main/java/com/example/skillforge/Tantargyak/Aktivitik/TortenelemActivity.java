package com.example.skillforge.Tantargyak.Aktivitik;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.example.skillforge.Tantargyak.Model.TemakorModel;
import com.example.skillforge.Aktivitik.Temakorok;
import com.example.skillforge.databinding.ActivityTortenelemBinding;

import java.util.ArrayList;

public class TortenelemActivity extends AppCompatActivity {

    // Adatok lekötése
    ActivityTortenelemBinding binding;
    ArrayList<TemakorModel> list; // lista létrehozása

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTortenelemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        list = new ArrayList<>();

        // RecyclerView elrendezése
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.TemakorokRecy.setLayoutManager(linearLayoutManager);

        // Témák listához adása
        list.add(new TemakorModel("Hunok", "Történelem"));
        list.add(new TemakorModel("Magyar államalapítás", "Történelem"));
        list.add(new TemakorModel("Árpád-ház", "Történelem"));
        list.add(new TemakorModel("Hunyadiak", "Történelem"));
        list.add(new TemakorModel("1956-os forradalom és szabadságharc", "Történelem"));
        list.add(new TemakorModel("Első Világháború", "Történelem"));
        list.add(new TemakorModel("Második Világháború", "Történelem"));
        // Adapter létrehozása és beállítása a RecyclerView-hoz
        Temakorok temak = new Temakorok(this, list);
        binding.TemakorokRecy.setAdapter(temak);


    }
}