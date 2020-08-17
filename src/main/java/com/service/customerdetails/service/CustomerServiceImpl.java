package com.service.customerdetails.service;

import com.service.customerdetails.model.Customer;
import com.service.customerdetails.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Customer findCustomerById(int customerId) {

        Optional<Customer> customerOptionalObj = customerRepository.findById(customerId);
        Customer customer = null;
        if (customerOptionalObj.isPresent()) {
            customer = customerOptionalObj.get();
        }

        return customer;
    }

    public List<Customer> findCustomerByFirstNameAndLastName(String firstName,String lastName){
        return customerRepository.findByFirstNameAndLastNameIgnoreCase(firstName, lastName);
    }

    public Customer save(Customer customer) {

        return customerRepository.save(customer);
    }

    public void deleteCustomerById(int customerId) {
        customerRepository.deleteById(customerId);
    }


}
