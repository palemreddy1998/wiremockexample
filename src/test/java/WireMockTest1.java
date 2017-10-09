import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
/**
 * Created by palemreddy on 07/10/2017.
 */
public class WireMockTest1 {

    //@Rule
    //public WireMockRule wireMockRule = new WireMockRule(9999);

    @Test
    public void exampleTest() {
        StubMapping mapping = stubFor(post(urlEqualTo("/sayHello2"))
                .withHeader("Accept", equalTo("text/json"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/json")
                        .withBody("{\"message\": \"Hello world! Your JustAPIs instance is running correctly.\"}")));
        Assertions.assertThat(mapping.getResponse()).isNotNull();

    }
}
