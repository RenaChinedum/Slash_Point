package com.ntechinedumvictor.slash_point.services;

import com.ntechinedumvictor.slash_point.dto.ProductDTO;


import java.math.BigDecimal;
import java.util.List;


public interface AdminService {


    public List<ProductDTO> listProducts();

    public void addProduct(ProductDTO productDTO);
    public void updateProduct(int productID, ProductDTO productDTO );
    public void deleteById(int id);

    public boolean updatePrice(int productId, BigDecimal newPrice);
    public int updateProductQuantity(int id, int newQuantity);

}
