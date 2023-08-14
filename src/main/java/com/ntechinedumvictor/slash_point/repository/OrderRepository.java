package com.ntechinedumvictor.slash_point.repository;

import com.ntechinedumvictor.slash_point.model.Order;
import com.ntechinedumvictor.slash_point.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {
 @Query(value = "select * from `order` where user_id = ?1"
         ,nativeQuery = true)
 List<Order> findAllByOrder_id(int userID);

 List<Order> findAllByUser(int userID);
  Order findAllByUserIdAndCartId(int userID, int cartId);

}
