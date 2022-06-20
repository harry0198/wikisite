package com.harrydrummond.projecthjd.service;

import com.harrydrummond.projecthjd.user.*;
import com.harrydrummond.projecthjd.user.roles.Role;
import com.harrydrummond.projecthjd.user.roles.UserRole;
import com.harrydrummond.projecthjd.user.saves.UserSaves;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    public void generateUser() {
        user = new User();
        user.setEmail("example@email.com");
        user.setUsername("harry");
        user.setUserRoles(Set.of(new UserRole(Role.USER)));
        user.setProvider(Provider.LOCAL);
        user.setDateCreated(LocalDateTime.now());
        user.setLocked(false);
        user.setEnabled(false);
        user.setId(1L);
    }

    @Test
    public void testSaveUser() {
        // given - precondition or setup
        given(userRepository.save(user)).willReturn(user);

        // when -  action or the behaviour that we are going test
        User savedUser = userService.saveUser(user);

        // then - verify the output
        assertThat(savedUser).isNotNull();
    }

    @Test
    public void testGetUserById() {
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        // when
        User savedEmployee = userService.getUserById(user.getId()).get();

        // then
        assertThat(savedEmployee).isNotNull();
    }

    @Test
    public void testGetAllUsers() {
        User user1 = new User();
        user1.setEmail("example2@email.com");
        user1.setUsername("harryd");
        user1.setUserRoles(Set.of(new UserRole(Role.USER)));
        user1.setProvider(Provider.LOCAL);
        user1.setDateCreated(LocalDateTime.now());
        user1.setId(2L);

        given(userRepository.findAll()).willReturn(List.of(user,user1));

        // when -  action or the behaviour that we are going test
        List<User> employeeList = userService.getAllUsers();

        // then - verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

//    @Test
//    public void testUpdateUser() {
//        // given - precondition or setup
//        given(userRepository.save(user)).willReturn(user);
//
//        LocalDateTime ldt = LocalDateTime.now();
//
//        Set<UserLikes> userLikesSet = new HashSet<>();
//        UserLikes ul = new UserLikes();
//        ul.setUser(user);
//        ul.setPost(null);
//        userLikesSet.add(ul);
//
//        Set<UserSaves> userSavesSet = new HashSet<>();
//        UserSaves us = new UserSaves();
//        us.setUser(user);
//        us.setPost(null);
//        userSavesSet.add(us);
//
//        Set<UserRole> roles = Set.of(new UserRole(Role.ADMIN));
//
//        user.setEmail("ram@gmail.com");
//        user.setUsername("Ram");
//        user.setUserRoles(roles);
//        user.setProvider(Provider.GOOGLE);
//        user.setDateCreated(ldt);
//        user.setEnabled(true);
//        user.setLocked(true);
//        user.setLikes(userLikesSet);
//        user.setSaves(userSavesSet);
//        // when -  action or the behaviour that we are going test
//        User updatedUser = userService.updateUser(user);
//
//        // then - verify the output
//        assertThat(updatedUser.getEmail()).isEqualTo("ram@gmail.com");
//        assertThat(updatedUser.getUsername()).isEqualTo("Ram");
//        assertThat(updatedUser.getUserRoles()).isEqualTo(roles);
//        assertThat(updatedUser.getProvider()).isEqualTo(Provider.GOOGLE);
//        assertThat(updatedUser.getDateCreated()).isEqualTo(ldt);
//        assertTrue(updatedUser.getEnabled());
//        assertTrue(updatedUser.getLocked());
//        assertThat(updatedUser.getSaves()).isEqualTo(userSavesSet);
//        assertThat(updatedUser.getLikes()).isEqualTo(userLikesSet);
//    }

    @Test
    public void testDeleteUser() {
        willDoNothing().given(userRepository).deleteById(1L);

        // when
        userRepository.deleteById(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }
}