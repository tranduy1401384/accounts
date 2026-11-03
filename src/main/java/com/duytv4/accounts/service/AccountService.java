package com.duytv4.accounts.service;

import com.duytv4.accounts.exception.AccountNotFoundException;
import com.duytv4.accounts.model.Account;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/** Example service backed by an in-memory map; swap for a repository once a datasource exists. */
@Service
@Slf4j
public class AccountService {

    private final Map<Long, Account> store = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong();

    public List<Account> findAll() {
        log.info("Find all");
        return List.copyOf(store.values());
    }

    public Account findById(Long id) {
        Account account = store.get(id);
        if (account == null) {
            throw new AccountNotFoundException(id);
        }
        return account;
    }

    public Account create(Account account) {
        log.info("Create account [{}]", account);
        account.setId(sequence.incrementAndGet());
        if (account.getBalance() == null) {
            account.setBalance(BigDecimal.ZERO);
        }
        if (account.getStatus() == null) {
            account.setStatus("ACTIVE");
        }
        store.put(account.getId(), account);
        return account;
    }

    public Account update(Long id, Account account) {
        findById(id);
        account.setId(id);
        store.put(id, account);
        return account;
    }

    public void delete(Long id) {
        if (store.remove(id) == null) {
            throw new AccountNotFoundException(id);
        }
    }
}
