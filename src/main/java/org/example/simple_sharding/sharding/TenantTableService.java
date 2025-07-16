package org.example.simple_sharding.sharding;

public interface TenantTableService {
    String getTableName(String tenantId, String logicTable);
}
