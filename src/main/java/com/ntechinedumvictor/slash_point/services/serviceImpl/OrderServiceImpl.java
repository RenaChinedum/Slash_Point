package com.ntechinedumvictor.slash_point.services.serviceImpl;

import com.ntechinedumvictor.slash_point.dto.OrderDTO;
import com.ntechinedumvictor.slash_point.enums.Status;
import com.ntechinedumvictor.slash_point.model.Cart;
import com.ntechinedumvictor.slash_point.model.Order;
import com.ntechinedumvictor.slash_point.model.User;
import com.ntechinedumvictor.slash_point.repository.CartRepository;
import com.ntechinedumvictor.slash_point.repository.OrderRepository;
import com.ntechinedumvictor.slash_point.repository.ProductRepository;
import com.ntechinedumvictor.slash_point.repository.UserRepository;
import com.ntechinedumvictor.slash_point.services.CartService;
import com.ntechinedumvictor.slash_point.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    @Override
    public List<OrderDTO> viewOrder(int userID){
        User user = userRepository.findUserById(userID).orElse(null);
        List<Order> orderList = orderRepository.findAllByUser(userID);
        List<OrderDTO> orderDTOList = new ArrayList<>();
        for(Order order : orderList){
            orderDTOList.add(OrderDTO.builder()
                            .order_id(order.getOrder_id())
                            .quantity(order.getQuantity())
                            .status(order.getStatus())
                            .price(order.getPrice())
                            .referenceID(order.getReferenceID())
                            .amount(order.getAmount())
                            .createdAt(order.getCreatedAt())
                            .cart(order.getCart())
                            .user(user)
                    .build());
        }
        return orderDTOList;
    }

    private String generateRandomId() {
        String allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789#$";
        StringBuilder idBuilder = new StringBuilder();
        Random random = new Random();
        int length = 15;
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(allowedChars.length());
            char c = allowedChars.charAt(index);
            idBuilder.append(c);
        }
        return idBuilder.toString();
    }

    @Override
    public void addToOrder(int userID) {
        User user = userRepository.findUserById(userID).orElse(null);
        if (user == null) {
            return; // User not found
        }
        List<Cart> cartItems = cartRepository.findAllCartByUser(userID);

        for (Cart cart : cartItems) {
            Order order = orderRepository.findAllByUserIdAndCartId(userID, cart.getId());

            if (order == null) {
                Order newOrder = Order.builder()
                        .referenceID(generateRandomId()) // Define how to generate referenceID
                        .status(Status.PENDING)
                        .price(cart.getProducts().getPrice())
                        .amount(cart.getProducts().getPrice()*cart.getQuantity())
                        .cart(cart)
                        .createdAt(LocalDate.now())
                        .user(user)
                        .build();
                orderRepository.save(newOrder);
            }
        }

        cartRepository.deleteAllByUserId(userID);
    }

}
