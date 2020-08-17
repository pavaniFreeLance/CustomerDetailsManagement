package com.service.customerdetails.repository;

import com.service.customerdetails.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

/*
 * Repository providing CRUD DB functionalities on Address Table
 */
public interface AddressRepository extends JpaRepository<Address,Integer> {
}
