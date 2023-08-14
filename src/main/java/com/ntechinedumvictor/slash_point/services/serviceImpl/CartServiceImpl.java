package com.ntechinedumvictor.slash_point.services.serviceImpl;

import com.ntechinedumvictor.slash_point.dto.CartDTO;
import com.ntechinedumvictor.slash_point.model.Cart;
import com.ntechinedumvictor.slash_point.model.Products;
import com.ntechinedumvictor.slash_point.model.User;
import com.ntechinedumvictor.slash_point.payment.Customer;
import com.ntechinedumvictor.slash_point.payment.PaymentRequest;
import com.ntechinedumvictor.slash_point.repository.CartRepository;
import com.ntechinedumvictor.slash_point.repository.ProductRepository;
import com.ntechinedumvictor.slash_point.repository.UserRepository;
import com.ntechinedumvictor.slash_point.services.CartService;
import com.ntechinedumvictor.slash_point.services.PaymentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PaymentService paymentService;


    public static CartDTO getDtoFromCart(Cart cart) {

        return new CartDTO(cart);
    }
    @Override
    public List<CartDTO> viewCart(int userID) {
        User user = userRepository.findUserById(userID).orElse(null);
        List<Cart> cartList = cartRepository.findAllCartByUser(userID);
        List<CartDTO> cartItems = new ArrayList<>();
        for (Cart cart : cartList) {
            Products products = productRepository.findById(cart.getProducts().getId()).orElse(null);
            cartItems.add(CartDTO.builder()
                    .id(cart.getId())
                    .user(user)
                    .products(products)
                    .quantity(cart.getQuantity())
                    .createdDate(cart.getCreatedDate())
                    .build());
        }
        return cartItems;
    }

    @Override
    public String checkout(int userID) {
        User user = userRepository.findUserById(userID).orElse(null);
        assert user != null;
        Customer customer = Customer.builder()
                .name(user.getFirstName()+" "+user.getLastName())
                .phonenumber(user.getContact())
                .email(user.getEmail())
                .build();
        double shippingFee = totalCartPrice(userID) * 0.01;
        PaymentRequest payment = PaymentRequest.builder()
                .tx_ref(UUID.randomUUID().toString())
                .customer(customer)
                .currency("NGN")
                .amount(String.valueOf(totalCartPrice(userID) + shippingFee))
                .redirect_url("https://1102.89.33.195:8080/redirect-transaction")
                .build();
        return paymentService.payment(payment);
    }



    @Override
    public Integer totalCartItems(int userID){

        return viewCart(userID).size();
    }

    @Override
    public boolean addToCart(int userID, int productID, int quantity) {
        boolean check = false;
        User user = userRepository.findUserById(userID).orElse(null);
        Optional<Products> optionalProducts = productRepository.findById(productID);
        if(optionalProducts.isPresent()){
            Products products = optionalProducts.get();
            Optional<Cart> optionalCart = cartRepository.findCartByProductsAndUser(products,user);
            Cart cart;
            if(optionalCart.isEmpty()) {
                cart = Cart.builder()
                        .user(user)
                        .products(products)
                        .createdDate(LocalDate.now())
                        .quantity(quantity)
                        .build();
            } else {
                cart = optionalCart.get();
                cart.setQuantity(cart.getQuantity()+quantity);
            }
            cartRepository.save(cart);
            check = true;
        }
        return check;
    }
    @Override
    public void increaseCartQuantity(int userId, int productId) {

        Cart cart = cartRepository.findByUserIdAndProductsId(userId,productId);
        if (cart != null) {
            System.out.println("cart = " + cart);
            int newQuantity = cart.getQuantity() + 1;
            cart.setQuantity(newQuantity);

            cartRepository.save(cart);
        }
    }
    @Override
    public void decreaseCartQuantity(int userId, int productId) {

        Cart cart = cartRepository.findByUserIdAndProductsId(productId, userId);

        if (cart != null) {
            int newQuantity = cart.getQuantity() - 1;
            // Ensure the minimum quantity is 1
            cart.setQuantity(Math.max(newQuantity, 1));

            cartRepository.save(cart);
        }
    }

    @Override
    @Transactional
    public double totalCartPrice(int userID) {
        List<CartDTO> cartDTOList = viewCart(userID);
        double cartTotal = 0.0;
        if(cartDTOList.isEmpty()){
            return cartTotal;
        }
        for(CartDTO cartDTO : cartDTOList){
            cartTotal = cartTotal + (cartDTO.getQuantity()*cartDTO.getProducts().getPrice());
        }
        return cartTotal;
    }

    @Transactional
    public void deleteFromCarts(int userID, int productID){
        cartRepository.deleteCartByUserIdAndProductsId(userID,productID);
    }
}
