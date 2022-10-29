package com.gameservice.outcome.specifications;

import com.gameservice.outcome.model.TableModel;
import com.gameservice.outcome.util.SpecSearchCriteria;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;

public class TableSpecification implements Specification<TableModel> {

    private final SpecSearchCriteria criteria;

    public TableSpecification(final SpecSearchCriteria criteria) {
        super();
        this.criteria = criteria;
    }

    public SpecSearchCriteria getCriteria() {
        return criteria;
    }

    @Override
    public Predicate toPredicate(@NonNull final Root<TableModel> root, @NonNull final CriteriaQuery<?> query, @NonNull final CriteriaBuilder builder) {
        switch (criteria.getOperation()) {
            case EQUALITY:
                return builder.equal(root.get(criteria.getKey()), criteria.getValue());
            case NEGATION:
                return builder.notEqual(root.get(criteria.getKey()), criteria.getValue());
            case GREATER_THAN:
                if (criteria.getValue() instanceof LocalDateTime) {
                    return builder.greaterThan(root.get(criteria.getKey()), (LocalDateTime) criteria.getValue());
                } else {
                    return builder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString());
                }
            case GREATER_THAN_EQUAL:
                if (criteria.getValue() instanceof LocalDateTime) {
                    return builder.greaterThanOrEqualTo(root.get(criteria.getKey()), (LocalDateTime) criteria.getValue());
                } else {
                    return builder.greaterThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());
                }
            case LESS_THAN:
                if (criteria.getValue() instanceof LocalDateTime) {
                    return builder.lessThan(root.get(criteria.getKey()), (LocalDateTime) criteria.getValue());
                } else {
                    return builder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString());
                }
            case LESS_THAN_EQUAL:
                if (criteria.getValue() instanceof LocalDateTime) {
                    return builder.lessThanOrEqualTo(root.get(criteria.getKey()), (LocalDateTime) criteria.getValue());
                } else {
                    return builder.lessThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());
                }
            case LIKE:
                return builder.like(root.get(criteria.getKey()), criteria.getValue().toString());
            case STARTS_WITH:
                return builder.like(root.get(criteria.getKey()), criteria.getValue() + "%");
            case ENDS_WITH:
                return builder.like(root.get(criteria.getKey()), "%" + criteria.getValue());
            case CONTAINS:
                return builder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
            default:
                return null;
        }
    }

}
