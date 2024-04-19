package com.rivera.mailservice.mail.repository;

import com.rivera.mailservice.mail.model.Email;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailRepository extends ListCrudRepository<Email, Long> {
}
