package com.ntechinedumvictor.slash_point.repository;

import com.ntechinedumvictor.slash_point.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Integer> {
    @Query(
            value = "select * from wishlists where user_id = ?1",
            nativeQuery = true
    )
    List<Wishlist> findAllByUserId(int user_id);
    @Query(value = "select * from wishlists where user_id = ?1 and  product_id = ?2",
            nativeQuery = true
    )
    Optional<Wishlist> findByUserIdAndAndProductId(int user_id, int product_id);
}
