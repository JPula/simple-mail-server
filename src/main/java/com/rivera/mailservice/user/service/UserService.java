package com.rivera.mailservice.user.service;

import com.rivera.mailservice.user.exception.UserNotFoundException;
import com.rivera.mailservice.user.model.User;
import com.rivera.mailservice.user.model.UserUpdateRequestDto;
import com.rivera.mailservice.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public List<User> getUsers(List<Long> ids) {
        return userRepository.findAllById(ids);
    }

    public User getUser(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(UserNotFoundException::new);
    }

    public User createUser(@Valid User user) {
            log.debug("[START] Create user: " + user);
            User savedUser = userRepository.save(user);
            log.debug("[END] Created user: " + user);

            return savedUser;
    }

    public User updateUser(Long userId, @Valid UserUpdateRequestDto updateRequestDto) {
        return updateUser(updateRequestDto.updateUser(getUser(userId)));
    }

    public User updateUser(@Valid User user) {
        log.debug("[START] Update user: " + user);
        User updatedUser = userRepository.save(user);
        log.debug("[END] Update user: " + user);

        return updatedUser;
    }

    public void softDelete(Long id) {
        softDelete(List.of(id));
    }

    @Transactional
    public void softDelete(List<Long> ids) {
        log.debug("[START] Delete users: " + ids);
        List<User> users = getUsers(ids);
        users.forEach(u -> u.setDeleted(true));
        userRepository.saveAll(users);
        log.debug("[END] Deleted users:" + ids);
    }
}
