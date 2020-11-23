package david.msabot.discordbot;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import david.msabot.discordbot.Entity.EurekaResponse;
import david.msabot.discordbot.Service.MSAService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
class DiscordbotApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void testXmlParse() throws Exception {
        String url = "http://140.121.197.130:9040/eureka/apps";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_XML_VALUE);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        String xml = restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
        MSAService service = new MSAService();

        XmlMapper mapper = new XmlMapper();
        EurekaResponse value = mapper.readValue(service.replaceReversed(xml), EurekaResponse.class);
        System.out.println(value);
    }

}
