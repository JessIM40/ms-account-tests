package com.bootcamp.msaccount.controller;

import com.bootcamp.msaccount.model.entity.Account;
import com.bootcamp.msaccount.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/account")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        return ResponseEntity.status(201).body(accountService.createAccount(account));
    }

    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    @PutMapping("/{accountId}/deposit")
    public ResponseEntity<Account> deposit(@PathVariable Long accountId, @RequestBody Double amount) {
        return ResponseEntity.ok(accountService.deposit(accountId, amount));
    }

    @PutMapping("/{accountId}/withdraw")
    public ResponseEntity<Account> withdraw(@PathVariable Long accountId, @RequestBody Double amount) {
        return ResponseEntity.ok(accountService.withdraw(accountId, amount));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }
}
