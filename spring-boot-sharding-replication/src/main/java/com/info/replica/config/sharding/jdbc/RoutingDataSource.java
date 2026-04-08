package com.info.replica.config.sharding.jdbc;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class RoutingDataSource extends AbstractRoutingDataSource {

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    public static void setShard(String shard) {
        contextHolder.set(shard);
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return contextHolder.get();
    }

}
