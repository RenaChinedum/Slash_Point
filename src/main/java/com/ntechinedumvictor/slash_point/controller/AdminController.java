package com.ntechinedumvictor.slash_point.controller;

import com.ntechinedumvictor.slash_point.dto.ProductDTO;
import com.ntechinedumvictor.slash_point.model.Products;
import com.ntechinedumvictor.slash_point.repository.ProductRepository;
import com.ntechinedumvictor.slash_point.services.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Controller
public class AdminController {
    AdminService adminService;
    ProductRepository productRepository;


    @Autowired
    public AdminController(AdminService adminService, ProductRepository productRepository) {
        this.adminService = adminService;
        this.productRepository = productRepository;
    }
    @GetMapping("/admin")
    public ModelAndView home(HttpServletRequest request){
        HttpSession session = request.getSession();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin");
        modelAndView.addObject("products", new ProductDTO());
        List<ProductDTO> productList = adminService.listProducts();
        int id = (int) session.getAttribute("userID");
        modelAndView.addObject("productList", productList);
        modelAndView.addObject("userID", id);
        return modelAndView;
    }


    @PostMapping("/addProduct")
    public ModelAndView addProduct(@ModelAttribute("products") ProductDTO product,
                                   BindingResult bindingResult, @RequestParam("imageUrl") MultipartFile image,
                                   ModelAndView modelAndView) {
        if (!Objects.requireNonNull(image.getContentType()).startsWith("image/")) {
            bindingResult.rejectValue("imageUrl", "file.type.invalid", "File must be an image");
            modelAndView.setViewName("admin");
            return modelAndView;
        }
        try {
            byte[] imageBytes = image.getBytes();
            product.setImageUrl(imageBytes);
            adminService.addProduct(product);
            modelAndView.addObject("message", "Product added successfully");
        } catch (Exception e) {
            // log the error using a logging framework instead of printing to console
//            logger.error("Error adding product", e);
            modelAndView.addObject("error", "Failed to add product");
        }
        modelAndView.addObject("products", new ProductDTO());
        List<ProductDTO> productList = adminService.listProducts();
        modelAndView.addObject("productList", productList);
        modelAndView.setViewName("admin");
        return modelAndView;
    }

    @GetMapping("/product/image/{id}")
    public ResponseEntity<byte[]> getProductImage(@PathVariable Integer id) {
        Products product = productRepository.findById(id).orElse(null);
        if (product == null || product.getImageUrl() == null) {
            return ResponseEntity.notFound().build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentLength(product.getImageUrl().length);
        System.out.println(product.getImageUrl().length);
        return new ResponseEntity<>(product.getImageUrl(), headers, HttpStatus.OK);
    }



    @PostMapping("/price_update")
    public ModelAndView updateProductPrice(@PathVariable int productId, @RequestParam BigDecimal newPrice) {
        ModelAndView modelAndView = new ModelAndView();
        boolean updated = adminService.updatePrice(productId, newPrice);
        if(updated) {
            modelAndView.addObject("message", "Product price updated successfully");
            modelAndView.setViewName("/admin");
        }else {
            modelAndView.addObject("error", "Update failed");
            modelAndView.setViewName("/admin");
        }
        modelAndView.addObject("products", new ProductDTO());
        List<ProductDTO> productList = adminService.listProducts();
        modelAndView.addObject("productList", productList);
        return modelAndView;
    }
    @PostMapping("/quantity_update")
    public ModelAndView updateProductQuantity(@PathVariable int productId, @RequestParam int newQuantity) {
        ModelAndView modelAndView = new ModelAndView();
        int updated = adminService.updateProductQuantity(productId, newQuantity);
        if(updated > 0) {
            modelAndView.addObject("message", "Product price updated successfully");
        }else {
            modelAndView.addObject("error", "Update failed");
        }
        modelAndView.addObject("products", new ProductDTO());
        List<ProductDTO> productList = adminService.listProducts();
        modelAndView.addObject("productList", productList);
        modelAndView.setViewName("/admin");
        return modelAndView;
    }
    @PostMapping("/remove_product")
    public String removeProduct(@ModelAttribute("productDTO") ProductDTO productDTO){
        adminService.deleteById(productDTO.getId());
        return "redirect:/admin";
    }

}
