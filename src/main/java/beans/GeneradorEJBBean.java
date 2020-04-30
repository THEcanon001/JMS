package beans;

import datatype.Coord;
import datatype.Punto;
import datatype.Vehiculo;
import utils.MersenneTwisterFast;
import utils.R;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;


@Stateless
@LocalBean
public class GeneradorEJBBean {

    private static String latitud_str = "-34.";
    private static String longuitud_str = "-56.";
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


    public void generarInstancia(int p, int v) {
        if(p == 0 || v == 0){
           p = generarCantPuntos();
           v = generarCantVehiculos();
        }
        List<Punto> puntos = generarListaPuntos(p);
        List<Vehiculo> vehiculos = generarListaVehiculos(v);
        enviar(puntos, vehiculos);
    }

    private void enviar(List<Punto> puntos, List<Vehiculo> vehiculos) {
        System.out.println("PUNTOS ENVIADOS CON EXITO");
    }

    private List<Vehiculo> generarListaVehiculos(int cantV) {
        List<Vehiculo> vehiculos = new ArrayList<>();
        Vehiculo v;
        for(int i = 0; i < cantV; i++){
            v = new Vehiculo();
            v.setId("" + i);
            v.setOrigen(generarUbicacion());
            v.setDestino(generarUbicacion());
            v.setHoraSalida(generarHora());
            v.setHoraLlegada(generarHora());
            while(v.getHoraSalida() > v.getHoraLlegada() || Math.abs(v.getHoraLlegada() - v.getHoraSalida()) < 120){
                v.setHoraSalida(generarHora());
                v.setHoraLlegada(generarHora());
            }
            v.setCapacidad(generarCapacidad());
            v.setPuntosFijos(generarPuntos());
            vehiculos.add(v);
        }
        return vehiculos;
    }

    private List<Punto> generarListaPuntos(int cantP) {
        List<Punto> puntos = new ArrayList<>();
        Punto p;
        for(int i = 0; i < cantP; i++) {
            p = new Punto();
            p.setId(""+i);
            p.setLugar(generarUbicacion());
            p.setTiempoEspera(generarTiempo());
            p.setVehiculos(generarVehiculos());
            p.setDimension(generarDimension());
            p.setHorarioMin(generarHora());
            p.setHorarioMax(generarHora());
            while(p.getHorarioMin() > p.getHorarioMax() || Math.abs(p.getHorarioMax() - p.getHorarioMin()) < 30){
                p.setHorarioMin(generarHora());
                p.setHorarioMax(generarHora());
            }
            p.setPrioridad(generarPrioridad());
            puntos.add(p);
        }
       return puntos;
    }


    private int generarCantVehiculos() {
        int valorDado = (int) Math.floor(Math.random() * 200 + 1);
        MersenneTwisterFast rnd = new MersenneTwisterFast(valorDado);
        return R.randomValueFromClosedInterval(1, maxV , rnd);
    }

    private int generarCantPuntos() {
        int valorDado = (int) Math.floor(Math.random() * 200 + 1);
        MersenneTwisterFast rnd = new MersenneTwisterFast(valorDado);
        return R.randomValueFromClosedInterval(1, maxP , rnd);
    }

    private int generarPrioridad() {
        int valorDado = (int) Math.floor(Math.random() * 200 + 1);
        MersenneTwisterFast rnd = new MersenneTwisterFast(valorDado);
        return R.randomValueFromClosedInterval(1, minPrio , rnd);
    }

    private int generarHora() {
        int valorDado = (int) Math.floor(Math.random() * 200 + 1);
        MersenneTwisterFast rnd = new MersenneTwisterFast(valorDado);
        return R.randomValueFromClosedInterval(HMIN, HMAX , rnd);
    }

    private double generarCapacidad() {
        int valorDado = (int) Math.floor(Math.random() * 200 + 1);
        MersenneTwisterFast rnd = new MersenneTwisterFast(valorDado);
        return R.randomValueFromClosedInterval(DMINV, DMAXV , rnd);
    }

    private double generarDimension() {
        int valorDado = (int) Math.floor(Math.random() * 200 + 1);
        MersenneTwisterFast rnd = new MersenneTwisterFast(valorDado);
        return R.randomValueFromClosedInterval(DMINP, DMAXP , rnd);
    }

    private List<String> generarPuntos() {
        List<String> puntos = new ArrayList<>();
        int valorDado = (int) Math.floor(Math.random() * 200 + 1);
        MersenneTwisterFast rnd = new MersenneTwisterFast(valorDado);
        int random =  R.randomValueFromClosedInterval(0, maxP/10 , rnd);
        if (random > 0 && random < 20)
            for(int i = 0 ; i < random; i++){
                random = R.randomValueFromClosedInterval(1, maxP , rnd);
                puntos.add(""+i);
            }
        return puntos;
    }

    private List<String> generarVehiculos() {
        List<String> vehiculos = new ArrayList<>();
        int valorDado = (int) Math.floor(Math.random() * 200 + 1);
        MersenneTwisterFast rnd = new MersenneTwisterFast(valorDado);
        int random =  R.randomValueFromClosedInterval(0, maxV , rnd);
        if (random > 0)
            for(int i = 0 ; i < random; i++){
                random = R.randomValueFromClosedInterval(1, maxV , rnd);
                vehiculos.add(""+i);
            }
        return vehiculos;
    }

    private double generarTiempo() {
        int valorDado = (int) Math.floor(Math.random() * 200 + 1);
        MersenneTwisterFast rnd = new MersenneTwisterFast(valorDado);
        return R.randomValueFromClosedInterval(tiempoMin, tiempoMax , rnd);
    }

    private Coord generarUbicacion() {
        int valorDado = (int) Math.floor(Math.random() * 200 + 1);
        MersenneTwisterFast rnd = new MersenneTwisterFast(valorDado);
        int lat = R.randomValueFromClosedInterval(min, max , rnd);

        rnd = new MersenneTwisterFast((int) Math.floor(Math.random() * 200 + 1));
        int longitud = R.randomValueFromClosedInterval(min_2, max_2, rnd);
        if (longitud <= 99999) {
            longuitud_str += "0";
        }
        longuitud_str += "" + longitud;
        latitud_str += "" + lat;
        return new Coord(Double.parseDouble(longuitud_str), Double.parseDouble(latitud_str));
    }
}
