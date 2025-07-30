package org.example.simple_sharding.sharding;

import org.apache.shardingsphere.sharding.api.sharding.standard.*;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.*;

public class CountryShardingAlgorithm implements StandardShardingAlgorithm<String>, ApplicationContextAware {

    private static final Map<String, String> COUNTRY_TABLE_MAP = Map.ofEntries(
            Map.entry("VN", "orders_0"),
            Map.entry("TH", "orders_0"),
            Map.entry("US", "orders_1"),
            Map.entry("CA", "orders_1"),
            Map.entry("JP", "orders_2"),
            Map.entry("KR", "orders_3")
    );

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<String> shardingValue) {
        String countryCode = shardingValue.getValue().toUpperCase();
        String targetTable = COUNTRY_TABLE_MAP.get(countryCode);

        if (targetTable == null) {
            throw new IllegalArgumentException("No target table for country: " + countryCode);
        }

        return availableTargetNames.stream()
                .filter(targetTable::equals)
                .findFirst().orElse(null);
    }

    @Override
    public Collection<String> doSharding(Collection<String> collection, RangeShardingValue<String> rangeShardingValue) {
        return List.of();
    }

    @Override
    public Properties getProps() {
        return new java.util.Properties();
    }

    @Override
    public void init(Properties properties) {

    }

    @Override
    public String getType() {
        return "COUNTRY_CLASS";
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }
}

