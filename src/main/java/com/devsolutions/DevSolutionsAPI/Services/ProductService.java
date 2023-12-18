package com.devsolutions.DevSolutionsAPI.Services;

import com.devsolutions.DevSolutionsAPI.Entities.ProductEntity;
import com.devsolutions.DevSolutionsAPI.Repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }


    public Optional<ProductEntity> getProduct(long id){
        return productRepository.findById(id);
    }

    public List<ProductEntity> getAllProducts(){
        return productRepository.findAll();
    }
}
