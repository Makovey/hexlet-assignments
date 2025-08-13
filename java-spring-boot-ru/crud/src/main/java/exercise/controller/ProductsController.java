package exercise.controller;

import java.util.List;

import exercise.dto.ProductCreateDTO;
import exercise.dto.ProductDTO;
import exercise.dto.ProductUpdateDTO;
import exercise.exception.CategoryNotFoundException;
import exercise.mapper.ProductMapper;
import exercise.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import exercise.exception.ResourceNotFoundException;
import exercise.repository.ProductRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
public class ProductsController {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    // BEGIN
    @GetMapping
    ResponseEntity<List<ProductDTO>> getAllProducts() {
        var products = productRepository
                .findAll()
                .stream()
                .map(productMapper::map)
                .toList();

        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    ResponseEntity<ProductDTO> getProductById(
            @PathVariable Long id
    ) {
        var product = productRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " found"));

        return ResponseEntity
                .ok(productMapper.map(product));
    }

    @PostMapping
    ResponseEntity<ProductDTO> createProduct(
            @Valid @RequestBody ProductCreateDTO productCreateDTO
    ) {
        var entity = productMapper.map(productCreateDTO);
        var category = categoryRepository.findById(productCreateDTO.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category with id " + productCreateDTO.getCategoryId()));

        entity.setCategory(category);
        var product = productRepository.save(entity);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productMapper.map(product));
    }

    @PutMapping("/{id}")
    ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductUpdateDTO productUpdateDTO
    ) {
        var product = productRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " found"));

        var category = categoryRepository
                .findById(productUpdateDTO.getCategoryId().get())
                .orElseThrow(() -> new CategoryNotFoundException("Category with id " + id + " found"));

        product.setCategory(category);
        productMapper.updateProduct(productUpdateDTO, product);
        productRepository.save(product);

        return ResponseEntity
                .ok(productMapper.map(product));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteProduct(
            @PathVariable Long id
    ) {
        var product = productRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " found"));

        productRepository.delete(product);

        return ResponseEntity
                .noContent()
                .build();
    }
    // END
}
