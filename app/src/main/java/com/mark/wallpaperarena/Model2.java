package com.mark.wallpaperarena;

public class Model2 {

    private int photo;
    private String text;

    public Model2(int photo, String text) {
        this.photo = photo;
        this.text = text;
    }

    public int getPhoto() {
        return photo;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return text;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public void setText(String text) {
        this.text = text;
    }
}
