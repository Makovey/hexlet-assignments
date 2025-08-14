package exercise.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import exercise.dto.ProductParamsDTO;
import exercise.model.Product;

// BEGIN
public class ProductSpecification {
    public static Specification<Product> build(ProductParamsDTO params) {
        return withCategoryId(params.getCategoryId())
                .and(withTitleCont(params.getTitleCont()))
                .and(withPriceLt(params.getPriceLt()))
                .and(withPriceGt(params.getPriceGt()))
                .and(withRatingGt(params.getRatingGt()));
    }

    private static Specification<Product> withCategoryId(Long categoryId) {
        return (root, query, cb) -> categoryId == null
                ? cb.conjunction()
                : cb.equal(root.get("category").get("id"), categoryId);
    }

    private static Specification<Product> withTitleCont(String titleCont) {
        return (root, query, cb) -> titleCont == null
                ? cb.conjunction()
                : cb.like(cb.lower(root.get("title")), "%" + titleCont.toLowerCase() + "%");
    }

    private static Specification<Product> withPriceLt(Integer priceLt) {
        return (root, query, cb) -> priceLt == null
                ? cb.conjunction()
                : cb.lessThan(root.get("price"), priceLt);
    }

    private static Specification<Product> withPriceGt(Integer priceGt) {
        return (root, query, cb) -> priceGt == null
                ? cb.conjunction()
                : cb.greaterThan(root.get("price"), priceGt);
    }

    private static Specification<Product> withRatingGt(Double ratingGt) {
        return (root, query, cb) -> ratingGt == null
                ? cb.conjunction()
                : cb.greaterThan(root.get("rating"), ratingGt);
    }
}

// END
