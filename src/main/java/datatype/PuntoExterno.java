package datatype;

import java.io.Serializable;
import java.util.List;

public class PuntoExterno implements Serializable {
    private String id;
    private Coord lugar;
    private double tiempoEspera;
    private List<String> vehiculos;
    private double dimension;
    private int horarioMin; //en minutos desde las 00
    private int horarioMax; //en minutos desde las 00
    private int prioridad;
    private String horaEstimada;

    public PuntoExterno() {
    }

    public PuntoExterno(String id, Coord lugar, double tiempoEspera, List<String> vehiculos, double dimension, int horarioMin, int horarioMax, int prioridad, String horaEstimada) {
        this.id = id;
        this.lugar = lugar;
        this.tiempoEspera = tiempoEspera;
        this.vehiculos = vehiculos;
        this.dimension = dimension;
        this.horarioMin = horarioMin;
        this.horarioMax = horarioMax;
        this.prioridad = prioridad;
        this.horaEstimada = horaEstimada;
    }

    @Override
    public String toString() {
        return "PuntoExterno{" +
                "id='" + id + '\'' +
                ", lugar=" + lugar +
                ", tiempoEspera=" + tiempoEspera +
                ", vehiculos=" + vehiculos +
                ", dimension=" + dimension +
                ", horarioMin=" + horarioMin +
                ", horarioMax=" + horarioMax +
                ", prioridad=" + prioridad +
                ", horaEstimada=" + horaEstimada +
                '}';
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

    public String getHoraEstimada() {
        return horaEstimada;
    }

    public void setHoraEstimada(String horaEstimada) {
        this.horaEstimada = horaEstimada;
    }
}
