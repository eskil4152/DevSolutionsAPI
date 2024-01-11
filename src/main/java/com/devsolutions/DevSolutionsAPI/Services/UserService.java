package com.devsolutions.DevSolutionsAPI.Services;

import com.devsolutions.DevSolutionsAPI.Entities.Users;
import com.devsolutions.DevSolutionsAPI.Security.PasswordEncoder;
import com.devsolutions.DevSolutionsAPI.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public Optional<Users> login(String username, String password){
        Optional<Users> user = userRepository.findByUsername(username);

        if (user.isEmpty())
            return Optional.empty();

        Users existingUser = user.get();

        boolean hashedPassword = PasswordEncoder.checkPassword(password, existingUser.getPassword());

        if (!hashedPassword){
            return Optional.empty();
        }

        return Optional.of(existingUser);
    }

    public Optional<Users> register(String firstname, String lastname, String username, String password, String email){
        Optional<Users> user = userRepository.findByUsername(username);

        if (user.isPresent())
            return Optional.empty();

        Users newUser = new Users(firstname, lastname, username, PasswordEncoder.hashPassword(password), email);

        userRepository.save(newUser);

        return Optional.of(newUser);
    }

    public Optional<Users> getUser(String username) {
        return userRepository.findByUsername(username);
    }

}
