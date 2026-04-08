package com.info.replica.service.migration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class UserMigrationBatch {

    private static final Logger logger = LoggerFactory.getLogger(UserMigrationBatch.class);

    @Autowired
    private UserMigrationService userMigrationService;

    @Scheduled(fixedDelay = 5000)  // Run every 5 seconds
    public void migrateUsers() {
//        String sql = "INSERT INTO new_shard.users SELECT * FROM old_shard.users WHERE MOD(id, 3) = 2";
        userMigrationService.saveIntoTheShardThree();
        logger.info("Data migrated to new shard.");
    }
}
