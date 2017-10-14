import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.stubbing.Scenario;
import helper.ResponseHelper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

/**
 * Created by palemreddy on 12/10/2017.
 */
public class WireMockScenarioTest {

    @Test
    public void toDoListScenario()throws Exception {
        WireMock.configureFor("localhost", 9999);
        stubFor(get(urlEqualTo("/todo/items")).inScenario("To do list")
                .whenScenarioStateIs(Scenario.STARTED)
                .willReturn(aResponse()
                        .withBody("<items>" +
                                "   <item>Buy milk</item>" +
                                "</items>")));

        stubFor(post(urlEqualTo("/todo/items")).inScenario("To do list")
                .whenScenarioStateIs(Scenario.STARTED)
                .withRequestBody(containing("Cancel newspaper subscription"))
                .willReturn(aResponse().withStatus(201))
                .willSetStateTo("Cancel newspaper item added"));

        stubFor(get(urlEqualTo("/todo/items")).inScenario("To do list")
                .whenScenarioStateIs("Cancel newspaper item added")
                .willReturn(aResponse()
                        .withBody("<items>" +
                                "   <item>Buy milk</item>" +
                                "   <item>Cancel newspaper subscription</item>" +
                                "</items>")));



        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet("http://localhost:9999/todo/items");
        CloseableHttpResponse response = httpClient.execute(httpGet);
        String responseToString = ResponseHelper.convertResponseToString(response);

        verify(WireMock.getRequestedFor(WireMock.urlEqualTo("/todo/items")));
        Assertions.assertThat(responseToString).contains("Buy milk");

        HttpPost httpPost = new HttpPost("http://localhost:9999/todo/items");
        httpPost.setHeader("Content-type", "text/plain");
        try {
            StringEntity stringEntity = new StringEntity("\"Cancel newspaper subscription\"");
            httpPost.getRequestLine();
            httpPost.setEntity(stringEntity);

            CloseableHttpResponse response1 = httpClient.execute(httpPost);
            Assertions.assertThat(response1.getStatusLine().getStatusCode()).isEqualTo(201);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }



        HttpGet httpGet1 = new HttpGet("http://localhost:9999/todo/items");
        CloseableHttpResponse response1 = httpClient.execute(httpGet1);
        String responseToString1 = ResponseHelper.convertResponseToString(response1);

        verify(WireMock.getRequestedFor(WireMock.urlEqualTo("/todo/items")));
        Assertions.assertThat(responseToString1).contains("Buy milk");
        Assertions.assertThat(responseToString1).contains("Cancel newspaper subscription");

    }


}
