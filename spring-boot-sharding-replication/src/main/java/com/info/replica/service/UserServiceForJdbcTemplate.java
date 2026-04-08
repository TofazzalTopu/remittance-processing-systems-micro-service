package com.info.replica.service;

import com.info.replica.config.sharding.jdbc.ReadWriteRoutingDataSource;
import com.info.replica.config.sharding.jdbc.RoutingDataSource;
import com.info.replica.dto.UserDTO;
import com.info.replica.entity.User;
import com.info.replica.mapper.UserRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserServiceForJdbcTemplate {

    private final Logger logger = LoggerFactory.getLogger(UserServiceForJdbcTemplate.class);
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserServiceForJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Transactional
    public void save(UserDTO user) {
        RoutingDataSource.setShard(determineShard(user.getId()));
        ReadWriteRoutingDataSource.setReadOnly(false);  // Use primary
        String sql = "INSERT INTO USER_TEST (id, user_name) VALUES (?, ?)";
        jdbcTemplate.update(sql, user.getId(), user.getName());
        logger.info("User saved successfully with ID: {} ", user.getId());
    }

    public User findById(Long userId) {
        RoutingDataSource.setShard(determineShard(userId));
        ReadWriteRoutingDataSource.setReadOnly(true);  // Use primary
        String sql = "SELECT * FROM USER_TEST WHERE ID = ?";
        User user = null;
        try {
            user = jdbcTemplate.queryForObject(sql, new UserRowMapper(), userId);
        } catch (EmptyResultDataAccessException e) {
            logger.error("User not found with ID: {} ", userId);
        }
        return user;
    }


    private String determineShard(Long userId) {
        int shardIndex = (int) (userId % 3);  // Now we have 3 shards
        return "shard" + (shardIndex + 1);    // Returns "shard1", "shard2", or "shard3"
    }


}
