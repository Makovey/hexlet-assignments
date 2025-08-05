package exercise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.data.domain.Sort;

import java.util.List;

import exercise.model.Product;
import exercise.repository.ProductRepository;
import exercise.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductRepository productRepository;

    // BEGIN
    @GetMapping
    ResponseEntity<List<Product>> getFilteredProducts(
            @RequestParam(required = false) Integer min,
            @RequestParam(required = false) Integer max
    ) {
        if (min == null && max == null) {
            var products = productRepository.findAll();
            return ResponseEntity
                    .ok()
                    .body(products);
        }

        if (min != null && max != null) {
            var products = productRepository.findAllByPriceBetween(min, max, Sort.by("price").ascending());
            return ResponseEntity
                    .ok()
                    .body(products);
        }

        if (min != null) {
            var products = productRepository.findAllByPriceAfter(min, Sort.by("price").ascending());
            return ResponseEntity
                    .ok()
                    .body(products);
        }

        var products = productRepository.findAllByPriceBefore(max, Sort.by("price").ascending());
        return ResponseEntity
                .ok()
                .body(products);
    }
    // END

    @GetMapping(path = "/{id}")
    public Product show(@PathVariable long id) {

        var product =  productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));

        return product;
    }
}
