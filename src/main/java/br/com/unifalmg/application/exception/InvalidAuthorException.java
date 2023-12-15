package br.com.unifalmg.application.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidAuthorException extends RuntimeException{

    public InvalidAuthorException(String message){super(message);}
}
