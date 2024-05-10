package com.example.skillforge.Aktivitik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import com.example.skillforge.MainActivity;
import com.example.skillforge.Tantargyak.Aktivitik.TortenelemActivity;
import com.example.skillforge.databinding.ActivityPontBinding;

public class PontActivity extends AppCompatActivity {

    ActivityPontBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPontBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // A pontokat átveszük
        int osszespontszam = getIntent().getIntExtra("osszes",0);
        int helyesvalasz = getIntent().getIntExtra("pont",0);
        int roszvalasz = osszespontszam - helyesvalasz;

        // A pontokat megjelenítése az eredményben
        binding.osszeskerdesT.setText(String.valueOf(osszespontszam));
        binding.helyesvalaszT.setText(String.valueOf(helyesvalasz));
        binding.roszvalaszT.setText(String.valueOf(roszvalasz));


        // Új játék gomb
        binding.ujbelijatekgomb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PontActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
            //Kilépés gomb
            binding.kilepesgomb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
        });
    }
}