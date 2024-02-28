package com.devsolutions.DevSolutionsAPI.Services;

import com.devsolutions.DevSolutionsAPI.Entities.Users;
import com.devsolutions.DevSolutionsAPI.Enums.UserRole;
import com.devsolutions.DevSolutionsAPI.Enums.UserRoleChange;
import com.devsolutions.DevSolutionsAPI.RequestBodies.RoleChangeRequest;
import com.devsolutions.DevSolutionsAPI.Security.PasswordEncoder;
import com.devsolutions.DevSolutionsAPI.Repositories.UserRepository;
import com.devsolutions.DevSolutionsAPI.Tools.ChangeRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final HashMap<String, Users> cache;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
        this.cache = new HashMap<>();
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void resetUsersCache(){
        cache.clear();
    }

    public Optional<Users> login(String username, String password){
        if (cache.containsKey(username)){
            Users cachedUser = cache.get(username);
            return PasswordEncoder.checkPassword(password, cachedUser.getPassword()) ? Optional.of(cachedUser) : Optional.empty();
        }

        Optional<Users> user = userRepository.findByUsername(username);

        if (user.isEmpty()){
            return Optional.empty();
        }

        boolean checkPassword = PasswordEncoder.checkPassword(password, user.get().getPassword());

        if (!checkPassword){
            return Optional.empty();
        }

        cache.put(username, user.get());
        return user;
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

    public Optional<List<Users>> getAllAdmins(){
        return userRepository.findAllByRole(UserRole.ADMIN);
    }

    @Transactional
    public int changeUserRole(RoleChangeRequest request, UserRole role) {
        Optional<Users> userOptional = userRepository.findByUsername(request.getUsername());

        if (userOptional.isEmpty())
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

        if ((newRole == UserRole.ADMIN || currentRole == UserRole.ADMIN) && role != UserRole.OWNER) {
            return 401;
        }

        userRepository.changeUserRole(newRole, user);

        return 200;
    }
}