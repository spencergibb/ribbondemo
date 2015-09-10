package demo;

import com.netflix.loadbalancer.AbstractServerListFilter;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Spencer Gibb
 */
@Slf4j
public class DemoServerListFilter extends AbstractServerListFilter<Server> {
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
