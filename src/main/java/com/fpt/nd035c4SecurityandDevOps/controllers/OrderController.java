package com.fpt.nd035c4SecurityandDevOps.controllers;
import java.util.LinkedList;
import java.util.List;

import com.fpt.nd035c4SecurityandDevOps.model.persistence.Cart;
import com.fpt.nd035c4SecurityandDevOps.model.persistence.User;
import com.fpt.nd035c4SecurityandDevOps.model.persistence.UserOrder;
import com.fpt.nd035c4SecurityandDevOps.model.persistence.repositories.OrderRepository;
import com.fpt.nd035c4SecurityandDevOps.model.persistence.repositories.UserRepository;
import com.fpt.nd035c4SecurityandDevOps.model.responses.ItemResponse;
import com.fpt.nd035c4SecurityandDevOps.model.responses.UserOrderResponse;
import com.fpt.nd035c4SecurityandDevOps.model.responses.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
public class OrderController {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @PostMapping("/submit/{username}")
    public ResponseEntity<UserOrderResponse> submit(@PathVariable String username) {
        User user = userRepository.findByUsername(username);
        if(user == null) {
            return ResponseEntity.notFound().build();
        }
        UserOrder order = UserOrder.createFromCart(user.getCart());
        orderRepository.save(order);
        UserOrderResponse userOrderResponse = new UserOrderResponse();
        userOrderResponse.setUserOrderId(order.getId());
        UserResponse userResponse = new UserResponse();
        userResponse.setIdUser(user.getId());
        userResponse.setUsername(user.getUsername());
        userOrderResponse.setUser(userResponse);
        List<ItemResponse> itemResponses = new LinkedList<>();
        if (order.getItems() != null){
            order.getItems().stream().forEach((el) -> {
                ItemResponse itemResponse = new ItemResponse();
                itemResponse.setId(el.getId());
                itemResponse.setDescription(el.getDescription());
                itemResponse.setName(el.getName());
                itemResponse.setPrice(el.getPrice());
                itemResponses.add(itemResponse);
            } );
        }
        userOrderResponse.setItems(itemResponses);
        userOrderResponse.setTotal(order.getTotal());
        return ResponseEntity.ok(userOrderResponse);
    }

    @GetMapping("/history/{username}")
    public ResponseEntity<List<UserOrderResponse>> getOrdersForUser(@PathVariable String username) {
        User user = userRepository.findByUsername(username);
        if(user == null) {
            return ResponseEntity.notFound().build();
        }
        UserResponse userResponse = new UserResponse();
        if (user != null){
            userResponse.setIdUser(user.getId());
            userResponse.setUsername(user.getUsername());
        }
        List<UserOrder> dbHistoryOrder = orderRepository.findByUser(user);
        List<UserOrderResponse> historyOrder = new LinkedList<>();
        dbHistoryOrder.stream().forEach((el)->{
            UserOrderResponse userOrderResponse = new UserOrderResponse();
            userOrderResponse.setUser(userResponse);
            userOrderResponse.setTotal(el.getTotal());
            userOrderResponse.setUserOrderId(el.getId());
            List<ItemResponse> itemResponses = new LinkedList<>();
            if (el.getItems() != null){
                el.getItems().stream().forEach((item) -> {
                    ItemResponse itemResponse = new ItemResponse();
                    itemResponse.setId(item.getId());
                    itemResponse.setDescription(item.getDescription());
                    itemResponse.setName(item.getName());
                    itemResponse.setPrice(item.getPrice());
                    itemResponses.add(itemResponse);
                } );
            }
            userOrderResponse.setItems(itemResponses);
            historyOrder.add(userOrderResponse);
        });
        return ResponseEntity.ok(historyOrder);
    }
}