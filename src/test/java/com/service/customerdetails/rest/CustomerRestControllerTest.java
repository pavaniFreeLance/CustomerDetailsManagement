package com.service.customerdetails.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.service.customerdetails.model.Address;
import com.service.customerdetails.model.Customer;
import com.service.customerdetails.service.CustomerService;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = CustomerRestController.class)
public class CustomerRestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CustomerService customerService;

	private List<Customer> customerList;

	private Customer customer;

	private Gson gson;

	@Before
	public void setUp() {
		Address address = new Address(1, "irene", "vel", "brabant", "3241", "NL");
		customer = new Customer(1, "testfirstname", "testlastname", 30, address);
		customerList = Arrays.asList(customer);
		gson = new Gson();
	}

	/* Test findAll with the result returned */
	@WithMockUser("abc")
	@Test
	public void testFindAllCustomers_resultReturned() throws Exception {
		// set up
		Mockito.when(customerService.findAll()).thenReturn(customerList);
		RequestBuilder request = MockMvcRequestBuilders.get("/api/customers").accept(MediaType.APPLICATION_JSON);

		// when
		MvcResult mvcResult = this.mockMvc.perform(request).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType("application/json"))
				.andExpect(MockMvcResultMatchers.jsonPath("$").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty()).andReturn();

		String resultStr = mvcResult.getResponse().getContentAsString();

		Type listOfMyClassObject = new TypeToken<List<Customer>>() {
		}.getType();

		List<Customer> outputList = gson.fromJson(resultStr, listOfMyClassObject);
		
		//verify
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

	/* Test findAll with the result returned is empty list */
	@WithMockUser("abc")
	@Test
	public void testFindAllCustomers_resultEmptyList() throws Exception {
		// set up
		List<Customer> emptyList = new ArrayList<Customer>();
		Mockito.when(customerService.findAll()).thenReturn(emptyList);

		RequestBuilder request = MockMvcRequestBuilders.get("/api/customers").accept(MediaType.APPLICATION_JSON);

		// when
		this.mockMvc.perform(request).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType("application/json"))
				.andExpect(MockMvcResultMatchers.jsonPath("$").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty()).andReturn();

	}

	/* Test findCustomerByID with result returned */
	@WithMockUser("abc")
	@Test
	public void testFindCustomerById_returnResult() throws Exception {
		// Setup
		Mockito.when(customerService.findCustomerById(customer.getId())).thenReturn(customer);

		RequestBuilder request = MockMvcRequestBuilders.get("/api/customers/1").accept(MediaType.APPLICATION_JSON);

		// action
		MvcResult mvcResult = this.mockMvc.perform(request).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType("application/json"))
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

	/* Test findCustomerByID with result null */
	@WithMockUser("abc")
	@Test
	public void testFindCustomerById_returnNull() throws Exception {
		// Setup
		Mockito.when(customerService.findCustomerById(customer.getId())).thenReturn(null);

		RequestBuilder request = MockMvcRequestBuilders.get("/api/customers/100").accept(MediaType.APPLICATION_JSON);

		// action
		this.mockMvc.perform(request).andExpect(status().isNotFound())
		.andReturn();

	}

	@WithMockUser("abc")
	@Test
	public void testFindCustomerByName_returnResult() throws Exception {
		// Setup
		String firstName = "testfirstname";
		String lastName = "testlastname";
		Mockito.when(customerService.findCustomerByFirstNameAndLastName(firstName, lastName)).thenReturn(customerList);

		RequestBuilder request = MockMvcRequestBuilders.get("/api/customers/testfirstname/testlastname")
				.accept(MediaType.APPLICATION_JSON);

		MvcResult mvcResult = this.mockMvc.perform(request).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType("application/json"))
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
	

	@WithMockUser("abc")
	@Test
	public void testFindCustomerByName_returnException() throws Exception {
		// Setup
		String firstName = "testfirstname";
		String lastName = "testlastname";
		Mockito.when(customerService.findCustomerByFirstNameAndLastName(firstName, lastName)).thenReturn(null);

		RequestBuilder request = MockMvcRequestBuilders.get("/api/customers/testfirstname/testlastname")
				.accept(MediaType.APPLICATION_JSON);

		MvcResult mvcResult = this.mockMvc.perform(request).andExpect(status().isNotFound())
				.andExpect(MockMvcResultMatchers.content().contentType("application/json"))
				.andExpect(MockMvcResultMatchers.jsonPath("$").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())				
				.andReturn();
		String resultStr = mvcResult.getResponse().getContentAsString();
		ErrorResponse resultObj = gson.fromJson(resultStr, ErrorResponse.class);

		assertEquals(404, resultObj.getStatus());
		assertEquals("Customer not found with given first and last name "+firstName +" "+lastName, resultObj.getMessage());
		

	}
	
	
	
	@WithMockUser(roles = "ADMIN")
	@Test 
	 public void testAddCustomer() throws Exception { 
		
		// Setup
		Customer toAddCustomer = customer;
		
	//	toAddCustomer.setId(0);
		//toAddCustomer.getAddress().setId(0);
		
		//String jsonStr = gson.toJson(toAddCustomer);
		String jsonStr = asJsonString(toAddCustomer);
		
		
		Mockito.when(customerService.save(toAddCustomer)).thenReturn(customer);

		RequestBuilder request = MockMvcRequestBuilders.post("/api/customers").content(jsonStr).accept(MediaType.APPLICATION_JSON);

				// action
		MvcResult mvcResult = this.mockMvc.perform(request).andExpect(status().isCreated())
						.andExpect(MockMvcResultMatchers.content().contentType("application/json"))
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
	
	public static String asJsonString(final Object obj) {
	    try {
	        return new ObjectMapper().writeValueAsString(obj);
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}
	 
	 /* 
	 * @Test void testUpdateCustomer() { fail("Not yet implemented"); }
	 * 
	 * @Test void testDeleteCustomer() { fail("Not yet implemented"); }
	 */

}
