package com.fpt.nd035c4SecurityandDevOps.service;

import com.fpt.nd035c4SecurityandDevOps.model.persistence.User;
import com.fpt.nd035c4SecurityandDevOps.model.requests.CreateUserRequest;
import com.fpt.nd035c4SecurityandDevOps.model.responses.UserResponse;

public interface UserService {
    User createUser(CreateUserRequest createUserRequest);
    UserResponse findByUserName(String username);
    UserResponse findById(Long id);
}
