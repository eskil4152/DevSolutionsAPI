package com.devsolutions.DevSolutionsAPI.Services;

import com.devsolutions.DevSolutionsAPI.Entities.Products;
import com.devsolutions.DevSolutionsAPI.Repositories.ProductRepository;
import com.devsolutions.DevSolutionsAPI.RequestBodies.ProductsRequest;
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

    public Optional<Products> getProduct(long id){
        return productRepository.findById(id);
    }

    public List<Products> getAllProducts(){
        return productRepository.findAll();
    }

    public Optional<Products> saveProduct(ProductsRequest request){
        Products products = new Products(request.getProductName(), request.getDescription(), request.getPrice());

        if (productRepository.findByName(products.getName()).isPresent()) {
            return Optional.empty();
        }

        productRepository.save(products);

        return Optional.of(products);
    }

    public Optional<Products> getProductByName(String name){
        return productRepository.findByName(name);
    }

    public Optional<Products> updateProduct(ProductsRequest request) {
        Products products = new Products(request.getProductName(), request.getDescription(), request.getPrice());
        Optional<Products> dbProduct = productRepository.findByName(products.getName());

        if (dbProduct.isEmpty()) {
            return dbProduct;
        }

        productRepository.save(dbProduct.get());
        return dbProduct;
    }

    public boolean deleteProduct(Long id){
        Optional<Products> dbProduct = productRepository.findById(id);

        if (dbProduct.isEmpty())
            return false;

        productRepository.delete(dbProduct.get());
        return true;
    }
}
