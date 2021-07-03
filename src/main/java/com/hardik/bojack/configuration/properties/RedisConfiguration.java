package com.hardik.bojack.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "spring.redis")
public class RedisConfiguration {

	private final String host;
	private final Integer port;
	private final Jedis jedis;

	@Data
	public class Jedis {
		private final Pool pool;

		@Data
		public class Pool {
			private final Integer maxActive;
			private final Integer maxIdle;
			private final Integer minIdle;
		}
	}
}