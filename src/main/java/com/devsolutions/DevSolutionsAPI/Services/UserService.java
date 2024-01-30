package com.devsolutions.DevSolutionsAPI.Services;

import com.devsolutions.DevSolutionsAPI.Entities.Users;
import com.devsolutions.DevSolutionsAPI.Enums.UserRole;
import com.devsolutions.DevSolutionsAPI.Enums.UserRoleChange;
import com.devsolutions.DevSolutionsAPI.RequestBodies.RoleChangeRequest;
import com.devsolutions.DevSolutionsAPI.Security.PasswordEncoder;
import com.devsolutions.DevSolutionsAPI.Repositories.UserRepository;
import com.devsolutions.DevSolutionsAPI.Tools.ChangeRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    public Optional<List<Users>> getAllUsers(){
        return userRepository.findAllByRole(UserRole.USER);
    }

    public Optional<List<Users>> getAllModerators(){
        return userRepository.findAllByRole(UserRole.MODERATOR);
    }

    @Transactional
    public int changeUserRole(RoleChangeRequest request) {
        Optional<Users> userOptional = userRepository.findById(request.getId());

        if (userOptional.isEmpty() || !userOptional.get().getUsername().equals(request.getUsername()))
            return 404;

        Users user = userOptional.get();
        UserRole currentRole = user.getRole();
        UserRoleChange change = request.getChange();
        UserRole newRole = null;

        switch (change) {
            case PROMOTE:
                newRole = ChangeRole.getNextHigherRole(currentRole);
                break;
            case DEMOTE:
                newRole = ChangeRole.getNextLowerRole(currentRole);
            default:
                break;
        }

        System.out.println("Passing role " + newRole + " to user with id " + user.getId());
        userRepository.changeUserRole(newRole, user);

        return 200;
    }
}
