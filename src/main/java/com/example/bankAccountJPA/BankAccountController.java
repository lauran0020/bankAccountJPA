package com.example.bankAccountJPA;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;


@RestController
public class BankAccountController {

    @Autowired
//    private BankAccountDAO accountDAO = new BankAccountDAO();
    private BankAccountJPARepo accountRepo;

    @GetMapping(path = "/getAllAccounts")
    public List<BankAccount> getAllAccounts() {
        return accountRepo.findAll();
    }

    @GetMapping(path = "/getAccount")
    public ResponseEntity getAccount(@RequestParam(value = "accNum") String accountNumber) {
        Optional<BankAccount> account = accountRepo.findById(accountNumber);
        if (!account.isPresent()) {
            return new ResponseEntity("User with account number " + accountNumber + " not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(account.get(), HttpStatus.OK);
    }

    @DeleteMapping(path = "/deleteAccount")
    public ResponseEntity deleteAccount(@RequestParam(value = "accNum") String accountNumber) {
          int result = accountRepo.deleteAccount(accountNumber);

        if (result == 0) {
            return new ResponseEntity("User with account number " + accountNumber + " not found", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>("Deleted account with account number: " + accountNumber + result, HttpStatus.OK);
    }

    @PostMapping(path="/addNewAccount")
    public ResponseEntity addNewAccount(@RequestBody BankAccount newAccount) {
        accountRepo.save(newAccount);
        return new ResponseEntity<>("Created new account", HttpStatus.CREATED);
    }



    @PutMapping(path="/increaseBalance")
    public ResponseEntity increaseBalance(@RequestParam(value="accNum") String accountNumber,
                                       @RequestParam(value="amt") double amount) {
        if (amount < 0 ){
            return new ResponseEntity("Please input an amount greater than 0", HttpStatus.BAD_GATEWAY);
        }
        Optional<BankAccount> accountOp = accountRepo.findById(accountNumber);
        if (!accountOp.isPresent()) {
            return new ResponseEntity("User with account number " + accountNumber + " not found", HttpStatus.NOT_FOUND);
        }

        BankAccount account = accountOp.get();
        double newBalance = account.getBalance() + amount;
        accountRepo.setNewBalance(accountNumber, newBalance);
        account.setBalance(newBalance);
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @PutMapping(path="/decreaseBalance")
    public ResponseEntity decreaseBalance(@RequestParam(value="accNum") String accountNumber,
                                       @RequestParam(value="amt") double amount) {
        if (amount < 0 ){
            return new ResponseEntity("Please input an amount greater than 0", HttpStatus.BAD_GATEWAY);
        }

        Optional<BankAccount> accountOp = accountRepo.findById(accountNumber);
        if (!accountOp.isPresent()) {
            return new ResponseEntity("User with account number " + accountNumber + " not found", HttpStatus.NOT_FOUND);
        }

        BankAccount account = accountOp.get();
        double curBalance = account.getBalance();

        if (amount > curBalance ){
            return new ResponseEntity("Please input an amount smaller than current balance: " + curBalance, HttpStatus.BAD_GATEWAY);
        }

        double newBalance = curBalance - amount;
        accountRepo.setNewBalance(accountNumber, newBalance);
        account.setBalance(newBalance);

        return new ResponseEntity<>(account, HttpStatus.OK);
    }
}


