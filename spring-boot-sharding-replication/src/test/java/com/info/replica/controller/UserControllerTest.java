package com.info.replica.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.info.replica.dto.UserDTO;
import com.info.replica.entity.User;
import com.info.replica.service.UserReadService;
import com.info.replica.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserReadService userReadService;

    @InjectMocks
    private UserController userController;

    @Autowired
    private MockMvc mockMvc;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        user = new User();
        user.setId(1L);
        user.setName("Tofazzal");
        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setName("Tofazzal");
    }

    @Test
    void testSaveUser() throws Exception {
        when(userService.save(any(UserDTO.class))).thenReturn(user);
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", new URI("/users").toString()))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Tofazzal"));
        verify(userService, times(1)).save(any(UserDTO.class));
    }

    @Test
    void testFindById_UserFound() throws Exception {
        when(userReadService.getUser(1L)).thenReturn(Optional.of(user));
        mockMvc.perform(get("/users/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Tofazzal"));
        verify(userReadService, times(1)).getUser(1L);
    }

    @Test
    void testFindById_UserNotFound() throws Exception {
        when(userReadService.getUser(2L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/users/2").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(userReadService, times(1)).getUser(2L);
    }

    @Test
    void testFindAllUsers() throws Exception {
        List<User> users = List.of(user);
        when(userReadService.findAll()).thenReturn(users);
        mockMvc.perform(get("/users").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Tofazzal"));
        verify(userReadService, times(1)).findAll();
    }
}
