import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.swarm.arquillian.DefaultDeployment;

import java.io.IOException;

import static org.fest.assertions.Assertions.assertThat;

/**
 *
 * @author Michal Szynkiewicz, michal.l.szynkiewicz@gmail.com
 * <br>
 * Date: 8/28/18
 */
@RunWith(Arquillian.class)
@DefaultDeployment
public class NoAcceptHeadersTest {

    @Test
    @RunAsClient
    public void shouldGetMetricsWithoutAcceptHeadersSet() throws IOException {
        Response response = Request.Get("http://localhost:8080/metrics").execute();
        assertThat(response.returnResponse().getStatusLine().getStatusCode()).isEqualTo(200);
    }
}