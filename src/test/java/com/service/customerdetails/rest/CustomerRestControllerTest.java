package com.service.customerdetails.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.service.customerdetails.model.Address;
import com.service.customerdetails.model.Customer;
import com.service.customerdetails.service.CustomerService;

/*
 * Test class for CustomerRestController
 */
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = CustomerRestController.class)
public class CustomerRestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CustomerService customerService;	

	private static final String CUSTOMERSURI = "/api/customers";

	private static final String CUSTOMERSBYIDURI = "/api/customers/1";

	private static final String USER = "USER";

	private static final String ADMIN = "ADMIN";
	
	private List<Customer> customerList;

	private Customer customer;

	private Gson gson;

	private String firstName;

	private String lastName;

	@Before
	public void setUp() {
		customer = getCustomer(1);
		customerList = Arrays.asList(customer);
		gson = new Gson();

		firstName = "testfirstname";

		lastName = "testlastname";
	}

	/*
	 * Unit testing findAll method
	 * 
	 */
	@WithMockUser(USER)
	@Test
	public void testFindAllCustomersResultReturned() throws Exception {
		// set up
		Mockito.when(customerService.findAll()).thenReturn(customerList);
		RequestBuilder request = MockMvcRequestBuilders.get(CUSTOMERSURI).accept(MediaType.APPLICATION_JSON);

		// when
		MvcResult mvcResult = this.mockMvc.perform(request).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(MockMvcResultMatchers.jsonPath("$").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty()).andReturn();

		String resultStr = mvcResult.getResponse().getContentAsString();

		Type listOfMyClassObject = new TypeToken<List<Customer>>() {
		}.getType();

		List<Customer> outputList = gson.fromJson(resultStr, listOfMyClassObject);

		// verify
		assertEquals(customerList.size(), outputList.size());

		int i = 0;
		for (Customer resultCustomer : outputList) {
			assertEquals(customerList.get(i).getId(), resultCustomer.getId());
			assertEquals(customerList.get(i).getFirstName(), resultCustomer.getFirstName());
			assertEquals(customerList.get(i).getLastName(), resultCustomer.getLastName());
			assertEquals(customerList.get(i).getAddress().getId(), resultCustomer.getAddress().getId());
			assertEquals(customerList.get(i).getAddress().getCity(), resultCustomer.getAddress().getCity());
			assertEquals(customerList.get(i).getAddress().getStreet(), resultCustomer.getAddress().getStreet());
			assertEquals(customerList.get(i).getAddress().getState(), resultCustomer.getAddress().getState());
			i++;
		}

	}

	/*
	 * Test findAll method. When empty list is the result, asserting response sent
	 */
	@WithMockUser(USER)
	@Test
	public void testFindAllCustomersResultEmptyList() throws Exception {
		// set up
		List<Customer> emptyList = new ArrayList<Customer>();
		Mockito.when(customerService.findAll()).thenReturn(emptyList);

		RequestBuilder request = MockMvcRequestBuilders.get(CUSTOMERSURI).accept(MediaType.APPLICATION_JSON);

		// when
		this.mockMvc.perform(request).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(MockMvcResultMatchers.jsonPath("$").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty()).andReturn();

	}

	/*
	 * Test findCustomerByID when customer found with given ID
	 */
	@WithMockUser(USER)
	@Test
	public void testFindCustomerByIdReturnResult() throws Exception {
		// Setup
		Mockito.when(customerService.findCustomerById(customer.getId())).thenReturn(customer);

		RequestBuilder request = MockMvcRequestBuilders.get(CUSTOMERSBYIDURI).accept(MediaType.APPLICATION_JSON);

		// action
		MvcResult mvcResult = this.mockMvc.perform(request).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(MockMvcResultMatchers.jsonPath("$").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty()).andReturn();

		// Verify
		String resultStr = mvcResult.getResponse().getContentAsString();
		Customer resultObj = gson.fromJson(resultStr, Customer.class);

		assertEquals(customer.getId(), resultObj.getId());
		assertEquals(customer.getFirstName(), resultObj.getFirstName());
		assertEquals(customer.getLastName(), resultObj.getLastName());
		assertEquals(customer.getAddress().getId(), resultObj.getAddress().getId());
		assertEquals(customer.getAddress().getCity(), resultObj.getAddress().getCity());
		assertEquals(customer.getAddress().getStreet(), resultObj.getAddress().getStreet());
		assertEquals(customer.getAddress().getState(), resultObj.getAddress().getState());

	}

	/*
	 * Test findCustomerByID with no Customer found with given ID
	 */
	@WithMockUser(USER)
	@Test
	public void testFindCustomerByIdReturnNull() throws Exception {
		// Setup
		Mockito.when(customerService.findCustomerById(customer.getId())).thenReturn(null);

		RequestBuilder request = MockMvcRequestBuilders.get("/api/customers/100").accept(MediaType.APPLICATION_JSON);

		// action
		this.mockMvc.perform(request).andExpect(status().isNotFound()).andReturn();

	}

	/*
	 * unit testing findCustomerByFirstNameAndLastName when customer found by first
	 * name and last name
	 */
	@WithMockUser(USER)
	@Test
	public void testFindCustomerByNameReturnResult() throws Exception {
		// Setup

		Mockito.when(customerService.findCustomerByFirstNameAndLastName(firstName, lastName)).thenReturn(customerList);

		RequestBuilder request = MockMvcRequestBuilders.get("/api/customers/testfirstname/testlastname")
				.accept(MediaType.APPLICATION_JSON);

		MvcResult mvcResult = this.mockMvc.perform(request).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(MockMvcResultMatchers.jsonPath("$").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty()).andReturn();

		String resultStr = mvcResult.getResponse().getContentAsString();

		Type listOfMyClassObject = new TypeToken<List<Customer>>() {
		}.getType();

		List<Customer> outputList = gson.fromJson(resultStr, listOfMyClassObject);

		assertEquals(customerList.size(), outputList.size());

		int i = 0;
		for (Customer resultCustomer : outputList) {
			assertEquals(customerList.get(i).getId(), resultCustomer.getId());
			assertEquals(customerList.get(i).getFirstName(), resultCustomer.getFirstName());
			assertEquals(customerList.get(i).getLastName(), resultCustomer.getLastName());
			assertEquals(customerList.get(i).getAddress().getId(), resultCustomer.getAddress().getId());
			assertEquals(customerList.get(i).getAddress().getCity(), resultCustomer.getAddress().getCity());
			assertEquals(customerList.get(i).getAddress().getStreet(), resultCustomer.getAddress().getStreet());
			assertEquals(customerList.get(i).getAddress().getState(), resultCustomer.getAddress().getState());
			i++;
		}

	}

	/*
	 * unit testing findCustomerByFirstNameAndLastName when customer NOT found by
	 * first name and last name
	 */

	@WithMockUser(USER)
	@Test
	public void testFindCustomerByNameReturnException() throws Exception {
		// Setup

		Mockito.when(customerService.findCustomerByFirstNameAndLastName(firstName, lastName)).thenReturn(null);

		RequestBuilder request = MockMvcRequestBuilders.get("/api/customers/testfirstname/testlastname")
				.accept(MediaType.APPLICATION_JSON);

		MvcResult mvcResult = this.mockMvc.perform(request).andExpect(status().isNotFound())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(MockMvcResultMatchers.jsonPath("$").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty()).andReturn();

		// verify
		String resultStr = mvcResult.getResponse().getContentAsString();
		ErrorResponse resultObj = gson.fromJson(resultStr, ErrorResponse.class);

		assertEquals(404, resultObj.getStatus());
		assertEquals("Customer not found with given first and last name " + firstName + " " + lastName,
				resultObj.getMessage());

	}

	/*
	 * Unit test for method AddCustomer. Successfully adding customer.
	 */
	@WithMockUser(roles = ADMIN)
	@Test
	public void testAddCustomer() throws Exception {

		// Setup
		// get the customer object with id equals to 0
		Customer customerObj = getCustomer(0);

		String jsonStr = gson.toJson(customerObj);
		customerObj.setId(1);
		customerObj.getAddress().setId(1);

		Mockito.when(customerService.save(any())).thenReturn(customerObj);

		RequestBuilder request = MockMvcRequestBuilders.post(CUSTOMERSURI).content(jsonStr)
				.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON);

		// action
		MvcResult mvcResult = this.mockMvc.perform(request).andExpect(status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("$").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty()).andReturn();

		// Verify
		verify(customerService, times(1)).save(any(Customer.class));

		String resultStr = mvcResult.getResponse().getContentAsString();
		Customer resultObj = gson.fromJson(resultStr, Customer.class);

		assertEquals(customer.getId(), resultObj.getId());
		assertEquals(customer.getFirstName(), resultObj.getFirstName());
		assertEquals(customer.getLastName(), resultObj.getLastName());
		assertEquals(customer.getAddress().getId(), resultObj.getAddress().getId());
		assertEquals(customer.getAddress().getCity(), resultObj.getAddress().getCity());
		assertEquals(customer.getAddress().getStreet(), resultObj.getAddress().getStreet());
		assertEquals(customer.getAddress().getState(), resultObj.getAddress().getState());

	}

	/*
	 * Unit test for AddCustomer method with role USER forbidden status returned
	 */
	@WithMockUser(roles = USER)
	@Test
	public void testAddCustomerForbidden() throws Exception {

		// Setup
		Customer customerObj = getCustomer(0);

		String jsonStr = gson.toJson(customerObj);

		RequestBuilder request = MockMvcRequestBuilders.post(CUSTOMERSURI).content(jsonStr)
				.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON);

		// action and verify
		this.mockMvc.perform(request).andExpect(status().isForbidden()).andReturn();

		// verify
		verify(customerService, times(0)).save(any(Customer.class));
	}

	/*
	 * Unit test for UpdateCustomer Customer found with the given ID and updated
	 * successfully
	 */
	@WithMockUser(roles = ADMIN)
	@Test
	public void testUpdateCustomerCustomerFound() throws Exception {
		// Setup

		Mockito.when(customerService.findCustomerById(1)).thenReturn(customer);

		Customer customerObj = getCustomer(1);
		customerObj.setFirstName("changedName");
		String jsonStr = gson.toJson(customerObj);
		Mockito.when(customerService.save(any())).thenReturn(customerObj);

		// calling rest API put with JSON string
		RequestBuilder request = MockMvcRequestBuilders.put(CUSTOMERSURI).content(jsonStr)
				.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON);

		// action
		MvcResult mvcResult = this.mockMvc.perform(request).andExpect(status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("$").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty()).andReturn();

		// Verify
		String resultStr = mvcResult.getResponse().getContentAsString();
		Customer resultObj = gson.fromJson(resultStr, Customer.class);

		verify(customerService, times(1)).findCustomerById(1);
		verify(customerService, times(1)).save(any(Customer.class));
		assertEquals(customerObj.getId(), resultObj.getId());
		assertEquals(customerObj.getFirstName(), resultObj.getFirstName());
		assertEquals(customerObj.getLastName(), resultObj.getLastName());

	}

	/*
	 * Unit test for UpdateCustomer method Customer not found with the given ID.
	 */
	@WithMockUser(roles = ADMIN)
	@Test
	public void testUpdateCustomerCustomerNotFound() throws Exception {
		// Setup
		// customer object is posted
		String jsonStr = gson.toJson(customer);
		Mockito.when(customerService.findCustomerById(1)).thenReturn(null);

		RequestBuilder request = MockMvcRequestBuilders.put(CUSTOMERSURI).content(jsonStr)
				.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON);

		// action
		MvcResult mvcResult = this.mockMvc.perform(request).andExpect(status().isNotFound())
				.andExpect(MockMvcResultMatchers.jsonPath("$").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty()).andReturn();

		// Verify
		String resultStr = mvcResult.getResponse().getContentAsString();
		ErrorResponse resultObj = gson.fromJson(resultStr, ErrorResponse.class);

		verify(customerService, times(1)).findCustomerById(1);
		verify(customerService, times(0)).save(any(Customer.class));
		assertEquals(404, resultObj.getStatus());
		assertEquals("Customer not found with id " + customer.getId(), resultObj.getMessage());

	}

	/*
	 * unit test for UpdateCustomer method with User role, forbidden is returned
	 */
	@WithMockUser(roles = USER)
	@Test
	public void testUpdateCustomerForbidden() throws Exception {
		// Setup
		// customer object is posted
		String jsonStr = gson.toJson(customer);

		RequestBuilder request = MockMvcRequestBuilders.put(CUSTOMERSURI).content(jsonStr)
				.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON);

		// action
		this.mockMvc.perform(request).andExpect(status().isForbidden()).andReturn();

		// verify
		verify(customerService, times(0)).findCustomerById(1);
		verify(customerService, times(0)).save(any(Customer.class));

	}

	/*
	 * Unit test for deleteCustomer method with Admin role Customer found and
	 * deleted successfully
	 */
	@WithMockUser(roles = ADMIN)
	@Test
	public void testdeleteCustomerCustomerFound() throws Exception {
		// Setup

		Mockito.when(customerService.findCustomerById(1)).thenReturn(customer);

		// Invoking delete rest API
		RequestBuilder request = MockMvcRequestBuilders.delete(CUSTOMERSBYIDURI)
				.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON);

		// action
		MvcResult mvcResult = this.mockMvc.perform(request).andExpect(status().is2xxSuccessful())
				.andExpect(MockMvcResultMatchers.jsonPath("$").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty()).andReturn();

		// Verify
		String resultStr = mvcResult.getResponse().getContentAsString();

		verify(customerService, times(1)).findCustomerById(customer.getId());
		verify(customerService, times(1)).deleteCustomerById(customer.getId());
		assertEquals("customer with id : " + customer.getId() + " deleted", resultStr);

	}

	/*
	 * Unit test for delete method with User Role
	 */
	@WithMockUser(roles = USER)
	@Test
	public void testDeleteCustomerForbidden() throws Exception {

		RequestBuilder request = MockMvcRequestBuilders.delete(CUSTOMERSBYIDURI)
				.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON);

		// action
		this.mockMvc.perform(request).andExpect(status().isForbidden()).andReturn();

		// verify
		verify(customerService, times(0)).findCustomerById(1);
		verify(customerService, times(0)).deleteCustomerById(1);

	}

	private Customer getCustomer(int i) {

		Address address = new Address(i, "irene", "vel", "brabant", "3241", "NL");

		customer = new Customer(i, "testfirstname", "testlastname", 30, address);

		return customer;
	}
}
