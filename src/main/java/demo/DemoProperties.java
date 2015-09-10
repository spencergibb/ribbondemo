package demo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.PatternMatchUtils;

/**
 * @author Spencer Gibb
 */
@ConfigurationProperties("demo")
@Data
public class DemoProperties {
	String filterPattern;

	public static void main(String[] args) {
	    String[] ids = new String[] {
				"localhost:9091",
				"localhost2:9092",
				"localhost3:9093"
		};

		String pattern = "*2:*";
		for (String id : ids) {
			boolean matches = PatternMatchUtils.simpleMatch(pattern, id);
			System.out.println(id + " matches " + pattern + ": " + matches);
		}
	}
}
