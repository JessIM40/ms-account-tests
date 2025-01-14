package com.bootcamp.msaccount.service;

import com.bootcamp.msaccount.model.entity.Account;

import java.util.List;

public interface AccountService {
    Account createAccount(Account account);
    List<Account> getAllAccounts();
    Account getAccountById(Long id);
    Account deposit(Long accountId, Double amount);
    Account withdraw(Long accountId, Double amount);
    void deleteAccount(Long id);
}
