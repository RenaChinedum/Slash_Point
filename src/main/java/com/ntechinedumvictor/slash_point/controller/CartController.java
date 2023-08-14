package com.ntechinedumvictor.slash_point.controller;

import com.ntechinedumvictor.slash_point.dto.CartDTO;
import com.ntechinedumvictor.slash_point.model.Cart;
import com.ntechinedumvictor.slash_point.services.CartService;
import com.ntechinedumvictor.slash_point.services.UserService;
import com.ntechinedumvictor.slash_point.services.WishlistService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final WishlistService wishlistService;


    @GetMapping("/cart")
    public String requestCartPage(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        int userID = (int) session.getAttribute("userID");
        model.addAttribute("userID", userID);
        List<CartDTO> cartDTOList = cartService.viewCart(userID);
        model.addAttribute("cartTotal", cartService.totalCartPrice(userID));
        model.addAttribute("CartList", cartDTOList);
        model.addAttribute("totalWishlistItem", wishlistService.totalWishlist(userID));
        return "cart";
    }

    @GetMapping("/addToCart/{productID}/{userID}")
    public String AddToCart(Model model, @PathVariable("userID") int userID, @PathVariable("productID") int productID){
        System.out.println("***************" + userID + "*******" + productID);
        int quantity = 1;
        boolean addingCart = cartService.addToCart(userID, productID,quantity);
        List<CartDTO> cartDTOList = cartService.viewCart(userID);
        model.addAttribute("CartList", cartDTOList);
        return "redirect:/";
    }

    @PostMapping("/increaseCartQuantity")
    public String increaseCartQuantity(@RequestParam("productId") int productId, @RequestParam("userId") int userId) {
        cartService.increaseCartQuantity(productId, userId);
        return "redirect:/cart";
    }

    @PostMapping("/decreaseCartQuantity")
    public String decreaseCartQuantity(@RequestParam("productId") int productId, @RequestParam("userId") int userId) {
        cartService.decreaseCartQuantity(productId, userId);
        return "redirect:/cart";
    }

//    @PostMapping("/cart-total-price/{userID}")
//    public String cartPrice( Model model, @PathVariable("userID") int userID){
//        double totalPrice = cartService.totalCartPrice(userID);
//        model.addAttribute("totalPrice", totalPrice);
//        return "cart";
//    }

    @GetMapping("/removeFromCart")
    public String removeFromCart(Model model, @RequestParam int productID, @RequestParam int userID){
        cartService.deleteFromCarts(userID,productID);
        return "redirect:/cart";
    }

}
