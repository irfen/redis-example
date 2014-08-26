package me.irfen.redis.ch03;

import java.util.Iterator;
import java.util.Set;

import javax.annotation.Resource;

import me.irfen.redis.ch02.entity.User;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class RedisSpringOperationsTest extends AbstractJUnit4SpringContextTests {

	@Resource
	private RedisTemplate<String, String> redisTemplate;

	@Test
	public void testSave() {
		User user = new User(2L, "irfen", 24);
		String idKey = "user:id";
		String nameKey = "user:" + user.getId() + ":name";
		String ageKey = "user:" + user.getId() + ":age";

		redisTemplate.opsForSet().add(idKey, user.getId().toString());
		redisTemplate.opsForValue().set(nameKey, user.getName());
		redisTemplate.opsForValue().set(ageKey, user.getAge().toString());
	}

	@Test
	public void testGet() {
		Long id = 2L;
		String idKey = "user:id";
		String nameKey = "user:" + id + ":name";
		String ageKey = "user:" + id + ":age";
		boolean isMember = redisTemplate.opsForSet().isMember(idKey, id.toString());
		if (isMember) {
			String name = redisTemplate.opsForValue().get(nameKey);
			Integer age = Integer.valueOf(redisTemplate.opsForValue().get(ageKey));
			User user = new User(id, name, age);
			System.out.println(user);
		}
	}

	@Test
	public void testKeys() {
		Set<String> keys = redisTemplate.keys("*"); // 不好用
		System.out.println(keys.size());
		for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			System.out.println(key);
		}
	}
}
