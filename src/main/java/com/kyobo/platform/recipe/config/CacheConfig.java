//package com.kyobo.platform.recipe.config;
//
//import java.time.Duration;
//
//import org.springframework.cache.CacheManager;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.cache.RedisCacheConfiguration;
//import org.springframework.data.redis.cache.RedisCacheManager;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.RedisSerializationContext;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
//@Configuration
//@EnableCaching
//public class CacheConfig {
//	
//	@Bean
//    public CacheManager cacheManager(RedisConnectionFactory cf){
//        RedisCacheConfiguration redisConfiguration = RedisCacheConfiguration.defaultCacheConfig()
//                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer())) 
//                .entryTtl(Duration.ofSeconds(30)); //TTL 적용도 가능하다. 
//
//		RedisCacheManager redisCacheManager = RedisCacheManager.RedisCacheManagerBuilder
//				.fromConnectionFactory(cf) //Connect 적용하고
//                .cacheDefaults(redisConfiguration).build();  //캐쉬설정과 관련된 것을 여기에 적용. 
//        return redisCacheManager;
//    }
//}
