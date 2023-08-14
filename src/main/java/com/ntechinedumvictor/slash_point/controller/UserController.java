package com.ntechinedumvictor.slash_point.controller;

import com.ntechinedumvictor.slash_point.dto.UserDTO;
import com.ntechinedumvictor.slash_point.enums.Role;
import com.ntechinedumvictor.slash_point.model.Products;
import com.ntechinedumvictor.slash_point.model.User;
import com.ntechinedumvictor.slash_point.repository.UserRepository;
import com.ntechinedumvictor.slash_point.services.AdminService;
import com.ntechinedumvictor.slash_point.services.CartService;
import com.ntechinedumvictor.slash_point.services.UserService;
import com.ntechinedumvictor.slash_point.services.WishlistService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;
    private final AdminService adminService;
    private final CartService cartService;

    private final UserRepository userRepository;
    private final WishlistService wishlistService;

    @GetMapping("/")
    public String renderHome( Model model, HttpServletRequest request){
        model.addAttribute("productList", adminService.listProducts());
        model.addAttribute("user", new UserDTO());
        HttpSession session = request.getSession();
        if(session.getAttribute("userID") != null){
            int userID= (int) session.getAttribute("userID");
            model.addAttribute("userID", userID);
            model.addAttribute("lastName", session.getAttribute("lastName"));
            model.addAttribute("email", session.getAttribute("email"));
            model.addAttribute("totalCartItem",cartService.totalCartItems(userID));
            model.addAttribute("totalWishlistItem", wishlistService.totalWishlist(userID));
            model.addAttribute("wishList", wishlistService.viewWishlist(userID));
        }
        return "index";
    }

    @GetMapping("/register")
    public ModelAndView registrationPage(ModelAndView modelAndView, HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session.getAttribute("userEmail") != null) {
            modelAndView.setViewName("redirect:/");
            modelAndView.addObject("session", session.getAttribute("userEmail"));
        } else {
            modelAndView.setViewName("registration");
            modelAndView.addObject("user", new UserDTO());
        }
        return modelAndView;
    }

    @PostMapping("/signup")
    public ModelAndView signup(@ModelAttribute("user") UserDTO user, ModelAndView modelAndView) {
        try {
            userService.createNewUser(user);
            modelAndView.setViewName("login");
            modelAndView.addObject("successMessage", "Sign up successful. Please login.");
        }catch (RuntimeException e){
            modelAndView.setViewName("registration");
            modelAndView.addObject("errorMessage", e.getMessage());
        }

        // check if the email already exists in the database
        return modelAndView;
    }


    @GetMapping("/login")
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        modelAndView.addObject("user", new UserDTO());
        return modelAndView;
    }

    @PostMapping("/login")
    public ModelAndView loginUser(@ModelAttribute("user") UserDTO userDTO, HttpServletRequest request) {
        ModelAndView model = new ModelAndView();
        try {
            boolean loggedIn = userService.loginUser(userDTO.getEmail(), userDTO.getPassword(), request);
            if (loggedIn) {
                Role role = Role.ADMIN;
                Role userRole = userDTO.getRole();
                if (userRole != null && userRole.equals(role)) {
                    model.addObject("status", "Login Successful");
                    model.setViewName("redirect:/admin");
                } else {
                    model.addObject("status", "Login Successful");
                    model.setViewName("redirect:/");
                }
            } else {
                model.addObject("errorMessage", "Invalid email or password.");
                model.setViewName("login");
            }


        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return model;
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession();

        if (session.getAttribute("userID") != null) {
            session.invalidate();
        }
        return "redirect:/";
    }

}


