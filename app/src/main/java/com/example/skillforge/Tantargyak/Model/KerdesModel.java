package com.example.skillforge.Tantargyak.Model;

import com.example.skillforge.Tantargyak.Kerdesek.TortenelemKerdesek;

public class KerdesModel extends TortenelemKerdesek {

    public String kerdes, opcio1,opcio2,opcio3,opcio4,jovalasz;

    public KerdesModel(String kerdes, String opcio1, String opcio2, String opcio3, String opcio4, String jovalasz) {
        this.kerdes = kerdes;
        this.opcio1 = opcio1;
        this.opcio2 = opcio2;
        this.opcio3 = opcio3;
        this.opcio4 = opcio4;
        this.jovalasz = jovalasz;



    }

    public KerdesModel(){

    }


    public String getKerdes() {
        return kerdes;
    }

    public void setKerdes(String kerdes) {
        this.kerdes = kerdes;
    }

    public String getOpcio1() {
        return opcio1;
    }

    public void setOpcio1(String opcio1) {
        this.opcio1 = opcio1;
    }

    public String getOpcio2() {
        return opcio2;
    }

    public void setOpcio2(String opcio2) {
        this.opcio2 = opcio2;
    }

    public String getOpcio3() {
        return opcio3;
    }

    public void setOpcio3(String opcio3) {
        this.opcio3 = opcio3;
    }

    public String getOpcio4() {
        return opcio4;
    }

    public void setOpcio4(String opcio4) {
        this.opcio4 = opcio4;
    }

    public String getJovalasz() {
        return jovalasz;
    }

    public void setJovalasz(String jovalasz) {
        this.jovalasz = jovalasz;
    }
}
