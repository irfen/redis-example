package me.irfen.redis.ch01;

import java.util.ResourceBundle;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisPoolInitialization {

	private static JedisPool pool;

	private JedisPoolInitialization() {
	}

	public static JedisPool getJedisPool() {
		if (pool == null) {
			ResourceBundle bundle = ResourceBundle.getBundle("redis");
			if (bundle == null) {
				throw new IllegalArgumentException("[redis.properties] is not found!");
			}
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxTotal(Integer.valueOf(bundle.getString("redis.pool.maxActive")));
			config.setMaxIdle(Integer.valueOf(bundle.getString("redis.pool.maxIdle")));
			config.setMaxWaitMillis(Long.valueOf(bundle.getString("redis.pool.maxWait")));
			config.setTestOnBorrow(Boolean.valueOf(bundle.getString("redis.pool.testOnBorrow")));
			config.setTestOnReturn(Boolean.valueOf(bundle.getString("redis.pool.testOnReturn")));
			pool = new JedisPool(config, bundle.getString("redis.ip"), Integer.valueOf(bundle.getString("redis.port")));
		}
		return pool;
	}
}
