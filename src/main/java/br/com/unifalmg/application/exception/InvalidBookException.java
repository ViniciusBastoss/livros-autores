package br.com.unifalmg.application.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidBookException extends RuntimeException {

    public InvalidBookException(String message){super(message);}
}
