package com.info.replica.repository.shard1;

import com.info.replica.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepositoryShardOne extends JpaRepository<User, Long> {
}

