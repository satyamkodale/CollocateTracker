package com.be.service;

import com.be.entity.Invitation;
import jakarta.mail.MessagingException;
import org.springframework.mail.MailException;

public interface InvitationService {
    //TODO CHANGE EXCEPTION TYPE

    public void sendInvitation(String email, Long projectId) throws MailException, MessagingException;

    public Invitation acceptInvitation(String token, Long userId) throws Exception;
    public String getTokenByUserMail(String userEmail);
    public void deleteToken(String token);
}
