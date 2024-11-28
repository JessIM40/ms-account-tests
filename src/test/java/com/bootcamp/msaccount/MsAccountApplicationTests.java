package com.bootcamp.msaccount;

import com.bootcamp.msaccount.model.AccountType;
import com.bootcamp.msaccount.model.entity.Account;
import com.bootcamp.msaccount.repository.AccountRepository;
import com.bootcamp.msaccount.service.AccountService;
import com.bootcamp.msaccount.service.impl.AccountServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MsAccountApplicationTests {

	@Mock
	private AccountRepository accountRepository;

	@InjectMocks
	private AccountServiceImpl accountService;

	private Account testAccount;

	@BeforeEach
	void setUp() {
		testAccount = new Account();
		testAccount.setId(1L);
		testAccount.setAccountNumber("AH-12345");
		testAccount.setBalance(1000.0);
		testAccount.setAccountType(AccountType.AHORROS);
		testAccount.setCustomerId("customer1");
	}

	@Test
	void contextLoads() {
		assertNotNull(accountService);
	}

	@Test
	void createAccount_Success() {
		when(accountRepository.save(any(Account.class))).thenReturn(testAccount);

		Account createdAccount = accountService.createAccount(testAccount);

		assertNotNull(createdAccount);
		assertEquals("AH-12345", createdAccount.getAccountNumber());
		assertEquals(0.0, createdAccount.getBalance());
		verify(accountRepository, times(1)).save(any(Account.class));
	}


	@Test
	void getAllAccounts_Success() {
		when(accountRepository.findAll()).thenReturn(List.of(testAccount));

		List<Account> accounts = accountService.getAllAccounts();

		assertNotNull(accounts);
		assertEquals(1, accounts.size());
		verify(accountRepository, times(1)).findAll();
	}


	@Test
	void getAccountById_AccountExists() {

		when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));

		Account foundAccount = accountService.getAccountById(1L);

		assertNotNull(foundAccount);
		assertEquals(1L, foundAccount.getId());
		verify(accountRepository, times(1)).findById(1L);
	}


	@Test
	void getAccountById_AccountFound() {

		when(accountRepository.findById(1L)).thenReturn(Optional.empty());

		RuntimeException exception = assertThrows(RuntimeException.class, () -> accountService.getAccountById(1L));
		assertEquals("Cuenta no encontrada", exception.getMessage());
		verify(accountRepository, times(1)).findById(1L);
	}


	@Test
	void deposit_Success() {

		when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));
		when(accountRepository.save(any(Account.class))).thenReturn(testAccount);

		Account updatedAccount = accountService.deposit(1L, 500.0);

		assertNotNull(updatedAccount);
		assertEquals(1500.0, updatedAccount.getBalance());
		verify(accountRepository, times(1)).save(any(Account.class));
	}


	@Test
	void withdraw_Success() {

		when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));
		when(accountRepository.save(any(Account.class))).thenReturn(testAccount);

		Account updatedAccount = accountService.withdraw(1L, 500.0);

		assertNotNull(updatedAccount);
		assertEquals(500.0, updatedAccount.getBalance());
		verify(accountRepository, times(1)).save(any(Account.class));
	}


	@Test
	void withdraw_InsufficientFunds() {

		when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));

		RuntimeException exception = assertThrows(RuntimeException.class, () -> accountService.withdraw(1L, 1500.0));
		assertEquals("Saldo insuficiente para cuentas de ahorro", exception.getMessage());
		verify(accountRepository, never()).save(any(Account.class));
	}


	@Test
	void deleteAccount_Success() {

		doNothing().when(accountRepository).deleteById(1L);

		accountService.deleteAccount(1L);

		verify(accountRepository, times(1)).deleteById(1L);
	}
}
