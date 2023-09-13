package com.fpt.nd035c4SecurityandDevOps.controllers;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import com.fpt.nd035c4SecurityandDevOps.model.persistence.Cart;
import com.fpt.nd035c4SecurityandDevOps.model.persistence.Item;
import com.fpt.nd035c4SecurityandDevOps.model.persistence.User;
import com.fpt.nd035c4SecurityandDevOps.model.persistence.repositories.CartRepository;
import com.fpt.nd035c4SecurityandDevOps.model.persistence.repositories.ItemRepository;
import com.fpt.nd035c4SecurityandDevOps.model.persistence.repositories.UserRepository;
import com.fpt.nd035c4SecurityandDevOps.model.requests.ModifyCartRequest;
import com.fpt.nd035c4SecurityandDevOps.model.responses.CartResponse;
import com.fpt.nd035c4SecurityandDevOps.model.responses.ItemResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ItemRepository itemRepository;

    @PostMapping("/addToCart")
    public ResponseEntity<CartResponse> addToCart(@RequestBody ModifyCartRequest request) {
        CartResponse cartResponse = new CartResponse();
        User user = userRepository.findByUsername(request.getUsername());
        if(user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Optional<Item> item = itemRepository.findById(request.getItemId());
        if(!item.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Cart cart = user.getCart();
        IntStream.range(0, request.getQuantity())
                .forEach(i -> cart.addItem(item.get()));
        cartRepository.save(cart);
        cartResponse.setIdCard(cart.getId());
        cartResponse.setIdUser(user.getId());
        cartResponse.setUsername(user.getUsername());
        List<ItemResponse> itemResponses = new LinkedList<>();
        if (cart.getItems() != null){
            cart.getItems().stream().forEach((el) -> {
                ItemResponse itemResponse = new ItemResponse();
                itemResponse.setId(el.getId());
                itemResponse.setDescription(el.getDescription());
                itemResponse.setName(el.getName());
                itemResponse.setPrice(el.getPrice());
                itemResponses.add(itemResponse);
            } );
        }
        cartResponse.setItemsCard(itemResponses);
        cartResponse.setTotalCard(cart.getTotal());
        return ResponseEntity.ok(cartResponse);
    }

    @PostMapping("/modifyItemOfCart")
    public ResponseEntity<CartResponse> modifyItemOfCart(@RequestBody ModifyCartRequest request) {
        CartResponse cartResponse = new CartResponse();
        User user = userRepository.findByUsername(request.getUsername());
        if(user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Optional<Item> item = itemRepository.findById(request.getItemId());
        if(!item.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Cart cart = user.getCart();
        IntStream.range(0, request.getQuantity())
                .forEach(i -> cart.removeItem(item.get()));
        cartRepository.save(cart);
        cartResponse.setIdCard(cart.getId());
        cartResponse.setIdUser(user.getId());
        cartResponse.setUsername(user.getUsername());
        List<ItemResponse> itemResponses = new LinkedList<>();
        if (cart.getItems() != null){
            cart.getItems().stream().forEach((el) -> {
                ItemResponse itemResponse = new ItemResponse();
                itemResponse.setId(el.getId());
                itemResponse.setDescription(el.getDescription());
                itemResponse.setName(el.getName());
                itemResponse.setPrice(el.getPrice());
                itemResponses.add(itemResponse);
            } );
        }
        cartResponse.setItemsCard(itemResponses);
        cartResponse.setTotalCard(cart.getTotal());
        return ResponseEntity.ok(cartResponse);
    }

}
