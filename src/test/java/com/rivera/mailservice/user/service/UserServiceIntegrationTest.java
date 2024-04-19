package com.rivera.mailservice.user.service;

import com.rivera.mailservice.user.model.User;
import com.rivera.mailservice.user.model.UserUpdateRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Test
    void given3Users_whenGetAllUsers_thenReturn3Users() {
        List<User> users = userService.getUsers();
        assertEquals(3, users.size());
    }

    @Test
    void givenUserId_thenReturnCorrectUserInfo() throws Exception {
        User actual = userService.getUser(1L);

        assertEquals(1L, actual.getId());
        assertEquals("betty.boop", actual.getUserName());
        assertEquals("betty.boop@email.com", actual.getEmailAddress());
        assertFalse(actual.isDeleted());
    }


    @Test
    void givenUserIdToDelete_whenDeleteUser_thenSoftDeleteUser() throws Exception {
        userService.softDelete(1L);

        User deletedUser = userService.getUser(1L);

        assertTrue(deletedUser.isDeleted());
    }

    @Test
    void givenUserIdToDelete_whenDeleteUser_thenOtherUsersShouldNotBeDeleted() throws Exception {
        userService.softDelete(1L);

        List<User> otherUsers = userService.getUsers(List.of(2L, 3L));

        boolean areAnyOtherUsersDeleted = otherUsers
                .stream().anyMatch(User::isDeleted);

        assertFalse(areAnyOtherUsersDeleted);
    }

    @Test
    void givenUserIdsToDelete_whenDeleteUsers_thenSoftDeleteUsers() {
        List<Long> userIdsToDelete = List.of(1L, 2L);

        userService.softDelete(userIdsToDelete);
        List<User> deletedUsers = userService.getUsers(userIdsToDelete);

        assertEquals(deletedUsers.size(), 2);

        boolean areAllUsersDeleted = deletedUsers
                .stream().allMatch(User::isDeleted);

        assertTrue(areAllUsersDeleted);
    }

    @Test
    void givenUsersToDelete_whenDeleteUsers_thenOtherUsersShouldNotBeDeleted() throws Exception {
        List<Long> userIdsToDelete = List.of(1L, 2L);

        userService.softDelete(userIdsToDelete);

        User otherUser = userService.getUser(3L);
        assertFalse(otherUser.isDeleted());
    }

    @Test
    void givenUser_whenUpdateUser_thenUserInfoShouldBeUpdated() throws Exception {
        String newEmailAddress = "betty.smith@email.com";
        User user = userService.getUser(1L);

        user.setEmailAddress(newEmailAddress);

        User updateUser = userService.updateUser(user);

        assertEquals("betty.boop", updateUser.getUserName());
        assertEquals(newEmailAddress, updateUser.getEmailAddress());
        assertFalse(updateUser.isDeleted());
    }

    @Test
    void givenUser_whenUpdateUserWithUserUpdateRequestDto_thenUserInfoShouldBeUpdated() throws Exception {
        UserUpdateRequestDto updateRequestDto = new UserUpdateRequestDto();
        updateRequestDto.setLastName("Smith");
        updateRequestDto.setEmailAddress("betty.smith@email.com");

        User updateUser = userService.updateUser(1L, updateRequestDto);

        assertEquals(1L, updateUser.getId());
        assertEquals("betty.boop", updateUser.getUserName());
        assertEquals("Betty", updateUser.getFirstName());
        assertEquals("Smith", updateUser.getLastName());
        assertEquals("betty.smith@email.com", updateUser.getEmailAddress());
        assertFalse(updateUser.isDeleted());
    }

    @Test
    void givenUser_whenUpdateUser_thenOtherUsersInfoShouldNotBeUpdated() throws Exception {
        String newEmailAddress = "betty.smith@email.com";
        User user = userService.getUser(1L);

        user.setEmailAddress(newEmailAddress);

        User updateUser = userService.updateUser(user);

        assertEquals(newEmailAddress, updateUser.getEmailAddress());
    }
}