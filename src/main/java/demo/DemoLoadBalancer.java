package demo;

import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Spencer Gibb
 */
public class DemoLoadBalancer implements ILoadBalancer {
	ILoadBalancer delegate;

	public DemoLoadBalancer(ILoadBalancer delegate) {
		this.delegate = delegate;
	}

	@Override
	public void addServers(List<Server> newServers) {
		this.delegate.addServers(newServers);
	}

	@Override
	public Server chooseServer(Object key) {
		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (sra != null) {
			HttpServletRequest req = sra.getRequest();
			String pattern = req.getParameter("X-Ribbon-Server");
			if (StringUtils.hasText(pattern)) {
				List<Server> servers = getServerList(true);
				for (Server server : servers) {
					if (PatternMatchUtils.simpleMatch(pattern, server.getId())) {
						return server;
					}
				}
			}
		}
		return this.delegate.chooseServer(key);
	}

	@Override
	public void markServerDown(Server server) {
		this.delegate.markServerDown(server);
	}

	@Override
	public List<Server> getServerList(boolean availableOnly) {
		return this.delegate.getServerList(availableOnly);
	}
}
