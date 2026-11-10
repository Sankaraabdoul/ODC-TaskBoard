package com.example.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.api.dto.EmployeePatchDTO;
import com.example.api.dto.EmployeeRequestDTO;
import com.example.api.dto.EmployeeResponseDTO;
import com.example.api.dto.EmployeeUpdateDTO;
import com.example.api.mapper.EmployeeMapper;
import com.example.api.model.Employee;
import com.example.api.service.EmployeeService;

import jakarta.validation.Valid;

@RestController
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * Create - inscription d'un nouvel employé. Route publique (pas d'auth requise).
     */
    @PostMapping("/employee")
    public ResponseEntity<EmployeeResponseDTO> createEmployee(@Valid @RequestBody EmployeeRequestDTO dto) {
        Employee employee = EmployeeMapper.toEntity(dto);
        Employee saved = employeeService.saveEmployee(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(EmployeeMapper.toResponseDTO(saved));
    }

    /**
     * Récupérer tous les employés.
     */
    @GetMapping("/employees")
    public List<EmployeeResponseDTO> getEmployees() {
        List<EmployeeResponseDTO> result = new java.util.ArrayList<>();
        employeeService.getEmployees().forEach(e -> result.add(EmployeeMapper.toResponseDTO(e)));
        return result;
    }

    /**
     * Récupérer un employé par id. Renvoie 404 si absent (géré par GlobalExceptionHandler).
     */
    @GetMapping("/employee/{id}")
    public EmployeeResponseDTO getEmployee(@PathVariable("id") final Long id) {
        return EmployeeMapper.toResponseDTO(employeeService.getEmployee(id));
    }

    /**
     * Suppression d'un employé à partir de son id. 204 si succès, 404 si absent.
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/employee/{id}")
    public ResponseEntity<Void> deleteEmployeeById(@PathVariable("id") final Long id) {
        employeeService.deleteEmployeeById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Suppression de tous les employés.
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/employees")
    public ResponseEntity<Void> deleteEmployees() {
        employeeService.deleteEmployees();
        return ResponseEntity.noContent().build();
    }

    /**
     * Mise à jour complète d'un employé. 404 si absent, 400 si validation échoue.
     */
    @PutMapping("/employee/{id}")
    public EmployeeResponseDTO updateEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeUpdateDTO dto) {
        return EmployeeMapper.toResponseDTO(employeeService.updateEmployee(id, dto));
    }

    /**
     * Mise à jour partielle d'un employé (seuls les champs fournis sont modifiés).
     */
    @PatchMapping("/employee/{id}")
    public EmployeeResponseDTO partialUpdate(@PathVariable Long id, @RequestBody EmployeePatchDTO dto) {
        return EmployeeMapper.toResponseDTO(employeeService.partialUpdate(id, dto));
    }
}
