package com.info.replica.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.info.replica.entity.User;
import com.info.replica.repository.shard3.UserRepositoryShardThree;
import com.info.replica.repository.shard4.UserRepositoryShardFour;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserReadServiceTest {

    @Mock
    private UserRepositoryShardThree userRepositoryShardThree;
    @Mock
    private UserRepositoryShardFour userRepositoryShardFour;

    @InjectMocks
    private UserReadService userReadService;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setId(1L);
        user1.setName("John Doe");


        user2 = new User();
        user2.setId(2L);
        user2.setName("Rana");
    }

    @Test
    void getUser_OddUserId_ReturnsUserFromShardFour() {
        when(userRepositoryShardFour.findById(1L)).thenReturn(Optional.of(user1));
        Optional<User> result = userReadService.getUser(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("John Doe", result.get().getName());
        verify(userRepositoryShardFour, times(1)).findById(1L);
    }

    @Test
    void when_GetUser_NotFound_ForOddUserId_FromShardFour() {
        when(userRepositoryShardFour.findById(1L)).thenReturn(Optional.empty());
        Optional<User> result = userReadService.getUser(1L);
        assertFalse(result.isPresent());
        verify(userRepositoryShardFour, times(1)).findById(1L);
    }

    @Test
    void getUser_EvenUserId_ReturnsUserFromShardThree() {
        when(userRepositoryShardThree.findById(2L)).thenReturn(Optional.of(user2));
        Optional<User> result = userReadService.getUser(2L);
        assertTrue(result.isPresent());
        assertEquals(2L, result.get().getId());
        assertEquals("Rana", result.get().getName());
        verify(userRepositoryShardThree, times(1)).findById(2L);
    }

    @Test
    void when_GetUser_NotFound_ForEvenUserId_FromShardThree() {
        when(userRepositoryShardThree.findById(2L)).thenReturn(Optional.empty());
        Optional<User> result = userReadService.getUser(2L);
        assertFalse(result.isPresent());
        verify(userRepositoryShardThree, times(1)).findById(2L);
    }

    @Test
    void testFindAll() {
        List<User> users = List.of(user2);
        when(userRepositoryShardThree.findAll(Sort.by("id").ascending())).thenReturn(users);
        List<User> result = userReadService.findAll();
        assertEquals(1, result.size());
        assertEquals(2L, result.get(0).getId());
        verify(userRepositoryShardThree, times(1)).findAll(Sort.by("id").ascending());
    }


    @Test
    void findAll_InvokesRepositoryWithCorrectSortOrder() {
        List<User> mockUsers = Arrays.asList(new User(), new User());
        when(userRepositoryShardThree.findAll(any(Sort.class))).thenReturn(mockUsers);
        List<User> result = userReadService.findAll();
        assertEquals(mockUsers, result);

        ArgumentCaptor<Sort> sortCaptor = ArgumentCaptor.forClass(Sort.class);
        verify(userRepositoryShardThree).findAll(sortCaptor.capture());

        Sort capturedSort = sortCaptor.getValue();
        assertNotNull(capturedSort.getOrderFor("id"));
        assertEquals(Sort.Direction.ASC, capturedSort.getOrderFor("id").getDirection());
    }


    @Test
    void get_UserRepository_When_EvenUserId_ReturnsShardThree() {
        CrudRepository repository = userReadService.getUserReadRepository(2L);
        assertEquals(userRepositoryShardThree, repository);
    }

    @Test
    void userRepository_NotFound_When_EvenUserId_ReturnsShardFour() {
        CrudRepository repository = userReadService.getUserReadRepository(2L);
        assertNotEquals(userRepositoryShardFour, repository);
    }

    @Test
    void get_UserRepository_when_OddUserId_ReturnsShardFour() {
        CrudRepository repository = userReadService.getUserReadRepository(3L);
        assertEquals(userRepositoryShardFour, repository);
    }

    @Test
    void get_UserRepository_NotFound_when_OddUserId_ReturnsShardThree() {
        CrudRepository repository = userReadService.getUserReadRepository(3L);
        assertNotEquals(userRepositoryShardThree, repository);
    }
}
