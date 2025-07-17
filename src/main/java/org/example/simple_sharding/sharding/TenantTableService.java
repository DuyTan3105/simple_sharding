package org.example.simple_sharding.sharding;

public interface TenantTableService {
    String getTableName(Long tenantId, String logicTable);
}