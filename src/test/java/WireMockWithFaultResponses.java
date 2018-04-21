import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.http.Fault;
import helper.ResponseHelper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.verify;
/**
 * Created by palemreddy on 14/10/2017.
 */
public class WireMockWithFaultResponses {
    @Test
    public void ShouldProduceSimpleFault()throws Exception {
        WireMock.configureFor("localhost", 9999);

        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/fault"))
                .willReturn(WireMock.aResponse()
                        .withStatus(405)
                        .withFault(Fault.MALFORMED_RESPONSE_CHUNK)));


        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet request = new HttpGet("http://localhost:9999/fault");
        CloseableHttpResponse response = httpClient.execute(request);
        System.out.println("response->"+response.toString());
        String responseToString = ResponseHelper.convertResponseToString(response);

        verify(WireMock.getRequestedFor(WireMock.urlEqualTo("/fault")));
        Assertions.assertThat(responseToString).isEqualTo("Welcome to wiremock!!");

    }

}
