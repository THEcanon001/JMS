package datatype;

import java.io.Serializable;
import java.util.List;

public class Contenedor implements Serializable {
    private List<PuntoExterno> puntos;
    private List<VehiculoExterno> vehiculos;
    private String param;

    public Contenedor() {
    }

    public Contenedor(List<PuntoExterno> puntos, List<VehiculoExterno> vehiculos) {
        this.puntos = puntos;
        this.vehiculos = vehiculos;
    }

    public List<PuntoExterno> getPuntos() {
        return puntos;
    }

    public void setPuntos(List<PuntoExterno> puntos) {
        this.puntos = puntos;
    }

    public List<VehiculoExterno> getVehiculos() {
        return vehiculos;
    }

    public void setVehiculos(List<VehiculoExterno> vehiculos) {
        this.vehiculos = vehiculos;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }
}
