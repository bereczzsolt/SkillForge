package com.example.skillforge.Aktivitik;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skillforge.R;
import com.example.skillforge.Tantargyak.Aktivitik.InformatikaActivity;
import com.example.skillforge.Tantargyak.Aktivitik.MatematikaActivity;
import com.example.skillforge.Tantargyak.Aktivitik.NyelvtanActivity;
import com.example.skillforge.Tantargyak.Kerdesek.AngolKerdesek;
import com.example.skillforge.Tantargyak.Kerdesek.IrodalomKerdesek;
import com.example.skillforge.Tantargyak.Model.TemakorModel;
import com.example.skillforge.Tantargyak.Kerdesek.TortenelemKerdesek;
import com.example.skillforge.databinding.TemakorokBinding;

import java.util.ArrayList;

public class Temakorok extends RecyclerView.Adapter<Temakorok.viewHolder> {
    Context context;
    ArrayList<TemakorModel>list;
    public Temakorok(Context context, ArrayList<TemakorModel> list) {
        this.context = context;
        this.list = list;
    }



    // Új nézet a RecylerView-ban
    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.temakorok,parent,false);

        return new viewHolder(view);

    }
    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        final TemakorModel model = list.get(position);
        holder.binding.TemaNev.setText(model.getSetName());

        // Intent létrehozása a témakör kérdések megjelenítéséhez
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                switch (model.getSubjectName()) {
                    case "Történelem":
                        intent = new Intent(context, TortenelemKerdesek.class);
                        break;
                    case "Angol":
                        intent = new Intent(context, AngolKerdesek.class);
                        break;
                    case "Informatika":
                        intent = new Intent(context, InformatikaActivity.class);
                        break;
                    case "Nyelvtan":
                        intent = new Intent(context, NyelvtanActivity.class);
                        break;
                    case "Matematika":
                        intent = new Intent(context, MatematikaActivity.class);
                        break;
                    case "Irodalom":
                        intent = new Intent(context, IrodalomKerdesek.class);
                        break;

                    default:
                        throw new IllegalStateException("Nincs ilyen tantárgy! " + model.getSetName());
                }
                intent.putExtra("tema", model.getSetName());
                context.startActivity(intent);
            }
        });
    }


    /*

    //Nézet megjelítése, adatok hozzárendelése
    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        final TemakorModel model = list.get(position);
        //Témakör neve
        holder.binding.TemaNev.setText(model.getSetName());

        // Intent létrehozása a témakör kérdések megjelenítéséhez
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TortenelemKerdesek.class);
                intent.putExtra("tema",model.getSetName());
                context.startActivity(intent);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AngolKerdesek.class);
                intent.putExtra("tema2",model.getSetName());
                context.startActivity(intent);
            }
        });
/*
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, InformatikaActivity.class);
                intent.putExtra("tema",model.getSetName());
                context.startActivity(intent);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NyelvtanActivity.class);
                intent.putExtra("tema",model.getSetName());
                context.startActivity(intent);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MatematikaActivity.class);
                intent.putExtra("tema",model.getSetName());
                context.startActivity(intent);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, IrodalomKerdesek.class);
                intent.putExtra("tema",model.getSetName());
                context.startActivity(intent);
            }
        });


    }
*/

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        TemakorokBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = TemakorokBinding.bind(itemView);
        }
    }


}
