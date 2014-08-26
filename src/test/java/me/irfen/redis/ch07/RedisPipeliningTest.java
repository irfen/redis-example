package me.irfen.redis.ch07;

import java.util.List;

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
public class RedisPipeliningTest extends AbstractJUnit4SpringContextTests {

	@Resource
	private RedisTemplate<String, String> redisTemplate;

	@Before
	public void before() {
		redisTemplate.setKeySerializer(redisTemplate.getStringSerializer());
		redisTemplate.setValueSerializer(redisTemplate.getStringSerializer());
	}

	@Test
	public void testPipelining() {
		List<Object> list = redisTemplate.executePipelined(new RedisCallback<Object>() {

			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				connection.lPush(redisTemplate.getStringSerializer().serialize("testpipelining1"),
						redisTemplate.getStringSerializer().serialize("1"));
				connection.set(redisTemplate.getStringSerializer().serialize("testpipelining2"),
						redisTemplate.getStringSerializer().serialize("2"));
				connection.lPush(redisTemplate.getStringSerializer().serialize("testpipelining1"),
						redisTemplate.getStringSerializer().serialize("2"));
				return null;
			}
		});
		for (Object o : list) {
			System.out.println(o);
		}
	}
}
