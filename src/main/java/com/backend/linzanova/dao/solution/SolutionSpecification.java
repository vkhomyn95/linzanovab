package com.backend.linzanova.dao.solution;

import com.backend.linzanova.entity.lens.Lens;
import com.backend.linzanova.entity.solution.Solution;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class SolutionSpecification {
    public static Specification<Solution> byColumnNameAndValue(String columnName, String value) {
        return new Specification<Solution>() {
            @Override
            public Predicate toPredicate(Root<Solution> root,
                                         CriteriaQuery<?> query, CriteriaBuilder builder) {
                return builder.equal(root.<String>get(columnName), value);
            }
        };
    }
}
