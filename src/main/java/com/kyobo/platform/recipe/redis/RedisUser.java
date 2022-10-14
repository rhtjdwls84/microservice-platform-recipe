package com.kyobo.platform.recipe.redis;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.redis.core.RedisHash;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@RedisHash(value = "redisUser", timeToLive = 10)
public class RedisUser {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;
	
	@Column
	private String username;
	
	private String password;
	
	@Column
	private String email;
}
