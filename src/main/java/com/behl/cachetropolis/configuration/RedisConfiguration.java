package com.behl.cachetropolis.configuration;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager.RedisCacheManagerBuilder;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisConfiguration {

	@Bean
	public RedisConnectionFactory redisConnectionFactory(final RedisProperties redisProperties) {
		final var standaloneConfiguration = new RedisStandaloneConfiguration(redisProperties.getHost(), redisProperties.getPort());
		standaloneConfiguration.setPassword(redisProperties.getPassword());
		return new LettuceConnectionFactory(standaloneConfiguration);
	}
	
	/**
	 * <p>Creates a primary <code>CacheManager</code> bean for caching lists using JSON
	 * serialization in the provisioned cache. This is the primary cache manager and
	 * does not require explicit definition when using <code>@Caching</code> annotations.</p>
	 * 
	 * <p>The below code will automatically use <code>listCacheManager</code> as the
	 * cacheManager:</p>
	 * 
	 * <pre>
	 * {@literal @}Cacheable(value = "persons", key = "#planetId")
	 * public List{@literal <}Person{@literal >} getPersons(UUID planetId) {
	 *     return listService.getListOfPersons(planetId);
	 * }
	 * </pre>
	 */
	@Bean
	@Primary
	public CacheManager listCacheManager(final RedisConnectionFactory redisConnectionFactory) {
		return build(redisConnectionFactory, new Jackson2JsonRedisSerializer<>(Object.class));
	}

	/**
	 * <p>
	 * Creates a {@link CacheManager} bean for caching objects using default
	 * serialization in the provisioned cache. This cache manager is intended to be
	 * used specifically for caching individual objects. When {@code @Caching}
	 * annotations are leveraged, it should be specified as follows:
	 * </p>
	 * 
	 * <pre>
	 * {@literal @}Cacheable(value = "person", key = "#personId")
	 * public Person getPerson(UUID personId) {
	 *     return objectService.getPerson(personId);
	 * }
	 * </pre>
	 */
	@Bean
	public CacheManager objectCacheManager(final RedisConnectionFactory redisConnectionFactory) {
		return build(redisConnectionFactory, RedisSerializer.json());
	}
	
	private CacheManager build(final RedisConnectionFactory redisConnectionFactory, final RedisSerializer<?> redisSerializer) {
		final var serializer = SerializationPair.fromSerializer(redisSerializer);
		return RedisCacheManagerBuilder.fromConnectionFactory(redisConnectionFactory)
				.cacheDefaults(
						RedisCacheConfiguration.defaultCacheConfig()
							.disableCachingNullValues()
							.serializeValuesWith(serializer))
				.build();
	}

}