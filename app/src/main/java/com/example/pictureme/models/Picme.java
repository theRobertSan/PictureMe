package com.example.pictureme.models;

import com.google.firebase.firestore.DocumentId;

public class Picme {
    private String feeling;
    private boolean isFoodPic;

    public Picme() {
    }

    public Picme(String feeling, boolean isFoodPic) {
        this.feeling = feeling;
        this.isFoodPic = isFoodPic;
    }

    public String getFeeling() {
        return feeling;
    }

    public void setFeeling(String feeling) {
        this.feeling = feeling;
    }

    public boolean getIsFoodPic() {
        return isFoodPic;
    }

    public void setIsFoodPic(boolean isFoodPic) {
        this.isFoodPic = isFoodPic;
    }

    @Override
    public String toString() {
        return "Picme{" +
                "feeling='" + feeling + '\'' +
                ", isFoodPic=" + isFoodPic +
                '}';
    }
}
