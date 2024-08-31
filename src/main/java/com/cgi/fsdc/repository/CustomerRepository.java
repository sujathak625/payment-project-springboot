package com.cgi.fsdc.repository;

import com.cgi.fsdc.utilities.enums.CardStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cgi.fsdc.entity.Customer;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer>{

    @Modifying
    @Transactional
    @Query("UPDATE Customer c SET c.status = :status WHERE c.custId = :customerId")
    int updateCardStatusByCustomerId(Integer customerId, CardStatus status);

}
