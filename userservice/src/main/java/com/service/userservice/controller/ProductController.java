package com.service.userservice.controller;

import com.service.userservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;
    @PostMapping("/add")
    public String addProduct(@RequestBody Map<String,Object> payload){
        return productService.saveProduct(payload);
//        return "Success";
    }
    @GetMapping("/all")
    public List<Map<String, Object>> getAll(){
        return productService.findAll();
    }
}
