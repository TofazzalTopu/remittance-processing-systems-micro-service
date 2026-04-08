package com.info.replica;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class SpringBootShardingReadWriteReplicaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootShardingReadWriteReplicaApplication.class, args);
	}

}
