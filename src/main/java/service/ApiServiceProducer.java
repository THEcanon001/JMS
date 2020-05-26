package service;

import beans.GeneradorEJBBean;
import com.mashape.unirest.http.exceptions.UnirestException;
import datatype.Contenedor;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

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
    @Path("tutaParametrico")
    public String tutaParametrico(Contenedor contenedor){
        generadorEJBBean.enviarParametros(contenedor);
        return "OK";
    }
}
