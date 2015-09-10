package demo;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.loadbalancer.AbstractServerListFilter;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerListFilter;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
@RibbonClient(value = "ribbondemo", configuration = RibbonDemoApplication.RibbonDemoRibbonConfig.class)
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

	@Configuration
	public static class RibbonDemoRibbonConfig {
		@Bean
		public ServerListFilter<Server> ribbonServerServerListFilter() {
			return new DemoServerListFilter();
		}
	}

	@Slf4j
	public static class DemoServerListFilter extends AbstractServerListFilter<Server> {
		@Autowired
		DemoProperties props;

		@Override
		public List<Server> getFilteredListOfServers(List<Server> servers) {
			if (StringUtils.hasText(props.getFilterPattern())) {
				String pattern = props.getFilterPattern();
				return filter(servers, pattern);
			}
			return servers;
		}

		private List<Server> filter(List<Server> servers, String pattern) {
			List<Server> filtered = new ArrayList<>();
			for (Server server : servers) {
				if (PatternMatchUtils.simpleMatch(pattern, server.getId())) {
					filtered.add(server);
				}
			}
			return filtered;
		}
	}

}
