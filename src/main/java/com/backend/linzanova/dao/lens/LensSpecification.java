package com.backend.linzanova.dao.lens;

import com.backend.linzanova.entity.lens.Lens;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class LensSpecification {
    public static Specification<Lens> byColumnNameAndValue(String columnName, String value) {
        return new Specification<Lens>() {
            @Override
            public Predicate toPredicate(Root<Lens> root,
                                         CriteriaQuery<?> query, CriteriaBuilder builder) {
                return builder.equal(root.<String>get(columnName), value);
            }
        };
    }
}
