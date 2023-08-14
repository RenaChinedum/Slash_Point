package com.ntechinedumvictor.slash_point.services.serviceImpl;


import com.ntechinedumvictor.slash_point.dto.WishlistDTO;
import com.ntechinedumvictor.slash_point.model.Products;
import com.ntechinedumvictor.slash_point.model.User;
import com.ntechinedumvictor.slash_point.model.Wishlist;
import com.ntechinedumvictor.slash_point.repository.ProductRepository;
import com.ntechinedumvictor.slash_point.repository.UserRepository;
import com.ntechinedumvictor.slash_point.repository.WishlistRepository;
import com.ntechinedumvictor.slash_point.services.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {
    private final UserRepository userRepository;
    private final WishlistRepository wishlistRepository;
    private  final ProductRepository productRepository;
    private final CartServiceImpl cartServiceImpl;

    @Override
    public boolean addToWishlist(int userID, int productID) {
        User user = userRepository.findUserById(userID).orElse(null);
        Products product = productRepository.findById(productID).orElse(null);
        if(user != null && product != null){
            Wishlist wishlist = wishlistRepository.findByUserIdAndAndProductId(userID,productID).orElse(null);
            if(wishlist == null){
                wishlistRepository.save(Wishlist.builder()
                        .user(user)
                        .product(product)
                        .build());
                return true;
            } else{
               return false;
            }
        }
        return false;
    }

    @Override
    public List<WishlistDTO> viewWishlist(int user_id) {
        User user = userRepository.findUserById(user_id).orElse(null);
        if(user != null){
            List<Wishlist> wishlistList = wishlistRepository.findAllByUserId(user_id);
            List<WishlistDTO> wishlistDTOList = new ArrayList<>();
            for(Wishlist wishlist:wishlistList){
                Products product = productRepository.findById(wishlist.getProduct().getId()).orElse(null);
                wishlistDTOList.add(WishlistDTO.builder()
                        .id(wishlist.getId())
                        .product(product)
                        .user(user)
                        .build());
            }
            return wishlistDTOList;
        }
        return null;
    }
    @Override
    public int totalWishlist(int userID){
        return viewWishlist(userID).size();
    }

    @Override
    public void deleteFromWishlist(int wishlist_id) {
        wishlistRepository.deleteById(wishlist_id);
    }

    @Override
    public boolean addToCart(int wishlist_id) {
        Wishlist wishlist = wishlistRepository.findById(wishlist_id).orElse(null);
        boolean isAddedToCart = false;
        if(wishlist != null){
            int product_id = wishlist.getProduct().getId();
            int user_id = wishlist.getUser().getId();
            int quantity = 1;
            isAddedToCart = cartServiceImpl.addToCart(user_id,product_id,quantity);

        }
        return isAddedToCart;
    }
}
