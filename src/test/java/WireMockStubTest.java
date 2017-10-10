import com.github.tomakehurst.wiremock.client.WireMock;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import static com.github.tomakehurst.wiremock.client.WireMock.verify;

/**
 * Created by palemreddy on 07/10/2017.
 */
public class WireMockStubTest {

    //@Rule
    //public WireMockRule wireMockRule = new WireMockRule(9999);

    @Test
    public void exampleTest()throws Exception {
        WireMock.configureFor("localhost", 9999);
        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/wiremock")).
                willReturn(WireMock.aResponse().withBody("Welcome to wiremock!!")));


        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet request = new HttpGet("http://localhost:9999/wiremock");
        CloseableHttpResponse response = httpClient.execute(request);
        String responseToString = convertResponseToString(response);

        verify(WireMock.getRequestedFor(WireMock.urlEqualTo("/wiremock")));
        Assertions.assertThat(responseToString).isEqualTo("Welcome to wiremock!!");

    }

    private static String convertResponseToString(HttpResponse response) throws IOException {
        InputStream responseStream = response.getEntity().getContent();
        Scanner scanner = new Scanner(responseStream, "UTF-8");
        String responseString = scanner.useDelimiter("\\Z").next();
        scanner.close();
        return responseString;
    }

}
