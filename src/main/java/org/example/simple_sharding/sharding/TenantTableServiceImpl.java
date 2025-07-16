package org.example.simple_sharding.sharding;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.simple_sharding.sharding.dto.OrderShardingDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class TenantTableServiceImpl implements TenantTableService {
    @Autowired
    private StringRedisTemplate redis;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public String getTableName(String tenantId, String logicTable) {
        String key = "sharding::t:{" + tenantId + "}:" + tenantId;
        String field = tenantId + "-" + logicTable;

        Object val = redis.opsForHash().get(key, field);
        if (val == null) throw new RuntimeException("Missing Redis entry");

        try {
            OrderShardingDTO dto = objectMapper.readValue(val.toString(), OrderShardingDTO.class);
            return dto.getActualTableName();
        } catch (Exception e) {
            throw new RuntimeException("JSON parse error", e);
        }
    }
}
