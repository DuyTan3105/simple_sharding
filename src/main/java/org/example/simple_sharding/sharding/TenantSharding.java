package org.example.simple_sharding.sharding;

import groovy.util.logging.Slf4j;
import org.apache.shardingsphere.sharding.api.sharding.standard.*;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TenantSharding implements StandardShardingAlgorithm<Long>, ApplicationContextAware {

    private static ApplicationContext applicationContext;
    private TenantTableService tableService;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        TenantSharding.applicationContext = context;
    }

    private TenantTableService getTableService() {
        if (tableService == null) {
            tableService = applicationContext.getBean(TenantTableService.class);
        }
        return tableService;
    }

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> shardingValue) {
        Long tenantId = shardingValue.getValue();
        String logicTable = shardingValue.getLogicTableName();

        try {
            String actualTable = getTableService().getTableName(tenantId, logicTable);

            return availableTargetNames.stream()
                    .filter(actualTable::equals)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(
                            "No matching table found for tenant_id: " + tenantId +
                                    ", expected table: " + actualTable +
                                    ", available tables: " + availableTargetNames));

        } catch (Exception e) {
            System.out.println("Error in doSharding: " + e.getMessage());
            return null; // Hoặc ném ra một ngoại lệ tùy ý
        }
    }

    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, RangeShardingValue<Long> rangeShardingValue) {
        String logicTable = rangeShardingValue.getLogicTableName();

        // Filter chỉ những tables liên quan đến logic table này
        return availableTargetNames.stream()
                .filter(tableName -> tableName.startsWith(logicTable + "_"))
                .collect(Collectors.toList());
    }

    @Override
    public String getType() {
        return "TENANT_CLASS";
    }

    @Override
    public void init(java.util.Properties props) {
        // Initialization logic nếu cần
    }

    @Override
    public java.util.Properties getProps() {
        return new java.util.Properties();
    }
}