package com.backend.linzanova.dao.drops;

import com.backend.linzanova.entity.drops.Drops;
import com.backend.linzanova.entity.lens.Lens;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class DropsSpecification {
    public static Specification<Drops> byColumnNameAndValue(String columnName, String value) {
        return new Specification<Drops>() {
            @Override
            public Predicate toPredicate(Root<Drops> root,
                                         CriteriaQuery<?> query, CriteriaBuilder builder) {
                return builder.equal(root.<String>get(columnName), value);
            }
        };
    }
}
