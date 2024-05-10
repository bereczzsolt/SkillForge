package com.example.skillforge.Tantargyak.Aktivitik;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.example.skillforge.Aktivitik.Temakorok;
import com.example.skillforge.Tantargyak.Model.TemakorModel;

import com.example.skillforge.databinding.ActivityVegyesKerdesekBinding;

import java.util.ArrayList;

public class VegyesKerdesekActivity extends AppCompatActivity {

    ActivityVegyesKerdesekBinding binding;

    ArrayList<TemakorModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityVegyesKerdesekBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        list = new ArrayList<>();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.TemakorokRecy.setLayoutManager(linearLayoutManager);

        // Témák listához adása
        list.add(new TemakorModel("Irodalom + Nyelvtan", "Vegyes"));
        list.add(new TemakorModel("Irodalom + Történelem", "Vegyes"));
        list.add(new TemakorModel("Érettségi tantárgyak vegyes kérdések: Első Teszt", "Vegyes"));
        list.add(new TemakorModel("Érettségi tantárgyak vegyes kérdések: Második Teszt", "Vegyes"));
        list.add(new TemakorModel("Érettségi tantárgyak vegyes kérdések: Harmadik Teszt", "Vegyes"));

        Temakorok temak = new Temakorok(this, list);
        binding.TemakorokRecy.setAdapter(temak);



    }
}
