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
	 * Find all customer with given ID and return response in JSON format
	 * 
	 * @path variable customerID
	 * 
	 * @return ResponseEntity<Customer>
	 * 
	 */
	@GetMapping(Constants.CUSTOMERS_BY_ID)
	public ResponseEntity<Customer> findCustomerById(@PathVariable @Min(1) int customerId) {

		Optional<Customer> customer = this.customerService.findCustomerById(customerId);

		if (customer.isPresent()) {
			return new ResponseEntity<>(customer.get(), HttpStatus.OK);
		} else {

			throw new CustomerNotFoundException("Customer not found with id " + customerId);
		}

	}

	/*
	 * Find all customers or Customers with first name and or last name
	 * 
	 * @path variables First name ,Last name
	 * 
	 * @return ResponseEntity<Customer>
	 * 
	 */
	@GetMapping(Constants.CUSTOMERS)
	public ResponseEntity<List<Customer>> findCustomers(@RequestParam Optional<String> firstName,
			@RequestParam Optional<String> lastName) {

		Optional<List<Customer>> customersList = this.customerService.findCustomers(firstName, lastName);

		if (customersList.isEmpty()) {

			throw new CustomerNotFoundException("Customers not found");

		} else {

			return new ResponseEntity<>(customersList.get(), HttpStatus.OK);

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
	@PostMapping(Constants.CUSTOMERS)
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
	@PutMapping(Constants.CUSTOMERS)
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

			return new ResponseEntity<>(this.customerService.save(customer), HttpStatus.OK);
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
	@PutMapping(Constants.UPDATEADDRESS)
	public ResponseEntity<Customer> updateCustomerAddress(@PathVariable int customerId, @PathVariable int addressId,
			@Valid @RequestBody Address addressDetails) {

		Optional<Customer> customerOptionalObj = this.customerService.findCustomerById(customerId);

		if (customerOptionalObj.isPresent()) {

			Customer customer = customerOptionalObj.get();

			if (addressId == customer.getAddress().getId()) {

				customer.getAddress().setStreet(addressDetails.getStreet());
				customer.getAddress().setCity(addressDetails.getCity());
				customer.getAddress().setState(addressDetails.getState());
				customer.getAddress().setCountry(addressDetails.getCountry());
				customer.getAddress().setZipCode(addressDetails.getZipCode());

				return new ResponseEntity<>(this.customerService.save(customer), HttpStatus.OK);
			} else {
				throw new CustomerNotFoundException("Customer not found with address id " + addressId);
			}

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
	@DeleteMapping(Constants.CUSTOMERS_BY_ID)
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
