package com.info.replica.config.sharding.jpa;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.info.replica.repository.shard3", entityManagerFactoryRef = "shard3EntityManagerFactory", transactionManagerRef = "shard3TransactionManager")
public class ThirdShardConfig {

    @Value("${spring.jpa.database-platform}")
    private String HIBERNATE_DIALECT;

    @Bean(name = "shard3DataSource")
    @ConfigurationProperties(prefix = "spring.datasource.shard3")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "shard3EntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier("shard3DataSource") DataSource dataSource, EntityManagerFactoryBuilder builder) {
        return builder.dataSource(dataSource)
                .packages("com.info.replica.entity") // Entity package
                .persistenceUnit("shard3")
                .properties(Map.of("hibernate.dialect", HIBERNATE_DIALECT)) // Explicitly set dialect
                .build();
    }

    @Bean(name = "shard3TransactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("shard3EntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}

