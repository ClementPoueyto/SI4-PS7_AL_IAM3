package com.aliam3.polyvilleactive.model.gamification;

/**
 * Classe qui représente un badge collectable
 * @author vivian
 * @author clement
 */
public class Badge {

    String name;
    String image;


    public Badge(String name, String image){
        this.name=name;
        this.image=image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
