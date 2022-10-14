package com.kyobo.platform.recipe.redis;

import java.util.Optional;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.qos.logback.classic.Logger;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisService {
	private static final Logger logger = (Logger) LoggerFactory.getLogger(RedisService.class);
	
	@Autowired
    private RedisRepository redisRepository;
	
	// Redis 세션 저장
	public String redisSaveSession(String id, String username) {
		logger.info("====================== redisSaveSession start ======================");
		RedisUser redisUser = new RedisUser();
        // 저장
		redisUser.setId(id);
		redisUser.setUsername(username);
		redisRepository.save(redisUser);
		logger.info("====================== redisSaveSession end ======================");
		return "Hello Kyobo_ppp";
	}
	
	// Redis 세션 체크
	public Optional<RedisUser> redisGetSession(String id) {
		logger.info("====================== redisGetSession start ======================");
		logger.info("====================== redisGetSession end ======================");
		return redisRepository.findById(id);
	}
}
