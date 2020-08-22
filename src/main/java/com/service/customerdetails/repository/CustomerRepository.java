package com.service.customerdetails.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.service.customerdetails.model.Customer;

/*
 * Repository extending JPA repository
 * providing CRUD functionalities on Customer table.
 */
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

	/*
	 * query to search customer with his First name and last name.
	 */
	List<Customer> findByFirstNameAndLastNameIgnoreCase(String firstName, String lastName);

}
