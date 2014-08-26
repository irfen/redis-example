package me.irfen.redis.ch06;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class RedisSubTest extends AbstractJUnit4SpringContextTests {

	@Resource
	private RedisTemplate<String, String> redisTemplate;

	@Before
	public void before() {
		redisTemplate.setKeySerializer(redisTemplate.getStringSerializer());
		redisTemplate.setValueSerializer(redisTemplate.getStringSerializer());
	}
	
	@Test
	public void testSubscribe() {
		redisTemplate.execute(new RedisCallback<Object>() {

			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				MessageListener listener = new SubListener();
				connection.subscribe(listener, redisTemplate.getStringSerializer().serialize("channel1.1"));
				return null;
			}
		});
	}
	
	private class SubListener implements MessageListener {

		public void onMessage(Message message, byte[] pattern) {
			byte[] body = message.getBody();
			byte[] channel = message.getChannel();
			String content = redisTemplate.getStringSerializer().deserialize(body);
			String publisher = redisTemplate.getStringSerializer().deserialize(channel);
			System.out.println(publisher + ": " + content);
		}
		
	}
}
