package me.irfen.redis.ch06;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class RedisPubTest extends AbstractJUnit4SpringContextTests {

	@Resource
	private RedisTemplate<String, String> redisTemplate;

	@Before
	public void before() {
		redisTemplate.setKeySerializer(redisTemplate.getStringSerializer());
		redisTemplate.setValueSerializer(redisTemplate.getStringSerializer());
	}

	@Test
	public void testPublish() {
		Long num = redisTemplate.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.publish(redisTemplate.getStringSerializer().serialize("channel1.1"),
						redisTemplate.getStringSerializer().serialize("hi"));
			}
		});
		System.out.println("publisher: " + num + " subscriber received");
	}
}
