package com.example.api.exception;

/**
 * Levée lorsqu'un employé demandé (par id ou par mail) n'existe pas en base.
 * Interceptée par GlobalExceptionHandler pour renvoyer un 404.
 */
public class EmployeeNotFoundException extends RuntimeException {

    public EmployeeNotFoundException(String message) {
        super(message);
    }

    public static EmployeeNotFoundException forId(Long id) {
        return new EmployeeNotFoundException("Aucun employé trouvé avec l'id " + id);
    }
}
