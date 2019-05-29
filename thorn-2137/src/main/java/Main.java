import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.wildfly.swarm.arquillian.DefaultDeployment;

import java.io.IOException;

@DefaultDeployment
public class Main {

    @RunAsClient
    public static void bug() throws IOException {
        Response response = Request.Get("http://localhost:8080/metrics").execute();

        assert (response.returnResponse().getStatusLine().getStatusCode() == 200);
    }

    @RunAsClient
    public static void main(String... args) throws IOException {
        bug();
    }
}