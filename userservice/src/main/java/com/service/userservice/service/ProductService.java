package com.service.userservice.service;

import com.service.userservice.repository.ProductRepo;
import com.service.userservice.utils.ModelMapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProductService {
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private ModelMapperService modelMapperService;
    public String saveProduct(Map<String,Object> payload){
        productRepo.saveData(payload);
        return "Success";
    }

    public List<Map<String, Object>> findAll(){
        return productRepo.getAll();
    }
}
