package com.shekhar.springboot_webflux.repo;

import com.shekhar.springboot_webflux.entity.Employee;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface EmployeeRepository extends ReactiveMongoRepository<Employee, String> {
}
