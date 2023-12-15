package br.com.unifalmg.application.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AuthorNotFoundException extends  RuntimeException {
    public AuthorNotFoundException(String message){ super(message);}
}




