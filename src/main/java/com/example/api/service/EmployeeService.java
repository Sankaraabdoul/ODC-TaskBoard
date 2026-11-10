package com.example.api.service;

import com.example.api.dto.EmployeePatchDTO;
import com.example.api.dto.EmployeeUpdateDTO;
import com.example.api.exception.EmailAlreadyUsedException;
import com.example.api.exception.EmployeeNotFoundException;
import com.example.api.model.Employee;
import com.example.api.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Employee getEmployee(final Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> EmployeeNotFoundException.forId(id));
    }

    public Iterable<Employee> getEmployees() {
        return employeeRepository.findAll();
    }

    public void deleteEmployeeById(final Long id) {
        if (!employeeRepository.existsById(id)) {
            throw EmployeeNotFoundException.forId(id);
        }
        employeeRepository.deleteById(id);
    }

    public void deleteEmployees() {
        employeeRepository.deleteAll();
    }

    /**
     * Création d'un employé : vérifie l'unicité de l'email et hache le mot de passe
     * avant persistance (jamais de mot de passe en clair en base).
     */
    public Employee saveEmployee(Employee employee) {
        if (employeeRepository.existsByMail(employee.getMail())) {
            throw new EmailAlreadyUsedException(employee.getMail());
        }
        if (employee.getRole() == null || employee.getRole().isBlank()) {
            employee.setRole("ROLE_EMPLOYEE");
        }
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        return employeeRepository.save(employee);
    }

    /**
     * Mise à jour complète (PUT). Le mot de passe n'est modifié que s'il est fourni ;
     * sinon le hash existant est conservé.
     */
    public Employee updateEmployee(final Long id, EmployeeUpdateDTO dto) {
        Employee employee = getEmployee(id);
        if (!employee.getMail().equalsIgnoreCase(dto.getMail()) && employeeRepository.existsByMail(dto.getMail())) {
            throw new EmailAlreadyUsedException(dto.getMail());
        }
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setMail(dto.getMail());
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            employee.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        if (dto.getRole() != null && !dto.getRole().isBlank()) {
            employee.setRole(dto.getRole());
        }
        return employeeRepository.save(employee);
    }

    /**
     * Mise à jour partielle (PATCH). Seuls les champs non-nuls du DTO sont appliqués.
     */
    public Employee partialUpdate(final Long id, EmployeePatchDTO dto) {
        Employee employee = getEmployee(id);

        if (dto.getFirstName() != null) {
            employee.setFirstName(dto.getFirstName());
        }
        if (dto.getLastName() != null) {
            employee.setLastName(dto.getLastName());
        }
        if (dto.getMail() != null) {
            if (!employee.getMail().equalsIgnoreCase(dto.getMail()) && employeeRepository.existsByMail(dto.getMail())) {
                throw new EmailAlreadyUsedException(dto.getMail());
            }
            employee.setMail(dto.getMail());
        }
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            employee.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        if (dto.getRole() != null && !dto.getRole().isBlank()) {
            employee.setRole(dto.getRole());
        }

        return employeeRepository.save(employee);
    }
}
