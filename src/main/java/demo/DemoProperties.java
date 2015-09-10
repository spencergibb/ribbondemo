package demo;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Spencer Gibb
 */
@ConfigurationProperties("demo")
@Data
public class DemoProperties {
	String filterPattern;
}
