package com.info.replica.service.migration;

import com.info.replica.entity.User;
import com.info.replica.repository.shard1.UserRepositoryShardOne;
import com.info.replica.repository.shard2.UserRepositoryShardTwo;
import com.info.replica.repository.shard3.UserRepositoryShardThree;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserMigrationService {

    private final UserRepositoryShardOne userRepositoryShardOne;

    private final UserRepositoryShardTwo userRepositoryShardTwo;
    private final UserRepositoryShardThree userRepositoryShardThree;


    public UserMigrationService(UserRepositoryShardOne userRepositoryShardOne, UserRepositoryShardTwo userRepositoryShardTwo, UserRepositoryShardThree userRepositoryShardThree) {
        this.userRepositoryShardOne = userRepositoryShardOne;
        this.userRepositoryShardTwo = userRepositoryShardTwo;
        this.userRepositoryShardThree = userRepositoryShardThree;
    }

    public List<User> findAllFromShardOne() {
        return userRepositoryShardOne.findAll(Sort.by("id").ascending());
    }

    public List<User> findAllFromShardTwo() {
        return userRepositoryShardTwo.findAll(Sort.by("id").ascending());
    }


    public List<User> saveIntoTheShardThree() {
        return userRepositoryShardThree.saveAll(findAllFromShardOne());
    }


}
