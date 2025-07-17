package org.example.simple_sharding.sharding;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.simple_sharding.sharding.dto.OrderShardingDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Utility service để setup và quản lý sharding mapping trong Redis
 */
@Service
public class RedisShardingUtility {

    @Autowired
    private StringRedisTemplate redis;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Tạo sharding mapping cho tenant
     */
    public void createShardingMapping(Long tenantId, String logicTable, String actualTable) {
        String key = "sharding::t:{" + tenantId%4 + "}:" + tenantId%4;
        String field = tenantId + "-" + logicTable;

        OrderShardingDTO dto = new OrderShardingDTO();
        dto.setTenantId(tenantId);
        dto.setLogicTableName(logicTable);
        dto.setActualTableName(actualTable);

        try {
            String jsonValue = objectMapper.writeValueAsString(dto);
            redis.opsForHash().put(key, field, jsonValue);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create sharding mapping", e);
        }
    }

    /**
     * Lấy sharding mapping cho tenant
     */
    public OrderShardingDTO getShardingMapping(String tenantId, String logicTable) {
        String key = "sharding::t:{" + tenantId + "}:" + tenantId;
        String field = tenantId + "-" + logicTable;

        Object val = redis.opsForHash().get(key, field);
        if (val == null) {
            return null;
        }

        try {
            return objectMapper.readValue(val.toString(), OrderShardingDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse sharding mapping", e);
        }
    }

    /**
     * Xóa sharding mapping cho tenant
     */
    public void deleteShardingMapping(String tenantId, String logicTable) {
        String key = "sharding::t:{" + tenantId + "}:" + tenantId;
        String field = tenantId + "-" + logicTable;
        redis.opsForHash().delete(key, field);
    }

    /**
     * Kiểm tra xem sharding mapping có tồn tại không
     */
    public boolean existsShardingMapping(String tenantId, String logicTable) {
        String key = "sharding::t:{" + tenantId + "}:" + tenantId;
        String field = tenantId + "-" + logicTable;
        return redis.opsForHash().hasKey(key, field);
    }
}