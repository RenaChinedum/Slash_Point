package com.ntechinedumvictor.slash_point.services;

import com.ntechinedumvictor.slash_point.dto.CartDTO;
import com.ntechinedumvictor.slash_point.model.Cart;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CartService {

    public static CartDTO getDtoFromCart(Cart cart) {

        return new CartDTO(cart);
    }

    public List<CartDTO> viewCart(int userID);


    String checkout(int userID);

    public Integer totalCartItems(int userID);
    public boolean addToCart(int userID, int productID, int quantity);

    void increaseCartQuantity(int userId, int productId);

    void decreaseCartQuantity(int userId, int productId);

    double totalCartPrice(int userID);

    public void deleteFromCarts(int userID, int productID);



}
