package org.example.simple_sharding.config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.shardingsphere.driver.api.ShardingSphereDataSourceFactory;
import org.apache.shardingsphere.infra.config.algorithm.AlgorithmConfiguration;
import org.apache.shardingsphere.sharding.api.config.ShardingRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.rule.ShardingTableRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.sharding.StandardShardingStrategyConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

@Configuration
public class ShardingConfig {

    @Bean
    @Primary
    public DataSource shardingDataSource() throws SQLException {
        Map<String, DataSource> dataSourceMap = new HashMap<>();

        // Tạo datasource cho sharding
        dataSourceMap.put("master_0", createDataSource("master_0", "localhost:5432/postgres"));
        dataSourceMap.put("master_1", createDataSource("master_1", "localhost:5433/postgres"));

        // Sharding rules
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();

        ShardingTableRuleConfiguration orderTableRule = new ShardingTableRuleConfiguration(
                "orders",
                "master_0.orders_0,master_0.orders_1,master_1.orders_2,master_1.orders_3"
        );

        // Table sharding strategy với tenant_id
        orderTableRule.setTableShardingStrategy(new StandardShardingStrategyConfiguration(
                "tenant_id", "tenant_db_algo"));

        shardingRuleConfig.getTables().add(orderTableRule);

        // Custom Redis-based sharding algorithm
        Properties tenantProps = new Properties();
        tenantProps.setProperty("strategy", "standard");
        tenantProps.setProperty("algorithmClassName", "org.example.simple_sharding.sharding.TenantSharding");
        shardingRuleConfig.getShardingAlgorithms().put("tenant_db_algo",
                new AlgorithmConfiguration("TENANT_CLASS", tenantProps));

        // Global properties
        Properties globalProps = new Properties();
        globalProps.setProperty("sql-show", "true");
        globalProps.setProperty("sql-simple", "false");

        System.out.println("== DataSources check ==");
        for (Map.Entry<String, DataSource> entry : dataSourceMap.entrySet()) {
            System.out.println("Checking datasource: " + entry.getKey());
            try (Connection conn = entry.getValue().getConnection()) {
                System.out.println("  Connected: " + !conn.isClosed());
            } catch (Exception e) {
                System.out.println("  Error: " + e.getMessage());
            }
        }

        return ShardingSphereDataSourceFactory.createDataSource(dataSourceMap, Collections.singleton(shardingRuleConfig), globalProps);
    }

    // Datasource cho non-sharding operations (nếu cần)
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource regularDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:postgresql://localhost:5432/postgres");
        dataSource.setUsername("postgres");
        dataSource.setPassword("12345678");
        dataSource.setDriverClassName("org.postgresql.Driver");
        return dataSource;
    }

    private DataSource createDataSource(String name, String url) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:postgresql://" + url);
        dataSource.setUsername("postgres");
        dataSource.setPassword("12345678");
        dataSource.setDriverClassName("org.postgresql.Driver");

        // HikariCP config
        dataSource.setMaximumPoolSize(20);
        dataSource.setMinimumIdle(5);
        dataSource.setConnectionTimeout(30000);
        dataSource.setIdleTimeout(600000);
        dataSource.setMaxLifetime(1800000);
        dataSource.setLeakDetectionThreshold(60000);
        dataSource.setPoolName(name + "-pool");

        return dataSource;
    }
}