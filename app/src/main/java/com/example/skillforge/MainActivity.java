package com.example.skillforge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import com.example.skillforge.Tantargyak.Aktivitik.AngolActivity;
import com.example.skillforge.Tantargyak.Aktivitik.InformatikaActivity;
import com.example.skillforge.Tantargyak.Aktivitik.IrodalomActivity;
import com.example.skillforge.Tantargyak.Aktivitik.MatematikaActivity;
import com.example.skillforge.Tantargyak.Aktivitik.NyelvtanActivity;
import com.example.skillforge.Tantargyak.Aktivitik.TortenelemActivity;
import com.example.skillforge.Tantargyak.Aktivitik.VegyesKerdesekActivity;


public class MainActivity extends AppCompatActivity {

    CardView Tortenelem, Informatika, Irodalom, Nyelvtan, Matematika, Angol, Vegyes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Tortenelem = findViewById(R.id.Tortenelem);
        Informatika = findViewById(R.id.Informatika);
        Irodalom = findViewById(R.id.Irodalom);
        Nyelvtan = findViewById(R.id.Nyelvtan);
        Matematika = findViewById(R.id.Matematika);
        Angol = findViewById(R.id.Angol);
        Vegyes = findViewById(R.id.Vegyes);

        Tortenelem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TortenelemActivity.class);
                startActivity(intent);
            }
        });

        Angol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AngolActivity.class);
                startActivity(intent);
            }
        });

        Informatika.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InformatikaActivity.class);
                startActivity(intent);
            }
        });


        Irodalom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, IrodalomActivity.class);
                startActivity(intent);
            }
        });

        Nyelvtan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NyelvtanActivity.class);
                startActivity(intent);
            }
        });

        Matematika.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MatematikaActivity.class);
                startActivity(intent);
            }
        });
        Vegyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, VegyesKerdesekActivity.class);
                startActivity(intent);
            }
        });

    }
}
