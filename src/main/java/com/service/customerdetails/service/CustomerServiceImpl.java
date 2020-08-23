package com.service.customerdetails.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.customerdetails.model.Customer;
import com.service.customerdetails.repository.CustomerRepository;

/*
 * This class implement CustomerService interface  
 */
@Service
public class CustomerServiceImpl implements CustomerService {

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
	public Optional<Customer> findCustomerById(int customerId) {

		Optional<Customer> customer = this.customerRepository.findById(customerId);

		return customer;

	}

	/*
	 * find customer by first name and last name
	 * 
	 * @param first name , last name
	 * 
	 * @return list of all customers
	 */
	public Optional<List<Customer>> findCustomerByFirstNameAndOrLastName(Optional<String> firstName,
			Optional<String> lastName) {

		Optional<List<Customer>> customersList = Optional.ofNullable(null);

		if (firstName.isPresent() && lastName.isPresent()) {

			customersList = this.customerRepository.findByFirstNameAndLastNameIgnoreCase(firstName.get(),
					lastName.get());

		} else if (firstName.isPresent()) {

			customersList = this.customerRepository.findByFirstName(firstName.get());

		} else if (lastName.isPresent()) {

			customersList = this.customerRepository.findByLastName(lastName.get());

		}

		return customersList;
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
