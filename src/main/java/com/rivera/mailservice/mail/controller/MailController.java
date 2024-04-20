package com.rivera.mailservice.mail.controller;

import com.rivera.mailservice.mail.model.Email;
import com.rivera.mailservice.mail.service.MailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mail-management")
public class MailController {

    private final MailService mailService;

    @Operation(summary = "Request all emails sent", description = "Returns all emails sent")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned"),
    })
    @GetMapping("/emails")
    public List<Email> getAllEmails() {
        return mailService.getEmails();
    }
}
