package com.example.skillforge.Tantargyak.Aktivitik;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.example.skillforge.Aktivitik.Temakorok;
import com.example.skillforge.Tantargyak.Model.TemakorModel;
import com.example.skillforge.databinding.ActivityNyelvtanBinding;

import java.util.ArrayList;

public class NyelvtanActivity extends AppCompatActivity {

    ActivityNyelvtanBinding binding;
    ArrayList<TemakorModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNyelvtanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        list = new ArrayList<>();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.TemakorokRecy.setLayoutManager(linearLayoutManager);


   // Témák listához adása
        list.add(new TemakorModel("Főnevek és főnévkifejezések", "Nyelvtan"));
        list.add(new TemakorModel("Igeidők és igemódok", "Nyelvtan"));
        list.add(new TemakorModel("Melléknevek és mellékmondatok", "Nyelvtan"));
        list.add(new TemakorModel("Határozószók és határozói mellékmondatok", "Nyelvtan"));
        list.add(new TemakorModel("Igenevek és igekötők", "Nyelvtan"));
        list.add(new TemakorModel("Szófajok és szószerkezetek", "Nyelvtan"));
        list.add(new TemakorModel("Mondattan és mondatfajták", "Nyelvtan"));

        Temakorok temak = new Temakorok(this, list);
        binding.TemakorokRecy.setAdapter(temak);



    }
}


