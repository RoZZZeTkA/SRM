package com.grigorev.srm.service;

import com.grigorev.srm.dao.UserDAO;
import com.grigorev.srm.entity.Role;
import com.grigorev.srm.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    UserService userService;

    @Mock
    UserDAO userDAO;

    @Mock
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userService = new UserService(userDAO, passwordEncoder);
    }

    @Test
    void canFindAllUser() {
        userService.findAll();

        verify(userDAO, times(1)).findAll();
    }

    @Test
    void canFindUserById() {
        Integer id = 1;

        userService.findById(id);

        verify(userDAO, times(1)).findById(id);
    }

    @Test
    void canFindUserByUsername() {
        String username = "tom";

        userService.findByUsername(username);

        verify(userDAO, times(1)).findByUsername(username);
    }

    @Test
    void canSaveUser() {
        User dumUser = new User(Role.ADMIN, "test", "test");
        User userToSave = new User(Role.ADMIN, "tom", "tom");
        when(userDAO.save(any(User.class))).thenReturn(dumUser);
        String emptyString = "";

        User user = userService.save(userToSave);

        verify(passwordEncoder, times(1)).encode(anyString());
        verify(userDAO, times(1)).save(userToSave);
        assertEquals(emptyString, user.getPassword());
    }

    @Test
    void cannotSaveEmptyUser() {
        User dumUser = new User(null, null, null);

        assertThrows(IllegalStateException.class, () -> userService.save(dumUser));
    }

    @Test
    void cannotSaveUserWithExistingUsername() {
        User userToSave = new User(Role.ADMIN, "tom", "tom");
        String message = "Username " + userToSave.getUsername() + " is already taken";
        when(userDAO.existsByUsername(anyString())).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> userService.save(userToSave), message);
    }
}
