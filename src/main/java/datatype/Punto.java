package datatype;

import java.io.Serializable;
import java.util.List;

public class Punto implements Serializable {
    private String id;
    private Coord lugar;
    private double tiempoEspera;
    private List<String> vehiculos;
    private double dimension;
    private int horarioMin; //en segundos
    private int horarioMax; //en segundos
    private int prioridad;

    public Punto() {
    }

    public Punto(String id, Coord lugar, double tiempoEspera, List<String> vehiculos, double dimension, int horarioMin, int horarioMax, int prioridad) {
        this.id = id;
        this.lugar = lugar;
        this.tiempoEspera = tiempoEspera;
        this.vehiculos = vehiculos;
        this.dimension = dimension;
        this.horarioMin = horarioMin;
        this.horarioMax = horarioMax;
        this.prioridad = prioridad;
    }

    public Coord getLugar() {
        return lugar;
    }

    public void setLugar(Coord lugar) {
        this.lugar = lugar;
    }

    public double getTiempoEspera() {
        return tiempoEspera;
    }

    public void setTiempoEspera(double tiempoEspera) {
        this.tiempoEspera = tiempoEspera;
    }

    public List<String> getVehiculos() {
        return vehiculos;
    }

    public void setVehiculos(List<String> vehiculos) {
        this.vehiculos = vehiculos;
    }

    public double getDimension() {
        return dimension;
    }

    public void setDimension(double dimension) {
        this.dimension = dimension;
    }

    public int getHorarioMin() {
        return horarioMin;
    }

    public void setHorarioMin(int horarioMin) {
        this.horarioMin = horarioMin;
    }

    public int getHorarioMax() {
        return horarioMax;
    }

    public void setHorarioMax(int horarioMax) {
        this.horarioMax = horarioMax;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }


}
