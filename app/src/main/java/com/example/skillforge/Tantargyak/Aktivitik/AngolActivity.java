package com.example.skillforge.Tantargyak.Aktivitik;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.os.Bundle;
import com.example.skillforge.Aktivitik.Temakorok;
import com.example.skillforge.Tantargyak.Model.TemakorModel;
import com.example.skillforge.databinding.ActivityAngolBinding;
import java.util.ArrayList;

public class AngolActivity extends AppCompatActivity {
    ActivityAngolBinding binding;
    ArrayList<TemakorModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAngolBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        list = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.TemakorokRecy.setLayoutManager(linearLayoutManager);

        list.add(new TemakorModel("Angol-Magyar Szófordítás","Angol"));
        list.add(new TemakorModel("Angol Nyelvtan","Angol"));
        list.add(new TemakorModel("Olvasás és Értelmezés","Angol"));
        list.add(new TemakorModel("Szókincs és Szóhasználat","Angol"));
        list.add(new TemakorModel("Írás és Összeállítás","Angol"));
        list.add(new TemakorModel("Szövegértés","Angol"));
        list.add(new TemakorModel("Angol nyelvoktatási módszertan","Angol"));

        Temakorok temak = new Temakorok(this, list);
        binding.TemakorokRecy.setAdapter(temak);
    }
}