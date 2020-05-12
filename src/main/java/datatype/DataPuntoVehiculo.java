package datatype;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataPuntoVehiculo implements Serializable {
    private VehiculoExterno VehiculoExterno;
    private List<PuntoExterno> PuntoExternos;

    private long secuencia = 0L;
    private static final String URL = "http://des25:5000/route/v1/driving/";
    private final static String PARAMETERS = "?overview=false";

    public DataPuntoVehiculo() {
    }

    public DataPuntoVehiculo(VehiculoExterno VehiculoExterno, List<PuntoExterno> PuntoExternos) {
        this.VehiculoExterno = VehiculoExterno;
        this.PuntoExternos = PuntoExternos;
    }

    public VehiculoExterno getVehiculoExterno() {
        return VehiculoExterno;
    }

    public void setVehiculoExterno(VehiculoExterno VehiculoExterno) {
        this.VehiculoExterno = VehiculoExterno;
    }

    public List<PuntoExterno> getPuntoExternos() {
        return PuntoExternos;
    }

    public void setPuntoExternos(List<PuntoExterno> PuntoExternos) {
        this.PuntoExternos = PuntoExternos;
    }

    public double calcularDistancia(VehiculoExterno vehiculoExterno, List<PuntoExterno> puntoExternos) {
        double result;
        if (puntoExternos.isEmpty() || vehiculoExterno == null) {
            return 0;
        }
        if (puntoExternos.size() == 1) {
            result = calcularTimeDistance(vehiculoExterno.getOrigen(), puntoExternos.get(0).getLugar()).getDistance();
            result += calcularTimeDistance(puntoExternos.get(0).getLugar(), vehiculoExterno.getDestino()).getDistance();
            return result;
        }

        result = calcularTimeDistance(vehiculoExterno.getOrigen(), puntoExternos.get(0).getLugar()).getDistance();
        for (int i = 0; i < puntoExternos.size() - 1; i++) {
            result += calcularTimeDistance(puntoExternos.get(i).getLugar(), puntoExternos.get(i+1).getLugar()).getDistance(); // De P1 a Pn-1
        }
        result += calcularTimeDistance(vehiculoExterno.getDestino(), puntoExternos.get(puntoExternos.size() - 1).getLugar()).getDistance(); // De Pn a destino de V
        return result;
    }

    private TimeDistance calcularTimeDistance(Coord origen, Coord destino) {
        HttpResponse<String> response;
        TimeDistance t = new TimeDistance();
        int i = 0;
        while (i == 0) {
            if (secuencia % 1000 == 0) {
                System.out.println(secuencia);
            }
            try {
                response = Unirest.get(URL + origen.getLatitud() + "," +  origen.getLongitud() + ";" + destino.getLatitud() + "," +  destino.getLongitud() + PARAMETERS)
                        .header("cache-control", "no-cache")
                        .asString();
                JSONObject json = new JSONObject(response.getBody());
                JSONArray results = json.getJSONArray("routes");
                JSONObject array = results.getJSONObject(0);
                double duration = array.getDouble("duration");
                double distance = array.getDouble("distance");
//            System.out.println("duracion: " + duration);
//            System.out.println("distancia: " + distance);
                t.setTime(duration / 60);
                t.setDistance(distance / 1000);
                i = 1;
                secuencia++;
            } catch (Exception ex) {
//                ex.printStackTrace();
                System.out.println("Fallo: " + ex.getMessage());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException exc) {
                    exc.printStackTrace();
                }
            }
        }
        return t;
    }

    @Override
    public String toString() {
        return "Grupo{" +
                "VehiculoExterno=" + VehiculoExterno +
                ", PuntoExternos=" + PuntoExternos +
                "}\n";
    }
}