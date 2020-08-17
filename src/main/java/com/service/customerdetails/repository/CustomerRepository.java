package com.service.customerdetails.repository;

import com.service.customerdetails.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/*
 * Repository providing CRUD DB functionalities on Customer table.
 */
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

	/*
	 * query to search customer with his First name and last name.
	 */
    List<Customer> findByFirstNameAndLastNameIgnoreCase(String firstName,String lastName);

}
