package org.example.simple_sharding.sharding.dto;

public class OrderShardingDTO {
    private Long tenantId;
    private String logicTableName;
    private String actualTableName;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getLogicTableName() {
        return logicTableName;
    }

    public void setLogicTableName(String logicTableName) {
        this.logicTableName = logicTableName;
    }

    public String getActualTableName() {
        return actualTableName;
    }

    public void setActualTableName(String actualTableName) {
        this.actualTableName = actualTableName;
    }

    @Override
    public String toString() {
        return "OrderShardingDTO{" +
                "tenantId='" + tenantId + '\'' +
                ", logicTableName='" + logicTableName + '\'' +
                ", actualTableName='" + actualTableName + '\'' +
                '}';
    }

    public static String getDefaultTableName(Long tenantId, String logicTableName) {
        return logicTableName.concat("_").concat(String.valueOf(tenantId%4));
    }
}