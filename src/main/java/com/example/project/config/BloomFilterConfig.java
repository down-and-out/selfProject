package com.example.project.config;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BloomFilterConfig {

    // 用户布隆过滤器名称
    private static final String USER_BLOOM_FILTER_NAME = "userBloomFilter";
    // 预计元素数量
    private static final long EXPECTED_ELEMENTS = 100000;
    // 误判率
    private static final double FALSE_PROBABILITY = 0.01;

    /**
     * 用户布隆过滤器配置
     */
    @Bean
    public RBloomFilter<String> userBloomFilter(RedissonClient redissonClient) {
        // 获取或创建布隆过滤器
        RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter(USER_BLOOM_FILTER_NAME);
        // 初始化布隆过滤器
        bloomFilter.tryInit(EXPECTED_ELEMENTS, FALSE_PROBABILITY);
        return bloomFilter;
    }

    // TODO 可以添加布隆过滤器自动扩容，缩容机制
}