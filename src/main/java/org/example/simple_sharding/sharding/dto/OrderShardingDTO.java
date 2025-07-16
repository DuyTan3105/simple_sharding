package org.example.simple_sharding.sharding.dto;

public class OrderShardingDTO
{
    private String tenantId;
    private String logicTableName;
    private String actualTableName;

    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }

    public String getLogicTableName() { return logicTableName; }
    public void setLogicTableName(String logicTableName) { this.logicTableName = logicTableName; }

    public String getActualTableName() { return actualTableName; }
    public void setActualTableName(String actualTableName) { this.actualTableName = actualTableName; }
}
