package com.ntechinedumvictor.slash_point.controller;

import com.ntechinedumvictor.slash_point.dto.ChangePasswordDTO;
import com.ntechinedumvictor.slash_point.dto.UserDTO;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class UserProfileController {
    private final UserService userService;
    private final AdminService adminService;
    private final CartService cartService;

    private final UserRepository userRepository;
    private final WishlistService wishlistService;

    @GetMapping("/user")
    public String renderHome(Model model, HttpServletRequest request){
        model.addAttribute("user", new UserDTO());
        HttpSession session = request.getSession();
        if(session.getAttribute("userID") != null){
            int userID= (int) session.getAttribute("userID");
            model.addAttribute("userID", userID);
            model.addAttribute("lastName", session.getAttribute("lastName"));
            model.addAttribute("email", session.getAttribute("email"));
            model.addAttribute("loggedInUser", userRepository.findUserById(userID));
            model.addAttribute("totalCartItem",cartService.totalCartItems(userID));
            model.addAttribute("totalWishlistItem", wishlistService.totalWishlist(userID));
            model.addAttribute("wishList", wishlistService.viewWishlist(userID));
        }
        return "user";
    }

    @GetMapping("/update-profile")
    public ModelAndView editProfilePage(ModelAndView modelAndView, HttpServletRequest request) {
        HttpSession session = request.getSession();
        int userID = (int)session.getAttribute("userID");
        if (session.getAttribute("userID") != null) {
            modelAndView.setViewName("edit-user");
            modelAndView.addObject("user", new UserDTO());
        } else {
            modelAndView.setViewName("login");
            modelAndView.addObject("status", "Login First to edit profile");
        }
        return modelAndView;
    }

    @PostMapping("/update-user-profile")
    public String updateProfile(@ModelAttribute("user") UserDTO user, HttpServletRequest request, Model model) {
            userService.updateProfile(user,request);
            model.addAttribute("status", "Profile update successful.");
           return "user";
    }

    @PostMapping("/upload-profile-image")
    public ModelAndView uploadProfilePicture(
            UserDTO userDTO,
            @RequestParam("userImage") MultipartFile image,
             BindingResult bindingResult,
             HttpServletRequest request,
            ModelAndView modelAndView
    ) throws IOException {
        if (!Objects.requireNonNull(image.getContentType()).startsWith("image/")) {
            bindingResult.rejectValue("imageUrl", "file.type.invalid", "File must be an image");
            modelAndView.setViewName("user");
            return modelAndView;
        }
        userService.updateProfilePicture(userDTO,image,request);
        modelAndView.addObject("message", "Product added successfully");
        modelAndView.setViewName("user");

        return modelAndView;
    }

    @GetMapping("change-password-page")
    public ModelAndView changePasswordPage(ModelAndView modelAndView, HttpServletRequest request) {
        HttpSession session = request.getSession();
        int userID = (int)session.getAttribute("userID");
        if (session.getAttribute("userID") != null) {
            modelAndView.setViewName("change-password-page");
            modelAndView.addObject("user", new ChangePasswordDTO());
        } else {
            modelAndView.setViewName("login");
            modelAndView.addObject("message", "Login to change password");
        }
        return modelAndView;
    }

    @PostMapping("/change-password")
    public String changePasswordSubmit(@ModelAttribute("userDTO")ChangePasswordDTO passwordDTO,
                                       BindingResult bindingResult,
                                       HttpServletRequest request,
                                       RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "change-password-page";
        }

        try {
            User updatedUser = userService.changePassword(passwordDTO, request);
            redirectAttributes.addFlashAttribute("successMessage", "Password changed successfully. Please log in again.");
            return "redirect:/login";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/change-password";
        }
    }

    @GetMapping("/user-image/{userID}")
    public ResponseEntity<byte[]> getUserProfileImage(@PathVariable int userID) {
        User user = userRepository.findUserById(userID).orElse(null);
        if (user == null || user.getUserImage() == null) {
            return ResponseEntity.notFound().build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentLength(user.getUserImage().length);
        return new ResponseEntity<>(user.getUserImage(), headers, HttpStatus.OK);
    }

    @GetMapping("delete-account")
    public String deleteAccount(UserDTO userDTO, HttpServletRequest request){
    userService.deleteAccount(userDTO,request);
    return "redirect:/";
    }

}
