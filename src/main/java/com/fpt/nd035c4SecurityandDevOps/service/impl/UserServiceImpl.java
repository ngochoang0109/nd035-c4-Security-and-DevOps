package com.fpt.nd035c4SecurityandDevOps.service.impl;

import com.fpt.nd035c4SecurityandDevOps.model.persistence.Cart;
import com.fpt.nd035c4SecurityandDevOps.model.persistence.User;
import com.fpt.nd035c4SecurityandDevOps.model.persistence.repositories.CartRepository;
import com.fpt.nd035c4SecurityandDevOps.model.persistence.repositories.UserRepository;
import com.fpt.nd035c4SecurityandDevOps.model.requests.CreateUserRequest;
import com.fpt.nd035c4SecurityandDevOps.model.responses.UserResponse;
import com.fpt.nd035c4SecurityandDevOps.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public User createUser(CreateUserRequest createUserRequest){
        User user = new User();
        user.setUsername(createUserRequest.getUsername());
        Cart cart = new Cart();
        cartRepository.save(cart);
        user.setCart(cart);
        if(createUserRequest.getPassword().length()<7 ||
                !createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())){
           return null;
        }
        user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));
        userRepository.save(user);
        return user;
    }

    @Override
    public UserResponse findByUserName(String username) {
        User user = userRepository.findByUsername(username);
        UserResponse userResponse = new UserResponse();
        if (user != null){
            Cart cart = user.getCart();
            userResponse.setIdUser(user.getId());
            userResponse.setUsername(user.getUsername());
            userResponse.setIdCard(cart.getId());
            if(cart.getItems() != null && cart.getItems().size() > 0){
                userResponse.setItemsCard(cart.getItems());
                userResponse.setTotalCard(cart.getTotal());
            }
        }
        return userResponse;
    }

    @Override
    public UserResponse findById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        UserResponse userResponse = new UserResponse();
        if (optionalUser.isPresent()){
            User user = optionalUser.get();
            Cart cart = user.getCart();
            userResponse.setIdUser(user.getId());
            userResponse.setUsername(user.getUsername());
            userResponse.setIdCard(cart.getId());
            if(cart.getItems() != null && cart.getItems().size() > 0){
                userResponse.setItemsCard(cart.getItems());
                userResponse.setTotalCard(cart.getTotal());
            }
        }
        return userResponse;
    }
}