package com.kyobo.platform.recipe.redis;

//import org.springframework.data.redis.core.RedisHash;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@RedisHash(value = "redisUser", timeToLive = 10)
public class RedisUser {
	private String id;
	
	private String username;
	
	private String password;
	
	private String email;
}
