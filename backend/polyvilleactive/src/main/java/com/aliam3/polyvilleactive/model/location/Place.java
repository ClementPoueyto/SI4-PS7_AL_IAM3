package com.aliam3.polyvilleactive.model.location;

public class Place {
    private String adress;
    private Position pos;

    public Place(){

    }

    public Place(String adress, Position pos){
        this.pos=pos;
        this.adress=adress;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }
}
