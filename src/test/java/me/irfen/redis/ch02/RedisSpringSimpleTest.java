package me.irfen.redis.ch02;

import java.io.Serializable;

import javax.annotation.Resource;

import me.irfen.redis.ch02.entity.User;

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
public class RedisSpringSimpleTest extends AbstractJUnit4SpringContextTests {

	@Resource
	private RedisTemplate<Serializable, Serializable> redisTemplate;

	@Test
	public void testSave() {
		final User user = new User(1L, "irfenat", 24);

		redisTemplate.execute(new RedisCallback<Object>() {
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				String idKey = "user:id";
				String nameKey = "user:" + user.getId() + ":name";
				String ageKey = "user:" + user.getId() + ":age";

				connection.sAdd(redisTemplate.getStringSerializer().serialize(idKey),
						redisTemplate.getStringSerializer().serialize(user.getId().toString()));

				connection.set(redisTemplate.getStringSerializer().serialize(nameKey), redisTemplate.getStringSerializer().serialize(user.getName()));

				connection.set(redisTemplate.getStringSerializer().serialize(ageKey),
						redisTemplate.getStringSerializer().serialize(user.getAge().toString()));
				return null;
			}
		});
	}

	@Test
	public void testGet() {
		final Long id = 1L;

		User user = redisTemplate.execute(new RedisCallback<User>() {
			public User doInRedis(RedisConnection connection) throws DataAccessException {
				String idKey = "user:id";
				String nameKey = "user:" + id + ":name";
				String ageKey = "user:" + id + ":age";
				boolean isMember = connection.sIsMember(redisTemplate.getStringSerializer().serialize(idKey), redisTemplate.getStringSerializer()
						.serialize(id.toString()));
				if (isMember) {
					byte[] nameByte = connection.get(redisTemplate.getStringSerializer().serialize(nameKey));
					String name = redisTemplate.getStringSerializer().deserialize(nameByte);
					byte[] ageByte = connection.get(redisTemplate.getStringSerializer().serialize(ageKey));
					Integer age = Integer.valueOf(redisTemplate.getStringSerializer().deserialize(ageByte));
					return new User(id, name, age);
				}
				return null;
			}
		});

		System.out.println(user);
	}
}
