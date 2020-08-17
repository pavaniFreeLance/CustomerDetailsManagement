package com.service.customerdetails.service;

import com.service.customerdetails.model.Customer;

import java.util.List;

public interface CustomerService {

    public List<Customer> findAll();

    public Customer findCustomerById(int customerId);

    public List<Customer> findCustomerByFirstNameAndLastName(String firstName,String lastName);

    public Customer save(Customer customer);

    public void deleteCustomerById(int customerId);

}
