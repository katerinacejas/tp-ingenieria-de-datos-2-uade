package com.poliglota.service;

import com.poliglota.model.mysql.Account;
import com.poliglota.repository.AccountRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account getAccountByUser(Long userId) {
        return accountRepository.findByUserId(userId);
    }

    public Account saveAccount(Account account) {
        return accountRepository.save(account);
    }

    public boolean deleteAccount(Long id) {
        if (accountRepository.existsById(id)) {
            accountRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
