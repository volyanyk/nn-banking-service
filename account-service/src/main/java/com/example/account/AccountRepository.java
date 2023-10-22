package com.example.account;

import com.example.mq.client.account.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    List<Account> findByCustomerId(Integer customerId);
    Optional<Account> findByCustomerIdAndType(Integer customerId, AccountType type);

    Optional<Account> findByIdAndCustomerIdAndType(Integer id, Integer customerId, AccountType type);

    Optional<Account> findByIdAndCustomerId(Integer id, Integer customerId);
}
