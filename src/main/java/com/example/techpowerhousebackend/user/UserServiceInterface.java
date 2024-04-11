package com.example.techpowerhousebackend.user;


import com.example.techpowerhousebackend.support.RegistrationRequest;
import com.example.techpowerhousebackend.support.exceptions.UserNotFoundException;

import java.util.List;

public interface UserServiceInterface {
    List<User> findAll();
    User findByEmail(String email) throws UserNotFoundException;
    User register(RegistrationRequest registrationRequest) throws Exception;

}