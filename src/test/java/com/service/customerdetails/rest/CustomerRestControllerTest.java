package com.service.customerdetails.rest;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
import com.service.customerdetails.dto.AddressDto;
import com.service.customerdetails.dto.CustomerDto;
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

	private static final String CUSTOMERS_URI = "/api/customers";

	private static final String CUSTOMERS_BY_ID_URI = "/api/customers/1";

	private static final String CUSTOMERS_ADDRESS_BY_ID_URI = "/api/customers/1/address/1";

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
	 * Test findCustomerByID when customer found with given ID
	 */
	@WithMockUser(USER)
	@Test
	public void testFindCustomerByIdCustomerFound() throws Exception {
		// Setup
		Optional<Customer> customerOptionalObj = Optional.of(getCustomer(1));

		Mockito.when(customerService.findCustomerById(customer.getId())).thenReturn(customerOptionalObj);

		RequestBuilder request = MockMvcRequestBuilders.get(CUSTOMERS_BY_ID_URI).accept(MediaType.APPLICATION_JSON);

		// action
		MvcResult mvcResult = this.mockMvc.perform(request).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(MockMvcResultMatchers.jsonPath("$").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty()).andReturn();

		// Verify
		verifyReturnedCustomerDto(mvcResult, customerOptionalObj.get());

	}

	/*
	 * Test findCustomerByID with no Customer found with given ID
	 */
	@WithMockUser(USER)
	@Test
	public void testFindCustomerByIdCustomerNotFound() throws Exception {
		// Setup
		Mockito.when(customerService.findCustomerById(customer.getId())).thenReturn(Optional.ofNullable(null));

		RequestBuilder request = MockMvcRequestBuilders.get("/api/customers/100").accept(MediaType.APPLICATION_JSON);

		// action
		this.mockMvc.perform(request).andExpect(status().isNotFound()).andReturn();

	}

	/*
	 * unit testing findCustomers when customer found by first name and last name
	 */
	@WithMockUser(USER)
	@Test
	public void testfindCustomersCustomerFoundWithFirstNameAndLastName() throws Exception {
		// Setup

		Mockito.when(customerService.findCustomers(Optional.ofNullable(firstName), Optional.ofNullable(lastName)))
		.thenReturn(Optional.of(customerList));

		RequestBuilder request = MockMvcRequestBuilders
				.get("/api/customers?firstName=" + firstName + "&&lastName=" + lastName)
				.accept(MediaType.APPLICATION_JSON);

		// perform
		MvcResult mvcResult = this.mockMvc.perform(request).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(MockMvcResultMatchers.jsonPath("$").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty()).andReturn();
		// Verify results
		verifyReturnedCustomerList(mvcResult);

	}

	/*
	 * unit testing findCustomers when customer NOT found by first name and last
	 * name
	 */

	@WithMockUser(USER)
	@Test
	public void testfindCustomerByFirstNameAndOrLastNameCustomerNotFoundWithFirstNameAndLastName() throws Exception {
		// Setup

		Mockito.when(customerService.findCustomers(Optional.ofNullable(firstName), Optional.ofNullable(lastName)))
		.thenReturn(Optional.ofNullable(null));

		RequestBuilder request = MockMvcRequestBuilders.get("/api/customers?firstName=" + Optional.ofNullable(firstName)
		+ "&&lastName=" + Optional.ofNullable(lastName)).accept(MediaType.APPLICATION_JSON);

		// perform
		MvcResult mvcResult = this.mockMvc.perform(request).andExpect(status().isNotFound())
				.andExpect(MockMvcResultMatchers.jsonPath("$").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty()).andReturn();

		// verify results
		verifyNotFound(mvcResult);
	}

	/*
	 * Unit test for method AddCustomer. Successfully adding customer with ADMIN
	 * role
	 */
	@WithMockUser(roles = ADMIN)
	@Test
	public void testAddCustomer() throws Exception {

		// Setup
		// Creating the customerDto object with id equals to 0
		CustomerDto customerDtoObj = getCustomerDto(0);

		String jsonStr = gson.toJson(customerDtoObj);
		customerDtoObj.setId(1);
		customerDtoObj.getAddressDto().setId(1);

		Mockito.when(customerService.saveCustomer(any())).thenReturn(customer);

		RequestBuilder request = MockMvcRequestBuilders.post(CUSTOMERS_URI).content(jsonStr)
				.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON);

		// action
		MvcResult mvcResult = this.mockMvc.perform(request).andExpect(status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("$").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty()).andReturn();

		// Verify
		verify(customerService, times(1)).saveCustomer(any(Customer.class));

		verifyReturnedCustomerDto(mvcResult, customer);

	}

	/*
	 * Unit test for AddCustomer method with role USER forbidden status returned
	 */
	@WithMockUser(roles = USER)
	@Test
	public void testAddCustomerForbidden() throws Exception {

		// Setup
		CustomerDto customerDtoObj = getCustomerDto(0);

		String jsonStr = gson.toJson(customerDtoObj);

		RequestBuilder request = MockMvcRequestBuilders.post(CUSTOMERS_URI).content(jsonStr)
				.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON);

		// action and verify
		this.mockMvc.perform(request).andExpect(status().isForbidden()).andReturn();

		// verify
		verify(customerService, times(0)).saveCustomer(any(Customer.class));
	}

	/*
	 * Unit test for UpdateCustomer Customer found with the given ID and updated
	 * successfully
	 */
	@WithMockUser(roles = ADMIN)
	@Test
	public void testUpdateCustomerCustomerFound() throws Exception {
		// Setup
		CustomerDto customerDtoInputObj = getCustomerDto(1);
		customerDtoInputObj.setFirstName("changedName");
		String jsonStr = gson.toJson(customerDtoInputObj);

		Customer customerSavedObj = getCustomer(1);
		customerSavedObj.setFirstName("changedName");

		Mockito.when(customerService.findCustomerById(1)).thenReturn(Optional.of(customer));
		Mockito.when(customerService.saveCustomer(any())).thenReturn(customerSavedObj);

		// calling rest API put with JSON string
		RequestBuilder request = MockMvcRequestBuilders.put(CUSTOMERS_URI).content(jsonStr)
				.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON);

		// action
		MvcResult mvcResult = this.mockMvc.perform(request).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty()).andReturn();

		// Verify
		String resultStr = mvcResult.getResponse().getContentAsString();
		CustomerDto resultObj = gson.fromJson(resultStr, CustomerDto.class);

		verify(customerService, times(1)).findCustomerById(1);
		verify(customerService, times(1)).saveCustomer(any(Customer.class));
		assertEquals(customerDtoInputObj.getId(), resultObj.getId());
		assertEquals(customerDtoInputObj.getFirstName(), resultObj.getFirstName());
		assertEquals(customerDtoInputObj.getLastName(), resultObj.getLastName());

	}

	/*
	 * Unit test for UpdateCustomer method Customer not found with the given ID.
	 */
	@WithMockUser(roles = ADMIN)
	@Test
	public void testUpdateCustomerCustomerNotFound() throws Exception {
		// Setup
		// customer object is posted
		CustomerDto customerDtoInputObj = getCustomerDto(1);
		customerDtoInputObj.setFirstName("changedName");
		String jsonStr = gson.toJson(customerDtoInputObj);

		Mockito.when(customerService.findCustomerById(1)).thenReturn(Optional.ofNullable(null));

		RequestBuilder request = MockMvcRequestBuilders.put(CUSTOMERS_URI).content(jsonStr)
				.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON);

		// action
		MvcResult mvcResult = this.mockMvc.perform(request).andExpect(status().isNotFound())
				.andExpect(MockMvcResultMatchers.jsonPath("$").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty()).andReturn();

		// Verify
		verifyNotFound(mvcResult);
		verify(customerService, times(1)).findCustomerById(1);
		verify(customerService, times(0)).saveCustomer(any(Customer.class));

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

		RequestBuilder request = MockMvcRequestBuilders.put(CUSTOMERS_URI).content(jsonStr)
				.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON);

		// action
		this.mockMvc.perform(request).andExpect(status().isForbidden()).andReturn();

		// verify
		verify(customerService, times(0)).findCustomerById(1);
		verify(customerService, times(0)).saveCustomer(any(Customer.class));

	}

	/*
	 * Unit test for updateCustomerAddress, Customer found with the given ID and
	 * updated successfully
	 */
	@WithMockUser(roles = ADMIN)
	@Test
	public void testupdateCustomerAddressCustomerFound() throws Exception {

		// Creating Address Dto object
		AddressDto addressDto = new AddressDto(1, "UpdatingStreet", "vel", "brabant", "3241", "NL");
		String jsonStr = gson.toJson(addressDto);

		Customer customerSavedObj = getCustomer(1);
		customerSavedObj.getAddress().setStreet("UpdatingStreet");

		Mockito.when(customerService.findCustomerById(1)).thenReturn(Optional.of(customer));
		Mockito.when(customerService.saveCustomer(any())).thenReturn(customerSavedObj);

		// Perform
		RequestBuilder request = MockMvcRequestBuilders.put(CUSTOMERS_ADDRESS_BY_ID_URI).content(jsonStr)
				.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON);

		// action
		MvcResult mvcResult = this.mockMvc.perform(request).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty()).andReturn();

		// Verify
		String resultStr = mvcResult.getResponse().getContentAsString();
		CustomerDto resultObj = gson.fromJson(resultStr, CustomerDto.class);

		verify(customerService, times(1)).findCustomerById(1);
		verify(customerService, times(1)).saveCustomer(any(Customer.class));
		assertEquals(addressDto.getId(), resultObj.getAddressDto().getId());
		assertEquals(addressDto.getStreet(), resultObj.getAddressDto().getStreet());
	}

	/*
	 * Unit test for UpdateCustomerAddress method Customer not found with the given
	 * ID.
	 */
	@WithMockUser(roles = ADMIN)
	@Test
	public void testupdateCustomerAddress_CustomerNotFound() throws Exception {

		// Creating Address Dto object
		AddressDto addressDto = new AddressDto(1, "differentStreet", "vel", "brabant", "3241", "NL");
		String jsonStr = gson.toJson(addressDto);

		Mockito.when(customerService.findCustomerById(1)).thenReturn(Optional.ofNullable(null));

		// Perform
		RequestBuilder request = MockMvcRequestBuilders.put(CUSTOMERS_ADDRESS_BY_ID_URI).content(jsonStr)
				.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON);

		// action
		MvcResult mvcResult = this.mockMvc.perform(request).andExpect(status().isNotFound())
				.andExpect(MockMvcResultMatchers.jsonPath("$").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty()).andReturn();

		verifyNotFound(mvcResult);
		verify(customerService, times(1)).findCustomerById(1);
		verify(customerService, times(0)).saveCustomer(any(Customer.class));

	}

	/*
	 * Unit test for UpdateCustomerAddress method Address not found with the given
	 * ID.
	 */
	@WithMockUser(roles = ADMIN)
	@Test
	public void testupdateCustomerAddress_AddressNotFound() throws Exception {

		// Creating Address Dto object
		AddressDto addressDto = new AddressDto(1, "differentStreet", "vel", "brabant", "3241", "NL");
		String jsonStr = gson.toJson(addressDto);

		Mockito.when(customerService.findCustomerById(1)).thenReturn(Optional.of(customer));

		// Perform
		RequestBuilder request = MockMvcRequestBuilders.put("/api/customers/1/address/10").content(jsonStr)
				.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON);

		// action
		MvcResult mvcResult = this.mockMvc.perform(request).andExpect(status().isNotFound())
				.andExpect(MockMvcResultMatchers.jsonPath("$").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty()).andReturn();

		verifyNotFound(mvcResult);
		verify(customerService, times(1)).findCustomerById(1);
		verify(customerService, times(0)).saveCustomer(any(Customer.class));

	}

	/*
	 * Unit test for deleteCustomer method with Admin role Customer found and
	 * deleted successfully
	 */
	@WithMockUser(roles = ADMIN)
	@Test
	public void testdeleteCustomerCustomerFound() throws Exception {
		// Setup

		Mockito.when(customerService.findCustomerById(1)).thenReturn(Optional.of(customer));

		// Invoking delete rest API
		RequestBuilder request = MockMvcRequestBuilders.delete(CUSTOMERS_BY_ID_URI)
				.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON);

		// action
		MvcResult mvcResult = this.mockMvc.perform(request).andExpect(status().is2xxSuccessful())
				.andExpect(MockMvcResultMatchers.jsonPath("$").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty()).andReturn();

		// Verify
		mvcResult.getResponse().getContentAsString();

		verify(customerService, times(1)).findCustomerById(customer.getId());
		verify(customerService, times(1)).deleteCustomerById(customer.getId());

	}

	/*
	 * Unit test for deleteCustomer method with Admin role Customer found and
	 * deleted successfully
	 */
	@WithMockUser(roles = ADMIN)
	@Test
	public void testdeleteCustomerCustomerNotFound() throws Exception {
		// Setup
		Mockito.when(customerService.findCustomerById(1)).thenReturn(Optional.ofNullable(null));

		// Invoking delete rest API
		RequestBuilder request = MockMvcRequestBuilders.delete(CUSTOMERS_BY_ID_URI)
				.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON);

		// action
		this.mockMvc.perform(request).andExpect(status().isNotFound()).andReturn();

		// Verify
		verify(customerService, times(1)).findCustomerById(customer.getId());
		verify(customerService, times(0)).deleteCustomerById(customer.getId());

	}

	/*
	 * Unit test for delete method with User Role
	 */
	@WithMockUser(roles = USER)
	@Test
	public void testDeleteCustomerForbidden() throws Exception {

		RequestBuilder request = MockMvcRequestBuilders.delete(CUSTOMERS_BY_ID_URI)
				.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON);

		// action
		this.mockMvc.perform(request).andExpect(status().isForbidden()).andReturn();

		// verify
		verify(customerService, times(0)).findCustomerById(1);
		verify(customerService, times(0)).deleteCustomerById(1);

	}

	/*
	 * Method to create customer object
	 * 
	 * @param id
	 */
	private Customer getCustomer(int i) {

		// Creating Customer object
		Address address = new Address(i, "irene", "vel", "brabant", "3241", "NL");

		Customer customer = new Customer(i, "testfirstname", "testlastname", 30, address);

		return customer;
	}

	/*
	 * 
	 */
	private CustomerDto getCustomerDto(int i) {

		// Creating Address Dto , CustomerDto object
		AddressDto addressDto = new AddressDto(i, "irene", "vel", "brabant", "3241", "NL");

		CustomerDto customerDto = new CustomerDto(i, "testfirstname", "testlastname", 30, addressDto);

		return customerDto;

	}

	/*
	 * Method to verify rest API returned object
	 * 
	 * @param mvcResult
	 * 
	 * @throws Exception
	 */
	private void verifyReturnedCustomerList(MvcResult mvcResult) throws Exception {
		// verify
		String resultStr = mvcResult.getResponse().getContentAsString();

		Type listOfMyClassObject = new TypeToken<List<CustomerDto>>() {
		}.getType();

		List<CustomerDto> outputList = gson.fromJson(resultStr, listOfMyClassObject);

		assertEquals(customerList.size(), outputList.size());

		int i = 0;
		for (CustomerDto resultCustomer : outputList) {
			assertEquals(customerList.get(i).getId(), resultCustomer.getId());
			assertEquals(customerList.get(i).getFirstName(), resultCustomer.getFirstName());
			assertEquals(customerList.get(i).getLastName(), resultCustomer.getLastName());
			assertEquals(customerList.get(i).getAddress().getId(), resultCustomer.getAddressDto().getId());
			assertEquals(customerList.get(i).getAddress().getCity(), resultCustomer.getAddressDto().getCity());
			assertEquals(customerList.get(i).getAddress().getStreet(), resultCustomer.getAddressDto().getStreet());
			assertEquals(customerList.get(i).getAddress().getState(), resultCustomer.getAddressDto().getState());
			i++;
		}
	}

	/*
	 * Verify result of Rest API perform when Error response is returned
	 * 
	 * @mvcResult
	 * 
	 * @throws Exception
	 */
	private void verifyNotFound(MvcResult mvcResult) throws Exception {
		// verify
		String resultStr = mvcResult.getResponse().getContentAsString();
		ErrorResponse resultObj = gson.fromJson(resultStr, ErrorResponse.class);

		assertEquals(404, resultObj.getStatus());

	}

	/*
	 * Verify Result object returned by perform on rest API
	 * 
	 * @mvcResult
	 * 
	 * @throws Exception
	 */
	private void verifyReturnedCustomerDto(MvcResult mvcResult, Customer customer) throws Exception {

		// Verify
		String resultStr = mvcResult.getResponse().getContentAsString();
		CustomerDto resultObj = gson.fromJson(resultStr, CustomerDto.class);

		assertEquals(customer.getId(), resultObj.getId());
		assertEquals(customer.getFirstName(), resultObj.getFirstName());
		assertEquals(customer.getLastName(), resultObj.getLastName());
		assertEquals(customer.getAddress().getId(), resultObj.getAddressDto().getId());
		assertEquals(customer.getAddress().getCity(), resultObj.getAddressDto().getCity());
		assertEquals(customer.getAddress().getStreet(), resultObj.getAddressDto().getStreet());
		assertEquals(customer.getAddress().getState(), resultObj.getAddressDto().getState());
	}
}
