package com.info.replica.service;

import com.info.replica.entity.User;
import com.info.replica.repository.shard3.UserRepositoryShardThree;
import com.info.replica.repository.shard4.UserRepositoryShardFour;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserReadService {

    private final UserRepositoryShardThree userRepositoryShardThree;
    private final UserRepositoryShardFour userRepositoryShardFour;

    @Autowired
    public UserReadService(UserRepositoryShardThree userRepositoryShardThree, UserRepositoryShardFour userRepositoryShardFour) {
        this.userRepositoryShardThree = userRepositoryShardThree;
        this.userRepositoryShardFour = userRepositoryShardFour;
    }

    public Optional<User> getUser(Long userId) {
        return getUserReadRepository(userId).findById(userId);
    }

    public List<User> findAll() {
        return userRepositoryShardThree.findAll(Sort.by("id").ascending());
    }

    public <T> CrudRepository getUserReadRepository(Long userId) {
        return userId % 2 == 0 ? userRepositoryShardThree : userRepositoryShardFour;
    }
}
