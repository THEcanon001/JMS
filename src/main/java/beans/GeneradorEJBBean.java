package beans;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import datatype.*;
import javafx.util.Pair;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.MersenneTwisterFast;
import utils.R;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


@Stateless
@LocalBean
public class GeneradorEJBBean {
    private final static String OUT_PATH = "C:/Users/christiang/Desktop/tutanka.txt";
    private long secuencia = 0L;
    private static final String latitud_str = "-34.";
    private static final String longuitud_str = "-56.";
    private final static int min = 718391;
    private final static int max = 872259;
    private final static int max_2 = 331384;
    private final static int min_2 = 38339;
    private final static int tiempoMin = 1;
    private final static int tiempoMax = 60;
    private final static int DMINP = 500;
    private final static int DMAXP = 2500;
    private final static int DMINV = 5000;
    private final static int DMAXV = 10000;
    private final static int HMIN = 480;  //8am
    private final static int HMAX = 1080; //6pm
    private final static int maxV = 15;
    private final static int maxP = 250;
    private final static int minPrio = 10;
    List<PuntoExterno> puntoExternos;

    public void generarInstancia(int p, int v) throws UnirestException {
        if (p == 0 || v == 0) {
            p = generarCantPuntos();
            v = generarCantVehiculos();
        }
        puntoExternos = generarListaPuntos(p, v);
        List<VehiculoExterno> vehiculoExternos = generarListaVehiculos(v, p);
        imprimir(puntoExternos, vehiculoExternos);
        enviar(puntoExternos, vehiculoExternos);

    }

    private void imprimir(List<PuntoExterno> puntoExternos, List<VehiculoExterno> vehiculoExternos) {
        Contenedor contenedor = new Contenedor(puntoExternos, vehiculoExternos);
        String json = new Gson().toJson(contenedor);
        System.out.println(json);
        String pto;
        String vcl;
        try {
            File file = new File(OUT_PATH);
            //Creo o actualizo el archivo
            if (file.createNewFile()) {
                System.out.println("Se creea el archivo con la salida");
            } else {
                System.out.println("Archivo actualizado con la nueva salida.");
            }
            //Escribo el contenido del archivo
            DecimalFormat formato1 = new DecimalFormat("#.00");
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            writer.write("Puntos: ");
            writer.newLine();
            for(PuntoExterno p :contenedor.getPuntos()){
                int id = Integer.parseInt(p.getId());
                pto= "IDE: "+p.getId()+"\nID: "+id+"\nTiempo E:" + minutosHoras(p.getTiempoEspera())+ "\nHora Min:"+minutosHoras(p.getHorarioMin()) + "\nHora Max:" +minutosHoras(p.getHorarioMax()) +
                        "\nPrio:"+p.getPrioridad() + "\nCapacidad:" + p.getDimension() + "\nVehiculos permitidos:" + p.getVehiculos().toString()+"\n";
                writer.write(pto);
                writer.newLine();
            }
            writer.write("Vehiculos: ");
            writer.newLine();
            for(VehiculoExterno v :contenedor.getVehiculos()){
                int id = Integer.parseInt(v.getId());
                vcl= "IDE: "+v.getId()+"\nID: "+id+"\nHora salida:" + minutosHoras(v.getHoraSalida())+"\nHora llegada:" +minutosHoras(v.getHoraLlegada())+ "\nCapacidad:" +v.getCapacidad()+"\nPuntosFijos" +v.getPuntosFijos().toString()+"\n";
                writer.write(vcl);
                writer.newLine();
            }
            writer.close();
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void enviar(List<PuntoExterno> puntoExternos, List<VehiculoExterno> vehiculoExternos) throws UnirestException {
        Contenedor contenedor = new Contenedor(puntoExternos, vehiculoExternos);
        Unirest.setTimeouts(0, 0);
        Gson gson = new Gson();
        try {
            Unirest.post("http://localhost:8080/ruteo-ws-1.0-SNAPSHOT/rest/ruteows/ruta")
                    .header("Content-Type", "application/json")
                    .body(gson.toJson(contenedor))
                    .asString();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private List<VehiculoExterno> generarListaVehiculos(int cantV, int cantP) {
        List<VehiculoExterno> vehiculoExternos = new ArrayList<>();
        VehiculoExterno v;
        for (int i = 1; i <= cantV; i++) {
            v = new VehiculoExterno();
            v.setId("" + i);
            v.setOrigen(generarUbicacion());
            v.setDestino(generarUbicacion());
            v.setHoraSalida(generarHora());
            v.setHoraLlegada(generarHora());
            while (v.getHoraSalida() > v.getHoraLlegada() || Math.abs(v.getHoraLlegada() - v.getHoraSalida()) < 180 || v.getHoraSalida() > 600 || v.getHoraLlegada() < 900) {
                v.setHoraSalida(generarHora());
                v.setHoraLlegada(generarHora());
            }
            v.setCapacidad(generarCapacidad());
            v.setPuntosFijos(generarPuntos(v, cantP, vehiculoExternos));
            vehiculoExternos.add(v);
        }
        comprobarPuntosFijos(vehiculoExternos, cantP);
        return vehiculoExternos;
    }

    private void comprobarPuntosFijos(List<VehiculoExterno> vehiculoExternos, int cantP) {
        List<String> a_remover = new ArrayList<>();
        for(VehiculoExterno v:vehiculoExternos){
            a_remover = new ArrayList<>();
            for(VehiculoExterno v1: vehiculoExternos){
                if(!v.getId().equals(v1.getId())){
                    for(String pfv :v.getPuntosFijos()){
                        for(String pfv1 :v1.getPuntosFijos()){
                            if(pfv.equals(pfv1))
                                a_remover.add(pfv1);
                        }
                    }
                    for(String r :a_remover){
                        if(v1.getPuntosFijos().size() > v.getPuntosFijos().size())
                            v1.getPuntosFijos().remove(r);
                        else
                            v.getPuntosFijos().remove(r);
                    }
                }
            }
        }
        int[] array = new int[cantP+2];
        for(VehiculoExterno v:vehiculoExternos){
            for(String pf : v.getPuntosFijos()){
                array[Integer.parseInt(pf)]++;
            }
            for(int i = 1; i <= cantP; i++){
                if(array[i] > 1)
                    v.getPuntosFijos().remove(String.valueOf(i));

            }
        }

        for(VehiculoExterno v :vehiculoExternos){
            DataPuntoVehiculo data = new DataPuntoVehiculo();
            data.setVehiculoExterno(v);
            data.setPuntoExternos(obtenerPuntos(v));
            while (!cumpleVentanas(data).isEmpty() || !cumpleCapacidad(data) || !cumploLlegada(data)){
                v.getPuntosFijos().remove(0);
                data.setVehiculoExterno(v);
                data.setPuntoExternos(obtenerPuntos(v));
            }
        }


    }

    private boolean cumploLlegada(DataPuntoVehiculo d) {
        double tiempoViaje = calcularTiempo(d) + d.getVehiculoExterno().getHoraSalida();
        return tiempoViaje <= d.getVehiculoExterno().getHoraLlegada();
    }

    private boolean cumpleCapacidad(DataPuntoVehiculo data) {
        int cant = 0;
        for(PuntoExterno p: data.getPuntoExternos()){
            cant+= p.getDimension();
        }
        return cant <= data.getVehiculoExterno().getCapacidad();
    }

    private List<PuntoExterno> obtenerPuntos(VehiculoExterno v) {
        List<PuntoExterno> puntos = new ArrayList<>();
        for(PuntoExterno p :puntoExternos){
            if (v.getPuntosFijos().contains(p.getId()))
                puntos.add(p);
        }
        return puntos;
    }

    private List<PuntoExterno> cumpleVentanas(DataPuntoVehiculo dataPV) {
        List<PuntoExterno> result = new ArrayList<>();
        if (dataPV.getVehiculoExterno() == null || dataPV.getPuntoExternos().isEmpty()) return result;
        int tiempo = dataPV.getVehiculoExterno().getHoraSalida(); // minutos desde las 0:00
        tiempo += calcularTimeDistance(dataPV.getVehiculoExterno().getOrigen(), dataPV.getPuntoExternos().get(0).getLugar()).getTime();// Origen al P1
        for (int i = 0; i < dataPV.getPuntoExternos().size() - 1; i++) {
            if (tiempo < dataPV.getPuntoExternos().get(i).getHorarioMin()) {
                tiempo = dataPV.getPuntoExternos().get(i).getHorarioMin(); // vehiculo espera
            }
            tiempo += dataPV.getPuntoExternos().get(i).getTiempoEspera();
            if (tiempo > dataPV.getPuntoExternos().get(i).getHorarioMax()) { // Marchesi
                result.add(dataPV.getPuntoExternos().get(i));
            }
            tiempo +=  calcularTimeDistance(dataPV.getPuntoExternos().get(i).getLugar(), dataPV.getPuntoExternos().get(i+1).getLugar()).getTime();; // De P1 a Pn
        }
        if (tiempo < dataPV.getPuntoExternos().get(dataPV.getPuntoExternos().size() - 1).getHorarioMin()) {
            tiempo = dataPV.getPuntoExternos().get(dataPV.getPuntoExternos().size() - 1).getHorarioMin(); // vehiculo espera
        }
        tiempo += dataPV.getPuntoExternos().get(dataPV.getPuntoExternos().size() - 1).getTiempoEspera();
        if (tiempo > dataPV.getPuntoExternos().get(dataPV.getPuntoExternos().size() - 1).getHorarioMax()) { // Marchesi
            result.add(dataPV.getPuntoExternos().get(dataPV.getPuntoExternos().size() - 1));
        }
        return result;
    }

    private List<PuntoExterno> generarListaPuntos(int cantP, int cantV) {
        List<PuntoExterno> puntoExternos = new ArrayList<>();
        PuntoExterno p;
        for (int i = 1; i <= cantP; i++) {
            p = new PuntoExterno();
            p.setId("" + i);
            p.setLugar(generarUbicacion());
            p.setTiempoEspera(generarTiempo());
            p.setVehiculos(generarVehiculos(cantV));
            p.setDimension(generarDimension());
            p.setHorarioMin(generarHora());
            p.setHorarioMax(generarHora());
            while (p.getHorarioMin() > p.getHorarioMax() || Math.abs(p.getHorarioMax() - p.getHorarioMin()) < 30 || p.getTiempoEspera() > Math.abs(p.getHorarioMax() - p.getHorarioMin())) {
                p.setHorarioMin(generarHora());
                p.setHorarioMax(generarHora());
            }
            p.setPrioridad(generarPrioridad());
            puntoExternos.add(p);
        }
        return puntoExternos;
    }


    private int generarCantVehiculos() {
        int valorDado = (int) Math.floor(Math.random() * 200 + 1);
        MersenneTwisterFast rnd = new MersenneTwisterFast(valorDado);
        return R.randomValueFromClosedInterval(1, maxV, rnd);
    }

    private int generarCantPuntos() {
        int valorDado = (int) Math.floor(Math.random() * 200 + 1);
        MersenneTwisterFast rnd = new MersenneTwisterFast(valorDado);
        return R.randomValueFromClosedInterval(1, maxP, rnd);
    }

    private int generarPrioridad() {
        int valorDado = (int) Math.floor(Math.random() * 200 + 1);
        MersenneTwisterFast rnd = new MersenneTwisterFast(valorDado);
        return R.randomValueFromClosedInterval(1, minPrio, rnd);
    }

    private int generarHora() {
        int valorDado = (int) Math.floor(Math.random() * 200 + 1);
        MersenneTwisterFast rnd = new MersenneTwisterFast(valorDado);
        return R.randomValueFromClosedInterval(HMIN, HMAX, rnd);
    }

    private double generarCapacidad() {
        int valorDado = (int) Math.floor(Math.random() * 200 + 1);
        MersenneTwisterFast rnd = new MersenneTwisterFast(valorDado);
        return R.randomValueFromClosedInterval(DMINV, DMAXV, rnd);
    }

    private double generarDimension() {
        int valorDado = (int) Math.floor(Math.random() * 200 + 1);
        MersenneTwisterFast rnd = new MersenneTwisterFast(valorDado);
        return R.randomValueFromClosedInterval(DMINP, DMAXP, rnd);
    }

    private List<String> generarPuntos(VehiculoExterno v, int cantP, List<VehiculoExterno> vehiculoExternos) {
        List<String> puntos = new ArrayList<>();
        int valorDado = (int) Math.floor(Math.random() * 200 + 1);
        MersenneTwisterFast rnd = new MersenneTwisterFast(valorDado);
        int tope = R.randomValueFromClosedInterval(0, cantP / 3, rnd);
        if (tope > 0 && tope < 20)
            for (int i = 0; i < tope; i++) {
                int random = R.randomValueFromClosedInterval(0, cantP - 1, rnd);
                boolean noValido = false;
                for (VehiculoExterno ve : vehiculoExternos) {
                    if (ve.getPuntosFijos() != null && !ve.getPuntosFijos().isEmpty() && ve.getPuntosFijos().contains("" + random)) {
                        noValido = true;
                        break;
                    }
                }
                if (!noValido && (puntoExternos.get(random).getVehiculos().contains(v.getId()) || puntoExternos.get(random).getVehiculos().isEmpty()) && cumpleHoraLLegadaVehiculo(puntoExternos.get(random), v)) {
                    int x = random + 1;
                    puntos.add("" + x);
                }else {
                    System.out.println("Reintentando recorrido...");
                }
            }
        return puntos;
    }
    private String minutosHoras(double minutos) {
        int hora = (int) (minutos / 60);
        int min = (int) (minutos % 60);
        String horas = "" + hora;
        if (hora < 10) {
            horas = "0" + hora;
        }
        String mins = "" + min;
        if (min < 10) {
            mins = "0" + min;
        }
        return "" + horas + ":" + mins;
    }
    private List<String> generarVehiculos(int cantV) {
        List<String> vehiculos = new ArrayList<>();
        int valorDado = (int) Math.floor(Math.random() * 200 + 1);
        MersenneTwisterFast rnd = new MersenneTwisterFast(valorDado);
        int tope = R.randomValueFromClosedInterval(0, cantV, rnd);
        if (tope > 0)
            for (int i = 0; i < tope; i++) {
                int random = R.randomValueFromClosedInterval(0, cantV - 1, rnd);
                int x = random+1;
                if (!vehiculos.contains("" + x))
                    vehiculos.add("" + x);
            }
        return vehiculos;
    }

    private double generarTiempo() {
        int valorDado = (int) Math.floor(Math.random() * 200 + 1);
        MersenneTwisterFast rnd = new MersenneTwisterFast(valorDado);
        return R.randomValueFromClosedInterval(tiempoMin, tiempoMax, rnd);
    }

    private Coord generarUbicacion() {
        int valorDado = (int) Math.floor(Math.random() * 200 + 1);
        MersenneTwisterFast rnd = new MersenneTwisterFast(valorDado);
        int lat = R.randomValueFromClosedInterval(min, max, rnd);
        rnd = new MersenneTwisterFast((int) Math.floor(Math.random() * 200 + 1));
        int longitud = R.randomValueFromClosedInterval(min_2, max_2, rnd);
        String lo = longuitud_str, la = latitud_str;
        if (longitud <= 99999) {
            lo += "0";
        }
        lo += "" + longitud;
        la += "" + lat;
        return new Coord(Double.parseDouble(lo), Double.parseDouble(la));
    }

    private TimeDistance obtenerTimeDistance(VehiculoExterno v, PuntoExterno punto) {
        double time;
        double distance;
        TimeDistance t;
        if (v.getPuntosFijos() == null || v.getPuntosFijos().isEmpty()) {
            t = calcularTimeDistance(v.getOrigen(), punto.getLugar());
            time = t.getTime() + punto.getTiempoEspera() + calcularTimeDistance(punto.getLugar(), v.getOrigen()).getTime();
            distance = t.getDistance();
        } else {
            t = calcularTimeDistance(v.getOrigen(), puntoExternos.get(Integer.parseInt(v.getPuntosFijos().get(0))).getLugar());
            time = t.getTime() + punto.getTiempoEspera();
            distance = t.getDistance();
            for (int i = 0; i < v.getPuntosFijos().size() - 1; i++) {
                t = calcularTimeDistance(puntoExternos.get(Integer.parseInt(v.getPuntosFijos().get(i))).getLugar(), puntoExternos.get(Integer.parseInt(v.getPuntosFijos().get(i + 1))).getLugar());
                time += t.getTime() + puntoExternos.get(Integer.parseInt(v.getPuntosFijos().get(i))).getTiempoEspera();
                distance += t.getDistance();
            }
            t = calcularTimeDistance(puntoExternos.get(Integer.parseInt(v.getPuntosFijos().get(v.getPuntosFijos().size() - 1))).getLugar(), punto.getLugar());
            time = t.getTime() + puntoExternos.get(Integer.parseInt(v.getPuntosFijos().get(v.getPuntosFijos().size() - 1))).getTiempoEspera() + punto.getTiempoEspera();
            distance = t.getDistance();
        }
        return new TimeDistance(time, distance);
    }


    private boolean cumpleHoraLLegadaVehiculo(PuntoExterno punto, VehiculoExterno v) {
         /*
        Chequeo que si paso por ese punto llegue a tiempo al destino del vehiculo contemplando el tiempo de espera. (2)
         */
        double hora_final = obtenerTimeDistance(v, punto).getTime();
        double hora_partida = v.getHoraSalida();
        return hora_final + hora_partida <= v.getHoraLlegada();
    }

    public double calcularTiempo(DataPuntoVehiculo d) {
        double result;
        if (d.getPuntoExternos().isEmpty() || d.getVehiculoExterno() == null) {
            return 0;
        }
        double horaActual = d.getVehiculoExterno().getHoraSalida();
        if (d.getPuntoExternos().size() == 1) {
            result = calcularTimeDistance(d.getVehiculoExterno().getOrigen(), d.getPuntoExternos().get(0).getLugar()).getTime();
            horaActual += result;
            if (horaActual < d.getPuntoExternos().get(0).getHorarioMin()) {
                result += d.getPuntoExternos().get(0).getHorarioMin() - horaActual; // si el VehiculoExterno llega antes de que abra, espera.
//                horaActual = d.getPuntoExternos().get(0).getHorarioMin();
            }
            result += d.getPuntoExternos().get(0).getTiempoEspera();
            result += calcularTimeDistance(d.getPuntoExternos().get(0).getLugar(), d.getVehiculoExterno().getDestino()).getTime();
            return result;
        }
        result = calcularTimeDistance(d.getVehiculoExterno().getOrigen(), d.getPuntoExternos().get(0).getLugar()).getTime();// V al primer P
        horaActual += result;
        for (int i = 0; i < d.getPuntoExternos().size() - 1; i++) {
            if (horaActual < d.getPuntoExternos().get(i).getHorarioMin()) {
                result += d.getPuntoExternos().get(i).getHorarioMin() - horaActual; // si el VehiculoExterno llega antes de que abra, espera.
                horaActual = d.getPuntoExternos().get(i).getHorarioMin();
            }
            result += d.getPuntoExternos().get(i).getTiempoEspera(); // tiempo en P
            result += calcularTimeDistance(d.getPuntoExternos().get(i).getLugar(), d.getPuntoExternos().get(i+1).getLugar()).getTime(); // De P1 a Pn-1
            horaActual += d.getPuntoExternos().get(i).getTiempoEspera();
            horaActual += calcularTimeDistance(d.getPuntoExternos().get(i).getLugar(), d.getPuntoExternos().get(i+1).getLugar()).getTime();// De P1 a Pn-1
        }
        if (horaActual < d.getPuntoExternos().get(d.getPuntoExternos().size() - 1).getHorarioMin()) {
            result += d.getPuntoExternos().get(d.getPuntoExternos().size() - 1).getHorarioMin() - horaActual; // si el VehiculoExterno llega antes de que abra, espera.
//            horaActual = d.getPuntoExternos().get(d.getPuntoExternos().size() - 1).getHorarioMin();
        }
        result += d.getPuntoExternos().get(d.getPuntoExternos().size() - 1).getTiempoEspera(); // tiempo en Pn
        result += calcularTimeDistance(d.getVehiculoExterno().getDestino(), d.getPuntoExternos().get(d.getPuntoExternos().size() - 1).getLugar()).getTime(); // De Pn a destino de V// De Pn a destino de V
        return result;
    }

    private TimeDistance calcularTimeDistance(Coord origen, Coord destino) {
        HttpResponse<String> response;
        TimeDistance t = new TimeDistance();
        final String URL = "http://des25:5000/route/v1/driving/";
        final String PARAMETERS = "?overview=false";
        int i = 0;
        while (i == 0) {
            if (secuencia % 1000 == 0) {
                System.out.println(secuencia);
            }
            try {
                response = Unirest.get(URL + origen.getLatitud() + "," + origen.getLongitud() + ";" + destino.getLatitud() + "," + destino.getLongitud() + PARAMETERS)
                        .header("cache-control", "no-cache")
                        .asString();
                JSONObject json = new JSONObject(response.getBody());
                JSONArray results = json.getJSONArray("routes");
                JSONObject array = results.getJSONObject(0);
                double duration = array.getDouble("duration");
                double distance = array.getDouble("distance");
                t.setTime(duration / 60);
                t.setDistance(distance / 1000);
                i = 1;
                secuencia++;
            } catch (Exception ex) {
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
}
