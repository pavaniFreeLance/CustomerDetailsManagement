package com.service.customerdetails.rest;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.service.customerdetails.dto.AddressDto;
import com.service.customerdetails.dto.CustomerDto;
import com.service.customerdetails.model.Customer;
import com.service.customerdetails.service.CustomerService;

/*
 *Customer Rest Controller with end points to 
 *Add Customer,
 *Update Customer,
 *Update Customer address
 *Search Customer by id,
 *Search Customers
 *Delete customer  
 * @throws CustomerNotFoundException
 */
@RestController
@Validated
@RequestMapping(value = "/api", produces = "application/json")
public class CustomerRestController {

	private CustomerService customerService;

	private ModelMapper modelMapper;

	/*
	 * Auto wiring CustomerService 
	 * Auto wiring ModelMapper
	 * 
	 * @param customerService,modelMapper
	 */
	@Autowired
	public CustomerRestController(CustomerService customerService,ModelMapper modelMapper) {

		this.customerService = customerService;
		this.modelMapper = modelMapper;

	}

	/*
	 * Find all customer with given ID and return response in JSON format
	 * 
	 * @path variable customerID
	 * 
	 * @return ResponseEntity<Customer>
	 * 
	 */
	@GetMapping(ApplicationConstants.CUSTOMERS_BY_ID_URI)
	public ResponseEntity<CustomerDto> findCustomerById(@PathVariable @Min(1) int customerId) {

		Optional<Customer> customer = this.customerService.findCustomerById(customerId);

		if (customer.isPresent()) {	

			return new ResponseEntity<>(convertEntityToDto(customer.get()), HttpStatus.OK);
		} else {

			throw new CustomerNotFoundException("Customer not found with id " + customerId);
		}

	}

	/*
	 * Find all customers or Customers with first name and or last name
	 * 
	 * @path variables First name ,Last name
	 * 
	 * @return ResponseEntity<CustomerDTO>
	 * 
	 */
	@GetMapping(ApplicationConstants.CUSTOMERS_URI)
	public ResponseEntity<List<CustomerDto>> findCustomers(@RequestParam Optional<String> firstName,
			@RequestParam Optional<String> lastName) {

		final Optional<List<Customer>> customersList = this.customerService.findCustomers(firstName, lastName);

		if (customersList.isPresent()) {

			return new ResponseEntity<>(customersList.get().stream().map(this::convertEntityToDto).collect(Collectors.toList()), HttpStatus.OK);

		} else {

			throw new CustomerNotFoundException("Customers not found");

		}

	}

	/*
	 * Adding new customer
	 * 
	 * @Request body with CustomerDto
	 * 
	 * @return ResponseEntity<CustomerDto>
	 * 
	 */
	@PostMapping(ApplicationConstants.CUSTOMERS_URI)
	public ResponseEntity<CustomerDto> addCustomer(@Valid @RequestBody CustomerDto customerDto) {

		customerDto.setId(0);
		customerDto.getAddressDto().setId(0);
		Customer customer = this.customerService.saveCustomer(convertDtoToEntity(customerDto));
		return (Objects.nonNull(customer)) ? new ResponseEntity<>(convertEntityToDto(customer), HttpStatus.CREATED) : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

	/*
	 * Updating existing customer
	 * 
	 * @Request body with CustomerDto 
	 * 
	 * @return ResponseEntity<CustomerDto>
	 * 
	 */
	@PutMapping(ApplicationConstants.CUSTOMERS_URI)
	public ResponseEntity<CustomerDto> updateCustomer(@Valid @RequestBody CustomerDto customerDto) {

		Optional<Customer> savedCustomer = this.customerService.findCustomerById(customerDto.getId());

		if (savedCustomer.isPresent()) {

			Customer customer = convertDtoToEntity(customerDto);
			customer.getAddress().setId(savedCustomer.get().getAddress().getId());

			return new ResponseEntity<>(convertEntityToDto(this.customerService.saveCustomer(customer)), HttpStatus.OK);
		} else {
			throw new CustomerNotFoundException("Customer not available to update");
		}
	}

	/*
	 * Updating existing customers living Address
	 * 
	 * @Request body with AddressDto
	 * @Path variable customerID,AddressID
	 * 
	 * @return ResponseEntity<CustomerDto>	 * 
	 */
	@PutMapping(ApplicationConstants.CUSTOMER_ADDRESS_URI)
	public ResponseEntity<CustomerDto> updateCustomerAddress(@PathVariable @Min(1) int customerId, @PathVariable @Min(1) int addressId,
			@Valid @RequestBody AddressDto addressDtoDetails) {

		Optional<Customer> customer = this.customerService.findCustomerById(customerId);

		if (customer.isPresent()) {

			if (addressId == customer.get().getAddress().getId()) {

				customer.get().getAddress().setStreet(addressDtoDetails.getStreet());
				customer.get().getAddress().setCity(addressDtoDetails.getCity());
				customer.get().getAddress().setState(addressDtoDetails.getState());
				customer.get().getAddress().setCountry(addressDtoDetails.getCountry());
				customer.get().getAddress().setZipCode(addressDtoDetails.getZipCode());

				return new ResponseEntity<>(convertEntityToDto(this.customerService.saveCustomer(customer.get())), HttpStatus.OK);
			} 
		}
		throw new CustomerNotFoundException("Customer not available to update");

	}

	/*
	 * deleting existing customer
	 * 
	 * @path variable customerID
	 * 
	 * @return String
	 * 
	 */	
	@DeleteMapping(ApplicationConstants.CUSTOMERS_BY_ID_URI)
	public String deleteCustomer(@PathVariable @Min(1) int customerId) {

		if ((this.customerService.findCustomerById(customerId)).isPresent()) {

			this.customerService.deleteCustomerById(customerId);

			return "customer deleted";

		} else {
			throw new CustomerNotFoundException("Customer not available to delete");
		}

	}

	/*
	 * Converting of Entity Object to DTO object
	 * @param Customer
	 * @return CustomerDto
	 */
	private CustomerDto convertEntityToDto(Customer customer) {

		return modelMapper.map(customer, CustomerDto.class);

	}

	/*
	 * Converting of DTO Object to Entity object
	 * @param Customer
	 * @return CustomerDto
	 */
	private Customer convertDtoToEntity(CustomerDto customerDto) {

		return modelMapper.map(customerDto, Customer.class);

	}
}
