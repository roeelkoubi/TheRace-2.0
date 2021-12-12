package models;

public class Score {
    private long timeLasted;
    private Coordinate coordinate;

    public Score(long score, Coordinate coordinate) {
        this.timeLasted = score;
        this.coordinate = coordinate;
    }

    public long getTimeLasted() {
        return timeLasted;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public void setTimeLasted(long timeLasted) {
        this.timeLasted = timeLasted;
    }

    public Score() { }

    @Override
    public String toString() {
        return "Score{" +
                "timeLasted=" + timeLasted +
                '}';
    }
}
