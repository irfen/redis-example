package me.irfen.redis.ch01;

import java.util.Iterator;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisSimpleTest {

	@Test
	public void simpleJedis() {
		@SuppressWarnings("resource")
		Jedis jedis = new Jedis("192.168.50.201");
		Set<String> keys = jedis.keys("*"); // 列出所有key
		for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
			String key = iterator.next();
			System.out.println(key);
		}
		System.out.println("====================");
		String k = "hello";
		System.out.println(jedis.set(k, "world"));
		System.out.println(jedis.get(k));

		jedis.del(k);
		System.out.println(jedis.get(k));

	}

	@Test
	public void poolJedis() {
		JedisPool pool = JedisPoolInitialization.getJedisPool();
		Jedis jedis = pool.getResource();
		jedis.set("abc", "irf");
		System.out.println(jedis.get("abc"));
		pool.returnResource(jedis);
	}

}
