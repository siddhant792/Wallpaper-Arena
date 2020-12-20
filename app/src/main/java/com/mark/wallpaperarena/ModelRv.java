package com.mark.wallpaperarena;

public class ModelRv {

    private String photo;

    public ModelRv(int animals, String photo) {
        this.photo = photo;
    }

    public ModelRv(int animals) {

    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}