package com.ntechinedumvictor.slash_point.controller;

import com.ntechinedumvictor.slash_point.dto.OrderDTO;
import com.ntechinedumvictor.slash_point.services.CartService;
import com.ntechinedumvictor.slash_point.services.OrderService;
import com.ntechinedumvictor.slash_point.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
@Controller
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final CartService cartService;
    private final UserService userService;


    @GetMapping("/order")
    public String requestOrderPage(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        int userID = (int) session.getAttribute("userID");
        model.addAttribute("userID", userID);
        List<OrderDTO> orderDTO = orderService.viewOrder(userID);
        model.addAttribute("orderList", orderDTO);
        return "order";
    }


}
