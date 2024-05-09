package com.example.skillforge.Tantargyak.Model;

public class TemakorModel {
    private String setName;
    private String subjectName;

    public TemakorModel(String setName, String subjectName) {
        this.setName = setName;
        this.subjectName = subjectName;
    }

    public String getSetName() {
        return setName;
    }

    public void setSetName(String setName) {
        this.setName = setName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
}

