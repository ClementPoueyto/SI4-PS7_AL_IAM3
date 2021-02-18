package com.aliam3.polyvilleactive.model.location;

import com.aliam3.polyvilleactive.exception.PositionException;
import com.aliam3.polyvilleactive.model.deserializer.PositionDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Objects;

@JsonDeserialize(using = PositionDeserializer.class)
public class Position {

    private double latitude;
    private double longitude;

    public Position(double lat, double lon){
        if(lat<90 && lat>-90) {
            this.latitude = lat;
        }
        else{
            throw new PositionException("wrong latitude, must be lat<90 && lat>-90");
        }
        if(lon<180&& lon>-180) {
            this.longitude = lon;
        }
        else{
            throw new PositionException("wrong latitude, must be lon<180&& lon>-180");        }
    }

    public String getNavitiaPosition(){
        return longitude+";"+latitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Position{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(longitude,latitude);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Position)) return false;
        Position other = (Position) obj;
        return (this.longitude==other.longitude) && (this.latitude==other.latitude);
    }
}
