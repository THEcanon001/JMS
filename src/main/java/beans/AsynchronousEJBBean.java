package beans;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;

@Stateless
public class AsynchronousEJBBean {

    @Asynchronous
    public void pruebaSemaforo() throws UnirestException {
        String url = "http://des25:8200/evolution-web/numerador";
        HttpResponse<String> response = Unirest.get(url)
                .header("cache-control", "no-cache")
                .asString();
        String var = response.getBody();
        System.out.println(var);
    }

    @Asynchronous
    public void test_serial() throws UnirestException {
        String url = "http://des23:8200/evolution-web/test_serial";
        HttpResponse<String> response = Unirest.get(url)
                .header("cache-control", "no-cache")
                .asString();
        String var = response.getBody();
        System.out.println(var);
    }
}
