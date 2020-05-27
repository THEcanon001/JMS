package datatype;

import java.io.Serializable;
import java.util.List;

public class Resultado implements Serializable {

    private List<DataPuntoVehiculo> solucion;

    public Resultado() {
    }

    public Resultado(List<DataPuntoVehiculo> solucion) {
        this.solucion = solucion;
    }

    public List<DataPuntoVehiculo> getSolucion() {
        return solucion;
    }

    public void setSolucion(List<DataPuntoVehiculo> solucion) {
        this.solucion = solucion;
    }
}
