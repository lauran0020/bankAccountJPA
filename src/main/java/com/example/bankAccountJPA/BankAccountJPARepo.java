package com.example.bankAccountJPA;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;


@Repository
public interface BankAccountJPARepo extends JpaRepository<BankAccount, String>{
    @Transactional
    @Modifying
    @Query(value="delete from BankAccount b where b.accountNumber=?1")
    public int deleteAccount(String accountNumber);

    @Transactional
    @Modifying
    @Query(value="update BankAccount b set b.balance = ?2 where b.accountNumber=?1")
    public int  setNewBalance(String accountNumber, double newBalance);
}
