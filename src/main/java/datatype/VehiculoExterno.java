package datatype;

import java.io.Serializable;
import java.util.List;

public class VehiculoExterno implements Serializable {
    private String id;
    private Coord origen;
    private int horaSalida;
    private Coord destino;
    private int horaLlegada;
    private double capacidad;
    private List<String> puntosFijos;

    public VehiculoExterno() {
    }

    public VehiculoExterno(String id, Coord origen, int horaSalida, Coord destino, int horaLlegada, double capacidad, List<String> puntosFijos) {
        this.id = id;
        this.origen = origen;
        this.horaSalida = horaSalida;
        this.destino = destino;
        this.horaLlegada = horaLlegada;
        this.capacidad = capacidad;
        this.puntosFijos = puntosFijos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Coord getOrigen() {
        return origen;
    }

    public void setOrigen(Coord origen) {
        this.origen = origen;
    }

    public int getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(int horaSalida) {
        this.horaSalida = horaSalida;
    }

    public Coord getDestino() {
        return destino;
    }

    public void setDestino(Coord destino) {
        this.destino = destino;
    }

    public int getHoraLlegada() {
        return horaLlegada;
    }

    public void setHoraLlegada(int horaLlegada) {
        this.horaLlegada = horaLlegada;
    }

    public double getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(double capacidad) {
        this.capacidad = capacidad;
    }

    public List<String> getPuntosFijos() {
        return puntosFijos;
    }

    public void setPuntosFijos(List<String> puntosFijos) {
        this.puntosFijos = puntosFijos;
    }
}
