package com.fpt.nd035c4SecurityandDevOps.model.responses;

import java.math.BigDecimal;
import java.util.List;

public class CartResponse {
    private long idCard;
    private long idUser;
    private String username;
    private List<ItemResponse> itemsCard;
    private BigDecimal totalCard;

    public long getIdCard() {
        return idCard;
    }

    public void setIdCard(long idCard) {
        this.idCard = idCard;
    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<ItemResponse> getItemsCard() {
        return itemsCard;
    }

    public void setItemsCard(List<ItemResponse> itemsCard) {
        this.itemsCard = itemsCard;
    }

    public BigDecimal getTotalCard() {
        return totalCard;
    }

    public void setTotalCard(BigDecimal totalCard) {
        this.totalCard = totalCard;
    }
}
