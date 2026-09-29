package com.shekhar.springboot_webflux.service;

import com.shekhar.springboot_webflux.dto.EmployeeDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EmployeeService {
    Mono<EmployeeDto> saveEmployee(EmployeeDto employeeDto);
    Mono<EmployeeDto> getEmployee(String employeeId);
    Flux<EmployeeDto> getAllEmployees();
    Mono<EmployeeDto> updateEmployee(EmployeeDto employeeDto, String employeeId);
    Mono<Void> deleteEmployee(String employeeId);
}
