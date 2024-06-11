package ro.foodx.backend.repository;

import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import ro.foodx.backend.model.reservation.Reservation;
import ro.foodx.backend.model.store.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReservationSpecifications {
    public static Specification<Reservation> byFilter(Long storeId, Map<String, String> filterMap) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("store").get("id"), storeId));  // Add storeId to the predicates

            filterMap.forEach((key, value) -> {
            });
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
