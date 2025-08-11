package exercise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.util.List;

import exercise.repository.ProductRepository;
import exercise.dto.ProductDTO;
import exercise.dto.ProductCreateDTO;
import exercise.dto.ProductUpdateDTO;
import exercise.exception.ResourceNotFoundException;
import exercise.mapper.ProductMapper;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    // BEGIN
    @GetMapping
    ResponseEntity<List<ProductDTO>> getAllProducts() {
        var products = productRepository
                .findAll()
                .stream()
                .map(productMapper::toProductDTO)
                .toList();

        return ResponseEntity
                .ok(products);
    }

    @GetMapping("/{id}")
    ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        var product = productRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));

        return  ResponseEntity
                .ok(productMapper.toProductDTO(product));
    }

    @PostMapping
    ResponseEntity<ProductDTO> createProduct(
            @RequestBody ProductCreateDTO productCreateDTO
    ) {
        var product = productRepository.save(productMapper.toProductFromCreate(productCreateDTO));
        return  ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productMapper.toProductDTO(product));
    }

    @PutMapping("/{id}")
    ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductUpdateDTO productUpdateDTO
    ) {
        var product = productRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));

        product.setCost(productUpdateDTO.getPrice());
        productRepository.save(product);

        return  ResponseEntity
                .ok(productMapper.toProductDTO(product));
    }
    // END
}
