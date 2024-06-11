package ro.foodx.backend.repository;

import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import ro.foodx.backend.model.store.Product;
import ro.foodx.backend.model.store.Store;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StoreSpecifications {
    public static Specification<Store> byFilter(Map<String, String> filterMap) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            filterMap.forEach((key, value) -> {
                if ("storeName".equals(key) && value != null && !value.trim().isEmpty()) {
                    String escapedValue = StringUtils.replace(value.trim(), "!", "!!")
                            .replace("%", "!%")
                            .replace("_", "!_")
                            .replace("[", "![");
                    predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("storeName")), "%" + escapedValue.toLowerCase() + "%", '!'));
                }
                /*
                switch (key) {
                    case "bagType":
                        predicates.add(criteriaBuilder.equal(root.get(key), value));
                        break;
                    case "isPublished":
                        predicates.add(criteriaBuilder.equal(root.get(key), Boolean.valueOf(value)));
                        break;
                } */
            });
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
