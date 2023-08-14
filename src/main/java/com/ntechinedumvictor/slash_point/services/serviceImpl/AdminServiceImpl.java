package com.ntechinedumvictor.slash_point.services.serviceImpl;

import com.ntechinedumvictor.slash_point.dto.ProductDTO;
import com.ntechinedumvictor.slash_point.model.Products;
import com.ntechinedumvictor.slash_point.repository.ProductRepository;
import com.ntechinedumvictor.slash_point.services.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final ProductRepository productRepository;


    public List<ProductDTO> listProducts() {
        List<Products> products = productRepository.findAll();
        List<ProductDTO> productList = new ArrayList<>();
        for(Products product : products) {
            ProductDTO productDTO = getDtoFromProduct(product);
            productList.add(productDTO);
        }
        return productList;
    }

    private ProductDTO getDtoFromProduct(Products product) {

        return ProductDTO.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .price(product.getPrice())
                .category(product.getCategory())
                .imageUrl(product.getImageUrl())
                .quantity(product.getQuantity())
                .build();
    }

    public static Products getProductFromDTO(ProductDTO productDTO){
        return Products.builder()
                .id(productDTO.getId())
                .productName(productDTO.getProductName())
                .category(productDTO.getCategory())
                .imageUrl(productDTO.getImageUrl())
                .price(productDTO.getPrice())
                .quantity(productDTO.getQuantity())
                .build();
    }

    public void addProduct(ProductDTO productDTO){
        Products products = getProductFromDTO(productDTO);
        productRepository.save(products);
    }

    public void updateProduct(int productID, ProductDTO productDTO ){
        Products product = getProductFromDTO(productDTO);
        product.setId(productID);
        productRepository.save(product);

    }

    public void deleteById(int id)
    {
        productRepository.deleteById(id);
    }
    public boolean updatePrice(int productId, BigDecimal newPrice) {
        Products product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            throw new RuntimeException("Product not found with id: " + productId);
        }
        productRepository.updatePrice(productId, newPrice);
        return true;
    }
    public int updateProductQuantity(int id, int newQuantity){
        int updated = productRepository.updateQuantity(id, newQuantity);
        if(updated == 0){
            throw new RuntimeException("Not updated!");
        }
        return updated;
    }
}
