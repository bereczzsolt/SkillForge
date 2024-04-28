package com.example.skillforge.Tantargyak.Aktivitik;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.example.skillforge.Aktivitik.Temakorok;
import com.example.skillforge.Tantargyak.Model.TemakorModel;
import com.example.skillforge.databinding.ActivityInformatikaBinding;

import java.util.ArrayList;

public class InformatikaActivity extends AppCompatActivity {

    ActivityInformatikaBinding binding;
    ArrayList<TemakorModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityInformatikaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        list = new ArrayList<>();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.TemakorokRecy.setLayoutManager(linearLayoutManager);

        list.add(new TemakorModel("TT"));
        list.add(new TemakorModel("Magyar államalapítás"));
        list.add(new TemakorModel("Árpád-ház"));
        list.add(new TemakorModel("Hunyadiak"));
        list.add(new TemakorModel("1956-os forradalom és szabadságharc"));
        list.add(new TemakorModel("Első Világháború"));
        list.add(new TemakorModel("Második Világháború"));


        Temakorok temak = new Temakorok(this, list);
        binding.TemakorokRecy.setAdapter(temak);



    }
}