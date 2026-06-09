package edu.unisabana.proyecto.application;

import edu.unisabana.proyecto.domain.Product;
import edu.unisabana.proyecto.domain.ProductRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepositoryPort productRepository;

    public ProductService(ProductRepositoryPort productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product getProduct(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product updateProduct(Long id, Product product) {
        Product existingProduct = getProduct(id);
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setStock(product.getStock());
        existingProduct.setCategoryId(product.getCategoryId());
        existingProduct.setLocationId(product.getLocationId());
        return productRepository.save(existingProduct);
    }

    public void deleteProduct(Long id) {
        getProduct(id); // Valida si existe
        productRepository.deleteById(id);
    }
}
