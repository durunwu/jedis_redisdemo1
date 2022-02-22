package com.atguigu.jedis;

import org.testng.annotations.Test;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Set;

public class JedisDemo1 {
    public static void main(String[] args) {
        //1.创建jedis对象，根据ip，端口与redis创建连接
        Jedis jedis = new Jedis("192.168.2.130",6379);//IP,端口

        //2.测试
        String ping = jedis.ping();
        System.out.println(ping);//输出PONG证明连接成功
    }

    @Test
    public void demo1(){
        Jedis jedis = new Jedis("192.168.2.130",6379);//IP,端口
        Set<String> keys = jedis.keys("*");
        for(String key:keys){
            System.out.println(key);
        }

        jedis.set("tom","123");
        System.out.println(jedis.get("tom"));

        jedis.lpush("k1","v1","v2","v3");
        List<String> k1 = jedis.lrange("k1", 0, -1);
        System.out.println(k1);

        jedis.lpush("y1","y1","y2","y3");
        List<String> y1 = jedis.lrange("y1", 0, -1);
        System.out.println(y1);

        //hash
        jedis.hset("key1","a1","123");
        System.out.println(jedis.hget("key1", "a1"));

        //zset
        jedis.zadd("china",200,"mysql");
        jedis.zadd("china",300,"java");
        Set<String> china = jedis.zrange("china", 0, -1);
        System.out.println(china);

    }
}
