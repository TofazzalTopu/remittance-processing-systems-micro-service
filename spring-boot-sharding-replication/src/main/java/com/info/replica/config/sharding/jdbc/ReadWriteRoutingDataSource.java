package com.info.replica.config.sharding.jdbc;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class ReadWriteRoutingDataSource extends AbstractRoutingDataSource {

    private static final ThreadLocal<Boolean> readOnlyContext = new ThreadLocal<>();

    public static void setReadOnly(boolean isReadOnly) {
        readOnlyContext.set(isReadOnly);
    }

    @Override
    protected Object determineCurrentLookupKey() {
        Boolean isReadOnly = readOnlyContext.get();
        return (isReadOnly != null && isReadOnly) ? "read" : "write";
    }


}
