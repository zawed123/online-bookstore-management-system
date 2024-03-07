package com.aequalis.entity;

import java.io.Serializable;
import java.util.Objects;

public class ShoppingCartId implements Serializable {

    private static final long serialVersionUID = 1L;

    private UserInfo userInfo;

    private Book book;

    public ShoppingCartId(UserInfo userInfo, Book book) {
        this.userInfo = userInfo;
        this.book = book;
    }

    public ShoppingCartId() {
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userInfo.getName(),book.getId());
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(!(obj instanceof ShoppingCart))
            return false;
        ShoppingCart shoppingCart = (ShoppingCart)obj;
        return ((this.getBook().getId() == shoppingCart.getBook().getId()) && (this.getUserInfo().getName().equals(shoppingCart.getUserInfo().getName())));
    }
}
