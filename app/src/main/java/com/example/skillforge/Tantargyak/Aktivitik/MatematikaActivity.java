package com.example.skillforge.Tantargyak.Aktivitik;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.example.skillforge.Aktivitik.Temakorok;
import com.example.skillforge.Tantargyak.Model.TemakorModel;
import com.example.skillforge.databinding.ActivityMatematikaBinding;

import java.util.ArrayList;

public class MatematikaActivity extends AppCompatActivity {

    ActivityMatematikaBinding binding;
    ArrayList<TemakorModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMatematikaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        list = new ArrayList<>();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.TemakorokRecy.setLayoutManager(linearLayoutManager);

   // Témák listához adása
        list.add(new TemakorModel("Algebra", "Matematika"));
        list.add(new TemakorModel("Geometria", "Matematika"));
        list.add(new TemakorModel("Számelmélet", "Matematika"));
        list.add(new TemakorModel("Analízis", "Matematika"));
        list.add(new TemakorModel("Valószínűségszámítás és statisztika", "Matematika"));
        list.add(new TemakorModel("Diszkrét matematika", "Matematika"));
        list.add(new TemakorModel("Lineáris algebra", "Matematika"));


        Temakorok temak = new Temakorok(this, list);
        binding.TemakorokRecy.setAdapter(temak);



    }
}