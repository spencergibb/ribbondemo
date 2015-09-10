package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(exclude = RibbonDemoRibbonConfig.class)
@EnableDiscoveryClient
@RestController
@RibbonClient(value = "ribbondemo", configuration = RibbonDemoRibbonConfig.class)
public class RibbonDemoApplication {

	@Autowired
	private RestTemplate rest;

	@Autowired
	private DiscoveryClient discovery;

	@RequestMapping("/")
	public String home() {
		String who = rest.getForObject("http://ribbondemo/me", String.class);
		return "Hi from: " + who;
	}

	@RequestMapping("/me")
	public String me() {
		return discovery.getLocalServiceInstance().getUri().toString();
	}

	@Bean
	public DemoProperties demoProperties() {
		return new DemoProperties();
	}

    public static void main(String[] args) {
        SpringApplication.run(RibbonDemoApplication.class, args);
    }

}

