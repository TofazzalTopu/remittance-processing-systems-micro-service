package com.info.replica.repository.shard4;

import com.info.replica.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface UserRepositoryShardFour extends JpaRepository<User, Long> {
}

