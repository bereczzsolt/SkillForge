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

        list.add(new TemakorModel("Algoritmusok és adatszerkezetek","Informatika"));
        list.add(new TemakorModel("Mesterséges intelligencia és gépi tanulás","Informatika"));
        list.add(new TemakorModel("Adatbázisrendszerek és adatkezelés","Informatika"));
        list.add(new TemakorModel("Hálózati technológiák és számítógép-hálózatok","Informatika"));
        list.add(new TemakorModel("Operációs rendszerek és rendszermenedzsment","Informatika"));
        list.add(new TemakorModel("Biztonság és adatvédelem az informatikában","Informatika"));
        list.add(new TemakorModel("Webfejlesztés és alkalmazások tervezése","Informatika"));
        Temakorok temak = new Temakorok(this, list);
        binding.TemakorokRecy.setAdapter(temak);



    }
}