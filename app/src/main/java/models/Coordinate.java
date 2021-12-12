package models;

public class Coordinate {
    private double longtitude;
    private double latitude;

    public Coordinate(double longtitude,double latitude){
        this.latitude = latitude;
        this.longtitude = longtitude;
    }
    public Coordinate(){}
    public double getLatitude() {
        return latitude;
    }
    public double getLongtitude() {
        return longtitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }
    @Override
    public String toString() {
        return "Coordinate{" +
                "longtitude=" + longtitude +
                ", latitude=" + latitude +
                '}';
    }
}
