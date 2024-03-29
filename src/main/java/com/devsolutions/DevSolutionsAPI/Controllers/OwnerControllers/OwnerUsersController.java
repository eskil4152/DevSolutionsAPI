package com.devsolutions.DevSolutionsAPI.Controllers.OwnerControllers;

import com.devsolutions.DevSolutionsAPI.Entities.Users;
import com.devsolutions.DevSolutionsAPI.Enums.UserRole;
import com.devsolutions.DevSolutionsAPI.Services.UserService;
import com.devsolutions.DevSolutionsAPI.Tools.CheckCookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/owner")
public class OwnerUsersController {
    private final UserService userService;
    private final CheckCookie checkCookie;

    @Autowired
    public OwnerUsersController(UserService userService){
        this.userService = userService;
        this.checkCookie = new CheckCookie(userService);
    }

    @GetMapping("/admins")
    public ResponseEntity<Optional<List<Users>>> getAllUsers(HttpServletRequest request){
        Optional<UserRole> role = checkCookie.CheckCookieForRole(request);

        if (role.isEmpty() || role.get() != UserRole.OWNER)
            return ResponseEntity.status(401).body(Optional.empty());

        return ResponseEntity.ok(userService.getAllAdmins());
    }
}
