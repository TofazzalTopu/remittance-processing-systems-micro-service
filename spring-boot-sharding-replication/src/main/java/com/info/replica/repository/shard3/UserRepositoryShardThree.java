package com.info.replica.repository.shard3;

import com.info.replica.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface UserRepositoryShardThree extends JpaRepository<User, Long> {
}

