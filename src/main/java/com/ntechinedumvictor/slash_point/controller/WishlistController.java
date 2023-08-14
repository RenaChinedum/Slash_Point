package com.ntechinedumvictor.slash_point.controller;


import com.ntechinedumvictor.slash_point.services.AdminService;
import com.ntechinedumvictor.slash_point.services.CartService;
import com.ntechinedumvictor.slash_point.services.serviceImpl.WishlistServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class WishlistController {
    private final WishlistServiceImpl wishlistServiceImpl;
    private final AdminService adminService;
    private final CartService cartService;

    @GetMapping("/wishlist")
    public String viewWishlist(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        int userID = (int)session.getAttribute("userID");
        model.addAttribute("userID", userID);
        model.addAttribute("lastName",session.getAttribute("lastName"));
        model.addAttribute("wishList", wishlistServiceImpl.viewWishlist(userID));
        model.addAttribute("email", session.getAttribute("email"));
        model.addAttribute("totalCartItem",cartService.totalCartItems(userID));
        return "wishlist";
    }
    @PostMapping("/addToWishlist")
    public String addToWishlist(Model model, @RequestParam("userID") int userID, @RequestParam("productID") int productID){

        boolean isAddedToWishlist = wishlistServiceImpl.addToWishlist(userID,productID);
        if(isAddedToWishlist){
            model.addAttribute("wishlistStatus", "Product Added to your wishlist");
        }else{
            model.addAttribute("wishlistStatus","Product is already on your wishlist");
        }
        model.addAttribute("productList", adminService.listProducts());
        model.addAttribute("userID", userID);

        return "redirect:/";
    }
    @PostMapping("/deleteFromWishlist")
    public String deleteFromWishlist(Model model, @RequestParam("wishlist_id") int id,
                                     @RequestParam("session_id") int session_id,
                                     @RequestParam("session_name") String session_name){
        wishlistServiceImpl.deleteFromWishlist(id);
        model.addAttribute("wishlistStatus", "Product deleted from wishlist Successfully");
        model.addAttribute("wishlistList", wishlistServiceImpl.viewWishlist(session_id));
        model.addAttribute("session_id",session_id);
        model.addAttribute("session_name", session_name);
        return "wishlist";
    }

    @PostMapping("/addToCartFromWishlist")
    public String addToCartFromWishlist(Model model,
                                        @RequestParam("wishlist_id") int id,
                                        @RequestParam("session_id") int session_id,
                                        @RequestParam("session_name") String session_name){
        boolean isAddedToCart = wishlistServiceImpl.addToCart(id);
        if(isAddedToCart){
            model.addAttribute("wishlistStatus","Product added to Cart Successfully");
        }else{
            model.addAttribute("wishlistStatus", "Failure in adding product to cart");
        }
        model.addAttribute("wishlistList", wishlistServiceImpl.viewWishlist(session_id));
        model.addAttribute("session_id",session_id);
        model.addAttribute("session_name", session_name);
        return "wishlist";
    }
}
