package com.service.customerdetails.service;

import com.service.customerdetails.model.Address;
import com.service.customerdetails.model.Customer;
import com.service.customerdetails.repository.CustomerRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceImplTest {
    @Mock
    private CustomerRepository customerRepository;

    private CustomerService customerService;

    private Customer customer;

    private Address address;


    @Before
    public void setUp() {
        customerService = new CustomerServiceImpl(customerRepository);
        address = new Address(1, "irene", "vel", "brabant", "3241", "NL");
        customer = new Customer(1, "aaa", "bbb", 30, address);
    }

    
    @Test
    public void findAllTest() {
        //set up
        List<Customer> customerList = new ArrayList<Customer>();
        customerList.add(customer);
        Mockito.when(customerRepository.findAll()).thenReturn(customerList);

        //when
        List<Customer> resultList = customerService.findAll();

        //verify results
        verify(customerRepository, times(1)).findAll();
        assertEquals(1, resultList.size());

    }

    @Test
    public void findCustomerByIdTest_customerPresent() {
        //setup
        int id = 1;
        Mockito.when(customerRepository.findById(1)).thenReturn(Optional.of(customer));

        //when
        Customer resultObj = customerService.findCustomerById(id);

        verify(customerRepository, times(1)).findById(id);
        assertNotNull(resultObj);
        assertEquals(id, resultObj.getId());
    }

    @Test
    public void findCustomerByIdTest_customerNotPresent() {
        //setup
        int id = 100;
     
        //when
        Customer resultObj = customerService.findCustomerById(id);

        verify(customerRepository, times(1)).findById(id);
        assertNull(null, resultObj);
    }

    @Test
    public void findCustomerByFirstNameAndLastNameTest_CustomerPresent() {
        //setup
        List<Customer> customerList = new ArrayList<Customer>();
        customerList.add(customer);
        Mockito.when(customerRepository.findByFirstNameAndLastNameIgnoreCase(customer.getFirstName(), customer.getLastName())).thenReturn(customerList);

        //when
        List<Customer> resultCustomerList = customerService.findCustomerByFirstNameAndLastName(customer.getFirstName(), customer.getLastName());

        //verify
        verify(customerRepository, times(1)).findByFirstNameAndLastNameIgnoreCase(customer.getFirstName(), customer.getLastName());
        assertEquals(1, resultCustomerList.size());

    }

}
