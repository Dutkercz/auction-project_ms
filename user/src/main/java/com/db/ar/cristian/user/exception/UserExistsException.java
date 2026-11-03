package com.db.ar.cristian.user.exception;

public class UserExistsException extends RuntimeException {
    public UserExistsException(String message){
        super(message);
    }
}
