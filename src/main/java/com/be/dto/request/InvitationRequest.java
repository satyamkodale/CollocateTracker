package com.be.dto.request;

import lombok.Data;

@Data
public class InvitationRequest {
    private Long projectId;
    private String email;
}
