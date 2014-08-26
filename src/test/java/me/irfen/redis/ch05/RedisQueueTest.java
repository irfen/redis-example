package me.irfen.redis.ch05;

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
public class RedisQueueTest extends AbstractJUnit4SpringContextTests {

	@Resource
	private RedisTemplate<String, String> redisTemplate;
	
	@Before
	public void before() {
		redisTemplate.setKeySerializer(redisTemplate.getStringSerializer());
		redisTemplate.setValueSerializer(redisTemplate.getStringSerializer());
	}
	
	@Test
	public void testQueue() {
		String elem = redisTemplate.execute(new RedisCallback<String>() {
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				List<byte[]> l = connection.bLPop(0, redisTemplate.getStringSerializer().serialize("queue"));
				if (l == null)
				{
					return null;
				}
				return redisTemplate.getStringSerializer().deserialize(l.get(1));
			}
		});
		System.out.println(elem);
	}
	
}
