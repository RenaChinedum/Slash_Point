package com.ntechinedumvictor.slash_point.services;




import com.ntechinedumvictor.slash_point.dto.WishlistDTO;

import java.util.List;

public interface WishlistService {
    boolean addToWishlist(int user_id, int product_id);
    List<WishlistDTO> viewWishlist(int user_id);


    boolean addToCart(int wishlist_id);

    int totalWishlist(int userID);

    void deleteFromWishlist(int wishlist_id);

}
