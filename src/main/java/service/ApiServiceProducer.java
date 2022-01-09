package service;

import beans.GeneradorEJBBean;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.sun.deploy.util.StringUtils;
import datatype.Coord;
import datatype.DataPuntoVehiculo;
import datatype.PuntoExterno;
import datatype.Resultado;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Singleton
@LocalBean
@Path("")
public class ApiServiceProducer {
    @EJB
    GeneradorEJBBean generadorEJBBean;

    @GET
    @Path("generador")
    public String tutaGenerator(@QueryParam("puntos") int p, @QueryParam("vehiculos") int v) {
        try {
            generadorEJBBean.generarInstancia(p, v);
            return "OK";
        } catch (UnirestException e) {
            e.printStackTrace();
            return "ERROR, " + e.getMessage();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/recibirSolucion")
    public String recibirSolucion(Resultado resultado) {
        System.out.println("Se recibe la solucion: " + resultado);
        for (DataPuntoVehiculo dt : resultado.getSolucion()) {
            if (dt != null) {
                if (dt.getVehiculoExterno() != null && dt.getVehiculoExterno().getId() != null) {
                    System.out.print("Vehiculo " + dt.getVehiculoExterno().getId() + " salida:" + dt.getVehiculoExterno().getHoraSalida() + " llegada estimada:" + dt.getVehiculoExterno().getHoraLlegadaEstimada() + " - ");
                }
                for (PuntoExterno p : dt.getPuntoExternos()) {
                    System.out.print(p.getId() + "->" + p.getHoraEstimada() + ";");
                }
                System.out.println();
            }
        }
        return "Ok.";
    }

    @GET
    @Path("testCalendar")
    public String testCalendar() {
        int minutos = 1180;
        String formato = "%02d:%02d";
        long horasReales = TimeUnit.MINUTES.toHours(minutos);
        long minutosReales = TimeUnit.MINUTES.toMinutes(minutos) - TimeUnit.HOURS.toMinutes(TimeUnit.MINUTES.toHours(minutos));
        return String.format(formato, horasReales, minutosReales);
    }

    @GET
    @Path("testRepetidos")
    public String testRepetidos() {
        String codigo = "CRQ123456789";
        codigo = codigo.substring(0, Math.min(codigo.length(), 10));
        return codigo;
    }

    @GET
    @Path("generarInterior")
    public String generarInterior() {
        List<PuntoExterno> puntoExternos = new ArrayList<>();

        for (int i = 0; i < 60; i++) {
            PuntoExterno pe = new PuntoExterno();
            Coord coord = new Coord();

        }
        return null;
    }

    private static int factorial(int n) {
        return (n == 0) ? 1 : n * factorial(n - 1);
    }

    @GET
    @Path("testJoda")
    public String testJoda() throws ParseException {
        System.out.println(org.joda.time.LocalDateTime.parse("2021-02-03T23:30:00").toDate());
        // Wed Feb 03 23:30:00 UYT 2021

        System.out.println(org.joda.time.LocalDateTime.fromDateFields(new Date()));
        // 2021-03-11T09:31:47.451

        System.out.println(org.joda.time.LocalDate.fromDateFields(new Date()));
        // 2021-03-11

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        List<Date> fechas = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            calendar.set(Calendar.DAY_OF_MONTH, i);
            fechas.add(calendar.getTime());
        }
        List<String> feriadosStr = new ArrayList<>();
        for (Date d :  fechas) {
            feriadosStr.add(org.joda.time.LocalDate.fromDateFields(d).toString());
        }
        System.out.println("Final str: " + feriadosStr);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        List<Date> dates = new ArrayList<>();
        for (String s: feriadosStr) {
            Date date = format.parse(s);
            dates.add(date);
        }
        System.out.println("Finales: " + dates);
        return "ok";
    }

}
