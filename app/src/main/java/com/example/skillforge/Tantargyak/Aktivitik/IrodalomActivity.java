package com.example.skillforge.Tantargyak.Aktivitik;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.example.skillforge.Aktivitik.Temakorok;
import com.example.skillforge.Tantargyak.Model.TemakorModel;
import com.example.skillforge.databinding.ActivityIrodalomBinding;

import java.util.ArrayList;

public class IrodalomActivity extends AppCompatActivity {

    ActivityIrodalomBinding binding;
    ArrayList<TemakorModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityIrodalomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        list = new ArrayList<>();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.TemakorokRecy.setLayoutManager(linearLayoutManager);
        // Témák listához adása
        list.add(new TemakorModel("Petőfi Sándor", "Irodalom"));
        list.add(new TemakorModel("Jókai Mór", "Irodalom"));
        list.add(new TemakorModel("Ady Endre", "Irodalom"));
        list.add(new TemakorModel("Kosztolányi Dezső", "Irodalom"));
        list.add(new TemakorModel("Móricz Zsigmond", "Irodalom"));
        list.add(new TemakorModel("Karinthy Frigyes", "Irodalom"));
        list.add(new TemakorModel("Szabó Magda ", "Irodalom"));

        Temakorok temak = new Temakorok(this, list);
        binding.TemakorokRecy.setAdapter(temak);



    }
}