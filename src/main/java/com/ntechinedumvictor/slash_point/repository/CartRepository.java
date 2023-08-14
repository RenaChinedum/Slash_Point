package com.ntechinedumvictor.slash_point.repository;

import com.ntechinedumvictor.slash_point.model.Cart;
import com.ntechinedumvictor.slash_point.model.Products;
import com.ntechinedumvictor.slash_point.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

    Cart findByUserIdAndProductsId(int user_id, int products_id);

    void deleteCartByUserIdAndProductsId(int userID, int productsID);

    void deleteAllByUserId(int userID);
    Optional<Cart> findCartByProductsAndUser(Products products, User user);

    @Query(
            value = "select * from Cart c where user_id=?1",
            nativeQuery = true
    )
    List<Cart> findAllCartByUser(int userID);

    Cart findCartById(int cartID);

    @Modifying
    @Query(value = "UPDATE cart SET quantity = ?1 WHERE product_id = ?2 AND user_id = ?3", nativeQuery = true)
    int updateCartQuantity(int quantity, int productId, int userId);

}
