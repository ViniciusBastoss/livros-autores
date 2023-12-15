package br.com.unifalmg.application.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BookNotFoundException extends RuntimeException{

    public BookNotFoundException(String message){ super(message);}
}
