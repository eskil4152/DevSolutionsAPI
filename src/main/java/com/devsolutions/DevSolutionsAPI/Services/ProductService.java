package com.devsolutions.DevSolutionsAPI.Services;

import com.devsolutions.DevSolutionsAPI.Entities.Products;
import com.devsolutions.DevSolutionsAPI.Repositories.ProductRepository;
import com.devsolutions.DevSolutionsAPI.RequestBodies.ProductsRequest;
import jnr.ffi.annotations.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final HashMap<Long, Products> productsCache;

    @Autowired
    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
        this.productsCache = new HashMap<>();
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void clearCache(){
        productsCache.clear();
    }

    public Optional<Products> getProduct(long id){
        if (productsCache.containsKey(id)){
            return Optional.ofNullable(productsCache.get(id));
        }

        Optional<Products> products = productRepository.findById(id);

        products.ifPresent(value -> productsCache.put(value.getId(), value));

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

        try {
            productsCache.put(productRepository.findByName(products.getName()).get().getId(), products);
        } catch (Exception e){
            System.out.println("Failed to cache, " + e);
            return Optional.of(products);
        }

        return Optional.of(products);
    }

    public Optional<Products> updateProduct(ProductsRequest request) {
        Optional<Products> product = productRepository.findById(request.getId());

        if (product.isEmpty()) {
            return product;
        }

        product.get().setName(request.getProductName());
        product.get().setDescription(request.getDescription());
        product.get().setPrice(request.getPrice());

        productsCache.put(request.getId(), product.get());
        productRepository.save(product.get());

        return product;
    }

    public boolean deleteProduct(Long id){
        Optional<Products> dbProduct = productRepository.findById(id);

        if (dbProduct.isEmpty())
            return false;

        productsCache.remove(dbProduct.get().getId());
        productRepository.delete(dbProduct.get());

        return true;
    }
}
