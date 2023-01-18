package com.grigorev.srm.controller;

import com.grigorev.srm.entity.Role;
import com.grigorev.srm.entity.User;
import com.grigorev.srm.service.DepartmentService;
import com.grigorev.srm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {

    private final UserService userService;

    private final DepartmentService departmentService;

    @Autowired
    public TestController(UserService userService, DepartmentService departmentService) {
        this.userService = userService;
        this.departmentService = departmentService;
    }

    private static final List<User> USERS = Arrays.asList(
            new User(1, Role.ADMIN, "tom", "tom"),
            new User(2, Role.USER, "ben", "ben"),
            new User(3, Role.USER, "marry", "marry")
    );

    @GetMapping("hello")
    public String test() {
//        return departmentService.findById(0).toString();
        return userService.findByUsername("qwe").toString();
    }

    @GetMapping("user/{userId}")
    public User getUser(@PathVariable("userId") Integer userId) {
        return USERS.stream()
                .filter(user -> userId.equals(user.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("User " + userId + " not exist"));
    }
}
