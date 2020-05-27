package service;

import beans.GeneradorEJBBean;
import com.mashape.unirest.http.exceptions.UnirestException;
import datatype.Resultado;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Singleton
@LocalBean
@Path("")
public class ApiServiceProducer {
    @EJB
    GeneradorEJBBean generadorEJBBean;

    @GET
    @Path("generador")
    public String tutaGenerator(@QueryParam("puntos") int p, @QueryParam("vehiculos") int v){
        try {
            generadorEJBBean.generarInstancia(p, v);
            return "OK";
        } catch (UnirestException e) {
            e.printStackTrace();
            return "ERROR, "+e.getMessage();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/recibirSolucion")
    public String recibirSolucion(Resultado resultado) {
        System.out.println("Se recibe la solucion: " + resultado);
        return "Ok.";
    }
}
