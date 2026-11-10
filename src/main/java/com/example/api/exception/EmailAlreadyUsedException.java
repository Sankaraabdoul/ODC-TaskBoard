package com.example.api.exception;

/**
 * Levée lorsqu'on tente de créer un employé avec un email déjà utilisé.
 * Interceptée par GlobalExceptionHandler pour renvoyer un 409 (Conflict).
 */
public class EmailAlreadyUsedException extends RuntimeException {

    public EmailAlreadyUsedException(String mail) {
        super("L'email '" + mail + "' est déjà utilisé par un autre employé");
    }
}
