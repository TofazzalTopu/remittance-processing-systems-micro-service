package com.info.replica.repository.shard2;

import com.info.replica.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface UserRepositoryShardTwo extends JpaRepository<User, Long> {
}

