package com.shoppingcart.service;

import com.shoppingcart.exception.ResourceNotFoundException;
import com.shoppingcart.model.Product;
import com.shoppingcart.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts(String category, String search) {
        if (category != null && !category.trim().isEmpty() && !category.equalsIgnoreCase("All")) {
            return productRepository.findByCategoryIgnoreCase(category.trim());
        }
        if (search != null && !search.trim().isEmpty()) {
            String q = search.trim();
            return productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(q, q);
        }
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    public List<String> getAllCategories() {
        return productRepository.findDistinctCategories();
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }
}
