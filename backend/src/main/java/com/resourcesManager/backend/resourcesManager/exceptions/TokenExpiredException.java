package com.resourcesManager.backend.resourcesManager.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class TokenExpiredException extends RuntimeException{
    public TokenExpiredException(String message){
        super(message);
    }

}
