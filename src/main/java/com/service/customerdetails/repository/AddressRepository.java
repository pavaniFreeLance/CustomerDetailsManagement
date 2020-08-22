package com.service.customerdetails.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.service.customerdetails.model.Address;

/*
 * Repository extending JPA repository
 * providing CRUD functionalities on Address Table
 */
public interface AddressRepository extends JpaRepository<Address, Integer> {
}
