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

        list.add(new TemakorModel("Angol1","Angol"));
        list.add(new TemakorModel("Angol2","Angol"));
        list.add(new TemakorModel("Angol3","Angol"));
        list.add(new TemakorModel("Angol4","Angol"));
        list.add(new TemakorModel("Angol5","Angol"));
        list.add(new TemakorModel("Angol6","Angol"));
        list.add(new TemakorModel("Angol7","Angol"));

        Temakorok temak = new Temakorok(this, list);
        binding.TemakorokRecy.setAdapter(temak);
    }
}