package com.github.microtweak.jbx4j.descriptor.persistence.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

@Entity
public class User extends BaseEntity {

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private Customer customer;

    private String username;

    private String password;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}