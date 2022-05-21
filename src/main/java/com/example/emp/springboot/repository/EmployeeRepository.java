package com.example.emp.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.emp.springboot.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}