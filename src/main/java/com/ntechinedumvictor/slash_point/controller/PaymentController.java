package com.ntechinedumvictor.slash_point.controller;

import com.ntechinedumvictor.slash_point.payment.VerifyPaymentRequest;
import com.ntechinedumvictor.slash_point.services.CartService;
import com.ntechinedumvictor.slash_point.services.OrderService;
import com.ntechinedumvictor.slash_point.services.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    private final CartService cartService;

    private final OrderService orderService;

    @PostMapping("/checkout")
    public String checkOut(Model model, @RequestParam("userID") int userID
                          ){
        model.addAttribute("orderLink", cartService.checkout(userID));
        model.addAttribute("cartStatus", "Redirecting");
        model.addAttribute("cartList", cartService.viewCart(userID));
        model.addAttribute("cartTotal", cartService.totalCartPrice(userID));
        model.addAttribute("userID", userID);
        return "cart";
    }

    @GetMapping("/redirect-transaction")
    public String transactionStatus(VerifyPaymentRequest verifyPaymentRequest,
                                    HttpServletRequest request,
                                    Model model){
        HttpSession session = request.getSession();

        int userID = (int)session.getAttribute("userID");
        if(verifyPaymentRequest.getStatus().equalsIgnoreCase("completed")){
            orderService.addToOrder(userID);
            model.addAttribute("cartStatus", "Thanks for Ordering");
        }else{
            model.addAttribute("cartStatus", "Failed to place order");
        }
        model.addAttribute("cartList", cartService.viewCart(userID));
        model.addAttribute("cartTotal", cartService.totalCartPrice(userID));
        model.addAttribute("userID",userID);
        return "cart";
    }




}
