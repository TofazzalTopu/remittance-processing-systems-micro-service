package com.info.replica.config.sharding.jdbc;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ShardingDataSourceConfig {

    @Bean(name = "shardOneDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.shard1")
    public DataSource shardOneDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "shardTwoDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.shard2")
    public DataSource shardTwoDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "shardThreeDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.shard3")
    public DataSource shardThreeDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "readWriteRoutingDataSource")
    public DataSource routingDataSource(@Qualifier("shardOneDataSource") DataSource shardOne,
                                        @Qualifier("shardTwoDataSource") DataSource shardTwo,
                                        @Qualifier("shardThreeDataSource") DataSource shardThree) {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("shardOne", shardOne);
        targetDataSources.put("shardTwo", shardTwo);
        targetDataSources.put("shardThree", shardThree);

        ReadWriteRoutingDataSource routingDataSource = new ReadWriteRoutingDataSource();
        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.setDefaultTargetDataSource(shardOne);
        return routingDataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(@Qualifier("readWriteRoutingDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}

