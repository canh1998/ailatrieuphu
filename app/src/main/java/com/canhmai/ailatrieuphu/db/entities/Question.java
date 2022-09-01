package com.canhmai.ailatrieuphu.db.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Question {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "_id")
    public int id;
    public Question(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCaseA() {
        return caseA;
    }

    public void setCaseA(String caseA) {
        this.caseA = caseA;
    }

    public String getCaseB() {
        return caseB;
    }

    public void setCaseB(String caseB) {
        this.caseB = caseB;
    }

    public String getCaseC() {
        return caseC;
    }

    public void setCaseC(String caseC) {
        this.caseC = caseC;
    }

    public String getCaseD() {
        return caseD;
    }

    public void setCaseD(String caseD) {
        this.caseD = caseD;
    }

    public int getTrueCase() {
        return trueCase;
    }

    public void setTrueCase(int trueCase) {
        this.trueCase = trueCase;
    }

    public Question(int id, int level, String question, String caseA, String caseB, String caseC, String caseD, int trueCase) {
        this.id = id;
        this.level = level;
        this.question = question;
        this.caseA = caseA;
        this.caseB = caseB;
        this.caseC = caseC;
        this.caseD = caseD;
        this.trueCase = trueCase;
    }

    @ColumnInfo(name = "level")
    public int level;

    @ColumnInfo(name = "question")
    public String question;

    @ColumnInfo(name = "casea")
    public String caseA;

    @ColumnInfo(name = "caseb")
    public String caseB;

    @ColumnInfo(name = "casec")
    public String caseC;

    @ColumnInfo(name = "cased")
    public String caseD;

    @ColumnInfo(name = "truecase")
    public int trueCase;

    @NonNull
    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", level=" + level +
                ", question='" + question + '\'' +
                ", caseA='" + caseA + '\'' +
                ", caseB='" + caseB + '\'' +
                ", caseC='" + caseC + '\'' +
                ", caseD='" + caseD + '\'' +
                ", trueCase=" + trueCase +
                '}';
    }
}

