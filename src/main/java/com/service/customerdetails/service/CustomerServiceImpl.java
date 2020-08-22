package com.service.customerdetails.service;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.customerdetails.model.Customer;
import com.service.customerdetails.repository.CustomerRepository;

/*
 * This class implement CustomerService interface  
 */
@Service
public class CustomerServiceImpl implements CustomerService {

	private static final Logger LOGGER = LogManager.getLogger(CustomerServiceImpl.class);

	private CustomerRepository customerRepository;

	/*
	 * Constructor auto wiring customeRepository
	 * 
	 * @param customerRepository
	 */
	@Autowired
	public CustomerServiceImpl(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	/*
	 * find all customers
	 * 
	 * @return list of customers
	 */
	public List<Customer> findAll() {
		return this.customerRepository.findAll();
	}

	/*
	 * find customer by id
	 * 
	 * @param id
	 * 
	 * @return Customer
	 */
	public Customer findCustomerById(int customerId) {

		Optional<Customer> customerOptionalObj = this.customerRepository.findById(customerId);

		Customer customer = null;

		if (customerOptionalObj.isPresent()) {
			LOGGER.info("Customer found with id " + customerId);
			customer = customerOptionalObj.get();
		}

		return customer;
	}

	/*
	 * find customer by first name and last name
	 * 
	 * @param first name , last name
	 * 
	 * @return list of all customers
	 */
	public List<Customer> findCustomerByFirstNameAndLastName(String firstName, String lastName) {
		return this.customerRepository.findByFirstNameAndLastNameIgnoreCase(firstName, lastName);
	}

	/*
	 * Save customer
	 * 
	 * @param customer object to save
	 * 
	 * @return customer object saved
	 */
	public Customer save(Customer customer) {

		return this.customerRepository.save(customer);
	}

	/*
	 * delete customer by ID
	 * 
	 * @param id
	 * 
	 * @return string
	 */
	public void deleteCustomerById(int customerId) {
		this.customerRepository.deleteById(customerId);
	}

}
