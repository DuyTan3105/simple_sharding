package org.example.simple_sharding.sharding;

import org.apache.shardingsphere.sharding.api.sharding.standard.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class TenantSharding implements StandardShardingAlgorithm<Comparable<Object>>
{
    @Autowired
    private TenantTableService tableService;

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Comparable<Object>> shardingValue) {
        String tenantId = shardingValue.getValue().toString();
        String logicTable = shardingValue.getLogicTableName();
        String actualTable = tableService.getTableName(tenantId, logicTable);

        return availableTargetNames.stream()
                .filter(actualTable::equals)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No matching table"));
    }

    @Override
    public Collection<String> doSharding(Collection<String> collection, RangeShardingValue<Comparable<Object>> rangeShardingValue) {
        return List.of();
    }

    @Override public String getType() { return "TENANT_CLASS"; }
    @Override public void init(java.util.Properties props) {}
    @Override public java.util.Properties getProps() { return new java.util.Properties(); }
}
