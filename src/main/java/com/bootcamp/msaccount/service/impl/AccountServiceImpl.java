package com.bootcamp.msaccount.service.impl;

import com.bootcamp.msaccount.model.AccountType;
import com.bootcamp.msaccount.model.entity.Account;
import com.bootcamp.msaccount.repository.AccountRepository;
import com.bootcamp.msaccount.service.AccountService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account createAccount(Account account) {
        account.setAccountNumber(generateAccountNumber(account.getAccountType()));
        account.setBalance(0.0);
        return accountRepository.save(account);
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
    }

    @Transactional
    @Override
    public Account deposit(Long accountId, Double amount) {
        Account account = getAccountById(accountId);
        account.setBalance(account.getBalance() + amount);
        return accountRepository.save(account);
    }

    @Transactional
    @Override
    public Account withdraw(Long accountId, Double amount) {
        Account account = getAccountById(accountId);
        if (account.getAccountType() == AccountType.AHORROS && account.getBalance() - amount < 0) {
            throw new RuntimeException("Saldo insuficiente para cuentas de ahorro");
        }
        if (account.getAccountType() == AccountType.CORRIENTE && account.getBalance() - amount < -500) {
            throw new RuntimeException("Limite de sobregiro alcanzado para cuentas corrientes");
        }
        account.setBalance(account.getBalance() - amount);
        return accountRepository.save(account);
    }

    @Transactional
    @Override
    public void  deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }

    private String generateAccountNumber(AccountType accountType) {
        String prefix = accountType == AccountType.AHORROS ? "AH-" : "CC-";
        return prefix + System.currentTimeMillis();
    }
}
