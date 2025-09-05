//package com.example.chat.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
//import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
//import org.springframework.data.redis.core.StringRedisTemplate;
//
//@Configuration
//public class RedisConfig {
//
//    @Bean
//    public LettuceConnectionFactory redisConnectionFactory() {
//        // 필요한 경우 host/port/password 는 application.yml 로 분리
//        RedisStandaloneConfiguration conf = new RedisStandaloneConfiguration("localhost", 6379);
//        return new LettuceConnectionFactory(conf);
//    }
//
//    @Bean
//    public StringRedisTemplate stringRedisTemplate(LettuceConnectionFactory cf) {
//        return new StringRedisTemplate(cf);
//    }
//}