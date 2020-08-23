package com.service.customerdetails.rest;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.service.customerdetails.model.Address;
import com.service.customerdetails.model.Customer;
import com.service.customerdetails.service.CustomerService;

/*
 * Rest Controller with end points to add,update,search,delete customers  
 * @throws CustomerNotFoundException
 */
@RestController
@RequestMapping(value = "/api", produces = "application/json")
public class CustomerRestController {

	private CustomerService customerService;

	private static final String CUSTOMERS = "/customers";

	private static final String CUSTOMERS_BY_ID = "/customers/{customerId}";

	/*
	 * Auto wiring CustomerService
	 * 
	 * @param customerService
	 */
	@Autowired
	public CustomerRestController(CustomerService customerService) {

		this.customerService = customerService;
	}

	/*
	 * Find all customers in the DB and return list in JSON format
	 */
	@GetMapping(CUSTOMERS)
	public ResponseEntity<List<Customer>> findAllCustomers() {

		return new ResponseEntity<>(this.customerService.findAll(), HttpStatus.OK);
	}

	/*
	 * Find all customer with given ID and return response in JSON format
	 * 
	 * @path variable customerID
	 * 
	 * @return ResponseEntity<Customer>
	 * 
	 */
	@GetMapping(CUSTOMERS_BY_ID)
	public ResponseEntity<Customer> findCustomerById(@PathVariable @Min(1) int customerId) {

		Optional<Customer> customer = this.customerService.findCustomerById(customerId);

		if (customer.isPresent()) {
			return new ResponseEntity<>(customer.get(), HttpStatus.OK);
		} else {

			throw new CustomerNotFoundException("Customer not found with id " + customerId);
		}

	}

	/*
	 * Find all customer with first name and or last name
	 * 
	 * @path variables First name ,Last name
	 * 
	 * @return ResponseEntity<Customer>
	 * 
	 */
	@GetMapping("/searchbyname")
	public ResponseEntity<List<Customer>> findCustomerByFirstNameAndOrLastName(@RequestParam Optional<String> firstName,
			@RequestParam Optional<String> lastName) {

		Optional<List<Customer>> customersList = this.customerService.findCustomerByFirstNameAndOrLastName(firstName,
				lastName);

		if (customersList.isPresent()) {

			return new ResponseEntity<>(customersList.get(), HttpStatus.OK);
		} else {
			throw new CustomerNotFoundException(
					"Customer not found with given first and last name " + firstName + " " + lastName);
		}

	}

	/*
	 * Adding new customer
	 * 
	 * @Request body with customer details
	 * 
	 * @return ResponseEntity<Customer>
	 * 
	 */
	@PostMapping(CUSTOMERS)
	public ResponseEntity<Customer> addCustomer(@Valid @RequestBody Customer customer) {

		customer.setId(0);

		customer.getAddress().setId(0);

		return new ResponseEntity<>(this.customerService.save(customer), HttpStatus.CREATED);
	}

	/*
	 * Updating existing customer
	 * 
	 * @Request body with customer details
	 * 
	 * @return ResponseEntity<Customer>
	 * 
	 */
	@PutMapping(CUSTOMERS)
	public ResponseEntity<Customer> updateCustomer(@Valid @RequestBody Customer customerDetails) {

		Optional<Customer> customerOptionalObj = this.customerService.findCustomerById(customerDetails.getId());

		if (customerOptionalObj.isPresent()) {

			Customer customer = customerOptionalObj.get();

			customer.setFirstName(customerDetails.getFirstName());
			customer.setLastName(customerDetails.getLastName());
			customer.setAge(customerDetails.getAge());
			customer.getAddress().setStreet(customerDetails.getAddress().getStreet());
			customer.getAddress().setCity(customerDetails.getAddress().getCity());
			customer.getAddress().setState(customerDetails.getAddress().getState());
			customer.getAddress().setCountry(customerDetails.getAddress().getCountry());
			customer.getAddress().setZipCode(customerDetails.getAddress().getZipCode());

			return new ResponseEntity<>(this.customerService.save(customer), HttpStatus.CREATED);
		} else {
			throw new CustomerNotFoundException("Customer not found with id " + customerDetails.getId());
		}
	}

	/*
	 * Updating existing customers living Address
	 * 
	 * @Request body with customer details
	 * 
	 * @return ResponseEntity<Customer>
	 * 
	 */
	@PutMapping("/customers/{customerId}/address")
	public ResponseEntity<Customer> updateCustomerAddress(@PathVariable int customerId,
			@Valid @RequestBody Address addressDetails) {

		Optional<Customer> customerOptionalObj = this.customerService.findCustomerById(customerId);

		if (customerOptionalObj.isPresent()) {

			Customer customer = customerOptionalObj.get();

			customer.getAddress().setStreet(addressDetails.getStreet());
			customer.getAddress().setCity(addressDetails.getCity());
			customer.getAddress().setState(addressDetails.getState());
			customer.getAddress().setCountry(addressDetails.getCountry());
			customer.getAddress().setZipCode(addressDetails.getZipCode());

			return new ResponseEntity<>(this.customerService.save(customer), HttpStatus.CREATED);

		} else {
			throw new CustomerNotFoundException("Customer not found with id " + customerId);
		}
	}

	/*
	 * deleting existing customer
	 * 
	 * @path variable customerID
	 * 
	 * @return String
	 * 
	 */
	@DeleteMapping(CUSTOMERS_BY_ID)
	public String deleteCustomer(@PathVariable int customerId) {

		Optional<Customer> customer = this.customerService.findCustomerById(customerId);

		if (customer.isPresent()) {

			this.customerService.deleteCustomerById(customerId);

			return "customer with id : " + customerId + " deleted";

		} else {
			throw new CustomerNotFoundException("Customer not found with id " + customerId);
		}

	}

}
