package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class UserController {
    UserDao userDao;

 public UserController(UserDao userDao){
        this.userDao = userDao;
    }
    @RequestMapping(value = "users/getAll", method = RequestMethod.GET )
    public List<User> getAllUsers(){
     return userDao.findAll();
    }



}
