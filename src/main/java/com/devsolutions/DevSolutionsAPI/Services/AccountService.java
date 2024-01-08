package com.devsolutions.DevSolutionsAPI.Services;

import com.devsolutions.DevSolutionsAPI.Entities.Accounts;
import com.devsolutions.DevSolutionsAPI.PasswordEncoder;
import com.devsolutions.DevSolutionsAPI.Repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public Optional<Accounts> login(String username, String password){
        Optional<Accounts> user = accountRepository.findByUsername(username);

        if (user.isEmpty())
            return Optional.empty();

        Accounts existingUser = user.get();

        boolean hashedPassword = PasswordEncoder.checkPassword(password, existingUser.getPassword());

        if (!hashedPassword){
            return Optional.empty();
        }

        return Optional.of(existingUser);
    }

    public Optional<Accounts> register(String username, String password, String email){
        Optional<Accounts> user = accountRepository.findByUsername(username);

        if (user.isPresent())
            return Optional.empty();

        Accounts newUser = new Accounts(username, PasswordEncoder.hashPassword(password), email);

        accountRepository.save(newUser);

        return Optional.of(newUser);
    }

}
