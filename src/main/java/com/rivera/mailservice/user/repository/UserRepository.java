package com.rivera.mailservice.user.repository;

import com.rivera.mailservice.user.model.User;
import org.springframework.data.repository.ListCrudRepository;

public interface UserRepository extends ListCrudRepository<User, Long> {}
