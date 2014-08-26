package me.irfen.redis.ch04;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.DefaultSortParameters;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.SortParameters;
import org.springframework.data.redis.connection.SortParameters.Order;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.query.SortQuery;
import org.springframework.data.redis.core.query.SortQueryBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class RedisSortTest extends AbstractJUnit4SpringContextTests {

	@Resource
	private RedisTemplate<String, Serializable> redisTemplate;
	
	@Before
	public void before() {
		redisTemplate.setKeySerializer(redisTemplate.getStringSerializer());
		redisTemplate.setValueSerializer(redisTemplate.getStringSerializer());
	}
	
	@Test
	public void testSort() {
		List<String> testList = redisTemplate.execute(new RedisCallback<List<String>>() {
			public List<String> doInRedis(RedisConnection connection) throws DataAccessException {
				SortParameters sp = new DefaultSortParameters(new SortParameters.Range(1, 2), Order.DESC, false);
				List<byte[]> l = connection.sort(redisTemplate.getStringSerializer().serialize("testlist"), sp);
				List<String> list = new ArrayList<String>();
				for (byte[] b : l) {
					list.add(redisTemplate.getStringSerializer().deserialize(b));
				}
				return list;
			}
		});
		
		for (String str : testList) {
			System.out.println(str);
		}
	}
	
	@Test
	public void testSortOperations() {
		SortQuery<String> query = SortQueryBuilder.sort("testlist").build();
		List<String> testList = redisTemplate.sort(query, redisTemplate.getStringSerializer());
		for (String str : testList) {
			System.out.println(str);
		}
	}
}
