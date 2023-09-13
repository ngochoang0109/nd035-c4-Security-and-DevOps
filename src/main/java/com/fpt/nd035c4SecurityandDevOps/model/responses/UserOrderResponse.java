package com.fpt.nd035c4SecurityandDevOps.model.responses;

import java.math.BigDecimal;
import java.util.List;

public class UserOrderResponse {
    private Long userOrderId;
    private List<ItemResponse> items;
    private UserResponse user;
    private BigDecimal total;

    public Long getUserOrderId() {
        return userOrderId;
    }

    public void setUserOrderId(Long userOrderId) {
        this.userOrderId = userOrderId;
    }

    public List<ItemResponse> getItems() {
        return items;
    }

    public void setItems(List<ItemResponse> items) {
        this.items = items;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
