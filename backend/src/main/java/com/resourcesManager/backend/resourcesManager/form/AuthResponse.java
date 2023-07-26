package com.resourcesManager.backend.resourcesManager.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder @AllArgsConstructor @NoArgsConstructor

public class AuthResponse {
    private String accessToken;
    private String refreshToken;

}
