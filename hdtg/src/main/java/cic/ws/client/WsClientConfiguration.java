package cic.ws.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;


@Configuration
public class WsClientConfiguration {
	@Bean
	  public Jaxb2Marshaller marshaller() {
	    Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
	    // this package must match the package in the <generatePackage> specified in
	    // pom.xml
	    marshaller.setContextPath("cic.ws.model");
	    return marshaller;
	  }

	  @Bean
	  public WsClient wsClient(Jaxb2Marshaller marshaller) {
	    WsClient client = new WsClient();
	    client.setDefaultUri("http://localhost:8081/CoreServiceApp/service/report");
	    client.setMarshaller(marshaller);
	    client.setUnmarshaller(marshaller);
	    return client;
	  }
}
