package com.fpt.nd035c4SecurityandDevOps.model.persistence;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "`cart`")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @ManyToMany
    @Column
    private List<Item> items;

    @OneToOne(mappedBy = "cart")
    private User user;

    @Column
    private BigDecimal total;

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void addItem(Item item) {
        if(items == null) {
            items = new ArrayList<>();
        }
        items.add(item);
        if(total == null) {
            total = new BigDecimal(0);
        }
        total = total.add(item.getPrice());
    }

    public void removeItem(Item item) {
        if(items == null) {
            items = new ArrayList<>();
        }
        items.remove(item);
        if(total == null) {
            total = new BigDecimal(0);
        }
        total = total.subtract(item.getPrice());
    }
}