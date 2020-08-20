package com.service.customerdetails.rest;

import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;

import com.service.customerdetails.model.Customer;
import com.service.customerdetails.service.CustomerService;

/*
 * Controller with rest end points to add,update,search,delete customers  
 * 
 */
@RestController
@RequestMapping(value = "/api", produces = "application/json")
public class CustomerRestController {
	
    private CustomerService customerService;   

    
    /*Auto wiring CustomerService */
    @Autowired
    public CustomerRestController(CustomerService customerService){
    	
        this.customerService = customerService;
    }

    /*
     * Find all customers in the DB and return list in JSON format
     */
    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> findAllCustomers(){    	   	
        
    	return new ResponseEntity<>(customerService.findAll(),HttpStatus.OK);
    }
    
    /*
     * Find all customer with given ID and return response in JSON format
     * @path variable customerID
     * @return ResponseEntity<Customer>
     * 
     */
    @GetMapping("/customers/{customerId}")
    public ResponseEntity<Customer> findCustomerById(@PathVariable int customerId){
    	   	
    	
        Customer customer = customerService.findCustomerById(customerId);
        
        if(customer == null){            
        
        	throw new CustomerNotFoundException("Customer not found with id "+customerId);
        }
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    /*
     * Find all customer with given first name and last name.
     * @path variables First name, last name
     * @return ResponseEntity<Customer>
     * 
     */      
    @GetMapping("/customers/{firstName}/{lastName}")
    public ResponseEntity<List<Customer>> findCustomerByName(@PathVariable("firstName") String firstName,@PathVariable("lastName") String lastName){    	   	

        List<Customer> customersList = customerService.findCustomerByFirstNameAndLastName(firstName,lastName);
        
        if(customersList == null || customersList.isEmpty()){           
                    	
        	throw new CustomerNotFoundException("Customer not found with given first and last name "+firstName +" "+ lastName);
        }
        
        return new ResponseEntity<>(customersList, HttpStatus.OK);
    }

    /*
     * Adding new customer 
     * @Request body with customer details 
     * @return ResponseEntity<Customer>
     * 
     */  
    @PostMapping("/customers")
    public ResponseEntity<Customer> addCustomer(@RequestBody Customer customer){
    	
        customer.setId(0);
        
        customer.getAddress().setId(0);
        
        return new ResponseEntity<>(customerService.save(customer),HttpStatus.CREATED);
    }

    /*
     * Updating existing customer 
     * @Request body with customer details 
     * @return ResponseEntity<Customer>
     * 
     */
    @PutMapping("/customers")
    public ResponseEntity<Customer> updateCustomer(@RequestBody Customer customerDetails){
    	 
    	Customer customer = customerService.findCustomerById(customerDetails.getId());
    	
         if(customer == null){            
             //return ResponseEntity.notFound().build();
         	throw new CustomerNotFoundException("Customer not found with id "+customerDetails.getId());
         }
         
         customer.setFirstName(customerDetails.getFirstName());
         customer.setLastName(customerDetails.getLastName());
         customer.setAge(customerDetails.getAge());
         customer.getAddress().setStreet(customerDetails.getAddress().getStreet());
         customer.getAddress().setCity(customerDetails.getAddress().getCity());
         customer.getAddress().setState(customerDetails.getAddress().getState());
         customer.getAddress().setCountry(customerDetails.getAddress().getCountry());
         customer.getAddress().setZipCode(customerDetails.getAddress().getZipCode());                
         
        return new ResponseEntity<>(customerService.save(customer),HttpStatus.CREATED);
    }

    /*
     * deleting existing customer 
     * @path variable customerID 
     * @return String
     * 
     */
    @DeleteMapping("/customers/{customerId}")
    public String deleteCustomer(@PathVariable int customerId){

        Customer customer = customerService.findCustomerById(customerId);
        
        if(customer == null) {
          
        	throw new CustomerNotFoundException("Customer not found with id "+customerId);
        }

        customerService.deleteCustomerById(customerId);
        
        return "customer with id : "+customerId+" deleted";

    }
    
   
}
