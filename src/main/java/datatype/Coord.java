package datatype;

import java.io.Serializable;

public class Coord implements Serializable {
    private Double longitud;
    private Double latitud;

    public Coord() {
    }

    public Coord(Double longitud, Double latitud) {
        this.longitud = longitud;
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }
}
