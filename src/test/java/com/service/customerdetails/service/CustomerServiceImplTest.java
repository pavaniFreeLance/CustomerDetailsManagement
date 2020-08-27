package com.service.customerdetails.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.service.customerdetails.model.Address;
import com.service.customerdetails.model.Customer;
import com.service.customerdetails.repository.CustomerRepository;

/*
 * Test class for CustomerServiceImpl
 */
@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceImplTest {

	@Mock
	private CustomerRepository customerRepository;

	private CustomerService customerService;

	private Customer customer;

	private Address address;

	/*
	 * Setting values before Tests
	 */
	@Before
	public void setUp() {

		customerService = new CustomerServiceImpl(customerRepository);
		// Creating Cutomer Object
		address = new Address(1, "irene", "vel", "brabant", "3241", "NL");
		customer = new Customer(1, "aaa", "bbb", 30, address);
	}

	/*
	 * Test link for findCustomerById() of CustomerService
	 */
	@Test
	public void findCustomerByIdTest_customerPresent() {
		// setup
		int id = 1;
		Mockito.when(customerRepository.findById(1)).thenReturn(Optional.of(customer));

		// when
		Optional<Customer> resultObj = customerService.findCustomerById(id);

		// Verify
		verify(customerRepository, times(1)).findById(id);
		assertTrue(resultObj.isPresent());
		assertEquals(id, resultObj.get().getId());
	}

	/*
	 * Test link for findCustomerById() of CustomerService Customer not found with
	 * the provided ID
	 */
	@Test
	public void findCustomerByIdTest_customerNotPresent() {
		// setup
		int id = 100;

		// when
		Optional<Customer> resultObj = customerService.findCustomerById(id);

		verify(customerRepository, times(1)).findById(id);
		assertTrue(resultObj.isEmpty());
	}

	/*
	 * Test link for findByFirstNameAndLastNameIgnoreCase() of CustomerService First
	 * name and last name values present
	 */
	@Test
	public void findCustomersTest_CustomerPresent() {
		// setup
		List<Customer> customerList = new ArrayList<Customer>();
		customerList.add(customer);
		Mockito.when(customerRepository.findByFirstNameAndLastNameIgnoreCase(customer.getFirstName(),
				customer.getLastName())).thenReturn(Optional.of(customerList));

		// when
		Optional<List<Customer>> resultCustomerList = customerService.findCustomers(
				Optional.of(customer.getFirstName()), Optional.of(customer.getLastName()));

		// verify
		verify(customerRepository, times(1)).findByFirstNameAndLastNameIgnoreCase(customer.getFirstName(),
				customer.getLastName());
		assertTrue(resultCustomerList.isPresent());
		assertEquals(1, resultCustomerList.get().size());

	}

	/*
	 * Test link for findByFirstNameAndLastNameIgnoreCase() of CustomerService First
	 * name value is present and last name value is not.
	 */
	@Test
	public void findCustomersTest_OnlyFirstNamePresent() {
		// setup
		List<Customer> customerList = new ArrayList<Customer>();
		customerList.add(customer);
		Mockito.when(customerRepository.findByFirstName(customer.getFirstName())).thenReturn(Optional.of(customerList));

		// when
		Optional<List<Customer>> resultCustomerList = customerService
				.findCustomers(Optional.of(customer.getFirstName()), Optional.ofNullable(null));

		// verify
		verify(customerRepository, times(0)).findByFirstNameAndLastNameIgnoreCase(any(String.class), any(String.class));
		verify(customerRepository, times(1)).findByFirstName(customer.getFirstName());

		assertTrue(resultCustomerList.isPresent());
		assertEquals(1, resultCustomerList.get().size());

	}

	/*
	 * Test link for findCustomers of CustomerService last
	 * name value is present and first name value is not.
	 */
	@Test
	public void findCustomersTest_OnlyLastNamePresent() {
		// setup
		List<Customer> customerList = new ArrayList<Customer>();
		customerList.add(customer);
		Mockito.when(customerRepository.findByLastName(customer.getLastName())).thenReturn(Optional.of(customerList));

		// when
		Optional<List<Customer>> resultCustomerList = customerService
				.findCustomers(Optional.ofNullable(null), Optional.of(customer.getLastName()));

		// verify
		verify(customerRepository, times(0)).findByFirstNameAndLastNameIgnoreCase(any(String.class), any(String.class));
		verify(customerRepository, times(0)).findByFirstName(any(String.class));
		verify(customerRepository, times(1)).findByLastName(customer.getLastName());

		assertTrue(resultCustomerList.isPresent());
		assertEquals(1, resultCustomerList.get().size());

	}

	/*
	 * Test link for findByFirstNameAndLastNameIgnoreCase() of CustomerService Both
	 * First and last name values are not present and Null is returned.
	 */
	@Test
	public void findCustomersTest_BothFirstAndLastNameNotPresent() {

		//set up
		List<Customer> customerList = new ArrayList<Customer>();
		customerList.add(customer);
		Mockito.when(customerRepository.findAll()).thenReturn(customerList);
		
		// perform
		Optional<List<Customer>> resultCustomerList = customerService
				.findCustomers(Optional.ofNullable(null), Optional.ofNullable(null));

		// verify
		verify(customerRepository, times(0)).findByFirstNameAndLastNameIgnoreCase(any(String.class), any(String.class));
		verify(customerRepository, times(0)).findByFirstName(any(String.class));
		verify(customerRepository, times(0)).findByLastName(any(String.class));
		verify(customerRepository, times(1)).findAll();

		assertTrue(resultCustomerList.isPresent());
		assertEquals(1, resultCustomerList.get().size());
		

	}

	/*
	 * Test link for Save() of CustomerService
	 */
	@Test
	public void saveTest() {
		// set up
		Address addressToSave = new Address(0, "irene", "vel", "brabant", "3241", "NL");
		Customer customerToSave = new Customer(0, "aaa", "bbb", 30, addressToSave);

		Mockito.when(customerRepository.save(customerToSave)).thenReturn(customer);

		// when
		Customer resultObj = customerService.saveCustomer(customerToSave);

		// verify results
		verify(customerRepository, times(1)).save(customerToSave);
		assertEquals(1, resultObj.getId());

	}

	/*
	 * Test link for deleteCustomerById() of CustomerService
	 */
	@Test
	public void deleteCustomerByIdTest() {

		// when
		customerService.deleteCustomerById(customer.getId());

		// verify results
		verify(customerRepository, times(1)).deleteById(customer.getId());

	}

}
