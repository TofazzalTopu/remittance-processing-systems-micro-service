package com.info.replica.service;

import com.info.replica.dto.UserDTO;
import com.info.replica.entity.User;
import com.info.replica.repository.shard1.UserRepositoryShardOne;
import com.info.replica.repository.shard2.UserRepositoryShardTwo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepositoryShardOne userRepositoryShardOne;

    private final UserRepositoryShardTwo userRepositoryShardTwo;

    @Autowired
    public UserService(UserRepositoryShardOne userRepositoryShardOne, UserRepositoryShardTwo userRepositoryShardTwo) {
        this.userRepositoryShardOne = userRepositoryShardOne;
        this.userRepositoryShardTwo = userRepositoryShardTwo;
    }

    @Transactional
    public User save(UserDTO usr) {
        User user = new User(usr.getId(), usr.getName());
        return (User) userRepository(usr.getId()).save(user);
    }

    public Optional<User> getUser(Long userId) {
        return userRepository(userId).findById(userId);
    }

    public List<User> findAll() {
        return userRepositoryShardTwo.findAll(Sort.by("id").ascending());
    }

    public <T> CrudRepository userRepository(Long userId) {
        return userId % 2 == 0 ? userRepositoryShardOne : userRepositoryShardTwo;
    }
}
