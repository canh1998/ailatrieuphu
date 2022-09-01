package com.canhmai.ailatrieuphu.db.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "HighScore")
public class HighScore {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    public int id;

    @NonNull
    @ColumnInfo(name = "Name")
    public String name;

    @NonNull
    @ColumnInfo(name = "Score")
    public int scoreUser;

    public HighScore(@NonNull String name, int scoreUser) {
        this.name = name;
        this.scoreUser = scoreUser;
    }

    public HighScore() {
    }

    public HighScore(int id, @NonNull String name, int scoreUser) {
        this.id = id;
        this.name = name;
        this.scoreUser = scoreUser;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public int getScoreUser() {
        return scoreUser;
    }

    public void setScoreUser(int scoreUser) {
        this.scoreUser = scoreUser;
    }
}

