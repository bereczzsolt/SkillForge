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
/*
        list.add(new TemakorModel("Hunok"));
        list.add(new TemakorModel("Magyar államalapítás"));
        list.add(new TemakorModel("Árpád-ház"));
        list.add(new TemakorModel("Hunyadiak"));


*/
        Temakorok temak = new Temakorok(this, list);
        binding.TemakorokRecy.setAdapter(temak);



    }
}