package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.session.web.http.CookieHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;

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

	@RequestMapping("/filter")
	public String filter(@RequestParam(value = "pattern", defaultValue = "") String pattern, HttpSession session) {
		if (StringUtils.hasText(pattern)) {
			session.setAttribute("X-Ribbon-Pattern", pattern);
		} else {
			session.removeAttribute("X-Ribbon-Pattern");
		}

		return pattern +"\n";
	}

	@Bean
	public HttpSessionStrategy httpSessionStrategy() {
		return new CookieHttpSessionStrategy();
	}

	@Bean
	public DemoProperties demoProperties() {
		return new DemoProperties();
	}

    public static void main(String[] args) {
        SpringApplication.run(RibbonDemoApplication.class, args);
    }

}

