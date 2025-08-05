package exercise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import exercise.model.Product;

import org.springframework.data.domain.Sort;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // BEGIN
    List<Product> findAllByPriceBetween(Integer priceStart, Integer priceEnd, Sort sort);
    List<Product> findAllByPriceAfter(int priceAfter, Sort sort);
    List<Product> findAllByPriceBefore(int priceAfter, Sort sort);
    // END
}
