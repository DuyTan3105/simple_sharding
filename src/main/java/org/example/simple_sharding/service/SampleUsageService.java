package org.example.simple_sharding.service;

import org.example.simple_sharding.sharding.RedisShardingUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service mẫu để demo cách sử dụng sharding system
 */
@Service
public class SampleUsageService {

    @Autowired
    private RedisShardingUtility redisShardingUtility;

    public void run(String... args) throws Exception {
        // Setup sample sharding mappings
        setupSampleShardingMappings();
    }

    /**
     * Tạo sample sharding mappings trong Redis
     */
    public void setupSampleShardingMappings() {
        try {
            // Tạo mapping cho các tenant khác nhau
            redisShardingUtility.createShardingMapping(0L, "orders", "orders_0");
            redisShardingUtility.createShardingMapping(1L, "orders", "orders_1");
            redisShardingUtility.createShardingMapping(2L, "orders", "orders_2");
            redisShardingUtility.createShardingMapping(3L, "orders", "orders_3");

            System.out.println("Sample sharding mappings created successfully!");

        } catch (Exception e) {
            System.err.println("Failed to setup sample sharding mappings: " + e.getMessage());
        }
    }
}