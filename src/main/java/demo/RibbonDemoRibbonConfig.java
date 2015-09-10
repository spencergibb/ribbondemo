package demo;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.LoadBalancerBuilder;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;
import com.netflix.loadbalancer.ServerListFilter;
import com.netflix.loadbalancer.ZoneAwareLoadBalancer;

/**
 * @author Spencer Gibb
 */
//NOTE: no @Configuration
class RibbonDemoRibbonConfig {

	@Bean
	public ServerListFilter<Server> ribbonServerServerListFilter() {
		return new DemoServerListFilter();
	}

	@Bean
	@ConditionalOnMissingBean
	public ILoadBalancer ribbonLoadBalancer(IClientConfig config,
											ServerList<Server> serverList, ServerListFilter<Server> serverListFilter,
											IRule rule, IPing ping) {
		ZoneAwareLoadBalancer<Server> balancer = LoadBalancerBuilder.newBuilder()
				.withClientConfig(config).withRule(rule).withPing(ping)
				.withServerListFilter(serverListFilter).withDynamicServerList(serverList)
				.buildDynamicServerListLoadBalancer();
		return new DemoLoadBalancer(balancer);
	}
}
