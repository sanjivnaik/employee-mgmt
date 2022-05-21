package com.example.emp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.emp.springboot.entity.Employee;
import com.example.emp.springboot.repository.EmployeeRepository;

import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("/employees")
    public List<Employee> fetchEmployees(){
    	sleep();
        return employeeRepository.findAll();
    }

	private void sleep() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
    /**
     * Create new employee
     *
     * @param employee
     * @return ResponseEntity
     */
    @PostMapping("/employees")
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
    	sleep();
        Employee newEmployee = employeeRepository
                .save(Employee.builder()
                        .firstName(employee.getFirstName())
                        .lastName(employee.getLastName())
                        .email(employee.getEmail())
                        .build());
        return new ResponseEntity<>(newEmployee, HttpStatus.OK);
    }
    
    @PutMapping("/employees/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable("id") long id, @RequestBody Employee employee) {
    	sleep();
        //check if employee exist in database
        Employee empObj = getEmpRec(id);

        if (empObj != null) {
            empObj.setFirstName(employee.getFirstName());
            empObj.setLastName(employee.getLastName());
            empObj.setEmail(employee.getEmail());
            return new ResponseEntity<>(employeeRepository.save(empObj), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    @DeleteMapping("/employees/{id}")
    public ResponseEntity<HttpStatus> deleteEmployeeById(@PathVariable("id") long id) {
    	sleep();
        try {
            //check if employee exist in database
            Employee emp = getEmpRec(id);

            if (emp != null) {
                employeeRepository.deleteById(id);
                return new ResponseEntity<>(HttpStatus.OK);
            }

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Method to get the employee record by id
     *
     * @param id
     * @return Employee
     */
    private Employee getEmpRec(long id) {
        Optional<Employee> empObj = employeeRepository.findById(id);

        if (empObj.isPresent()) {
            return empObj.get();
        }
        return null;
    }
}