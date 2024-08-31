package com.cgi.fsdc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cgi.fsdc.entity.Transaction;

import java.util.List;
import java.time.Instant;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findByCustomerIdAndCreateTimeAfter(Integer customerId, Instant createTime);
}
