package service;

import beans.GeneradorEJBBean;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ws.rs.GET;
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
        generadorEJBBean.generarInstancia(p, v);
        return "ALAAAAAA";
    }
}
