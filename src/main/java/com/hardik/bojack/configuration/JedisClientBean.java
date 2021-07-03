package com.hardik.bojack.configuration;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;

import com.hardik.bojack.configuration.properties.RedisConfiguration;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@EnableConfigurationProperties(RedisConfiguration.class)
public class JedisClientBean {

	private final RedisConfiguration redisConfiguration;

	@Bean
	public JedisClientConfiguration getJedisClientConfiguration() {
		final var configuration = redisConfiguration.getJedis().getPool();
		JedisClientConfiguration.JedisPoolingClientConfigurationBuilder JedisPoolingClientConfigurationBuilder = (JedisClientConfiguration.JedisPoolingClientConfigurationBuilder) JedisClientConfiguration
				.builder();
		final var GenericObjectPoolConfig = new GenericObjectPoolConfig();
		GenericObjectPoolConfig.setMaxTotal(configuration.getMaxActive());
		GenericObjectPoolConfig.setMaxIdle(configuration.getMaxIdle());
		GenericObjectPoolConfig.setMinIdle(configuration.getMinIdle());
		return JedisPoolingClientConfigurationBuilder.poolConfig(GenericObjectPoolConfig).build();
	}

}