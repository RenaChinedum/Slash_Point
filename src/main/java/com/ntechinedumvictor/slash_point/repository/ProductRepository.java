package com.ntechinedumvictor.slash_point.repository;

import com.ntechinedumvictor.slash_point.model.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface ProductRepository extends JpaRepository<Products, Integer> {

    @Modifying
    @Query(value= "update products set price = ? where id = ?",
            nativeQuery = true)
    void updatePrice(int id, BigDecimal price);

    @Modifying
    @Query(value = "update product set quantity =? where id = ?", nativeQuery = true)
    int updateQuantity(int id, int quantity);


    @Query(value = "select * from products where id = ?1",
    nativeQuery = true)
    Products findProductsById(int id);

//    Products findProductsById(int productID);
}
