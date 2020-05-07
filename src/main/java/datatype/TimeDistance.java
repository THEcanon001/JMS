package datatype;

import java.io.Serializable;

public class TimeDistance implements Serializable {
    private double time;
    private double distance;

    public TimeDistance() {
    }

    public TimeDistance(double time, double distance) {
        this.time = time;
        this.distance = distance;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
