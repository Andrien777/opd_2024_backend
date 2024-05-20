package com.opd.phonenumberapi.entity;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.CriteriaQuery;


import org.springframework.data.jpa.domain.Specification;

public class UserSpecification implements Specification<User> {
    private final SearchCriteria criteria;

    public UserSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder){
        if(criteria.getOperation().equals("=")){
            return builder.like(root.get(criteria.getKey()),"%"+criteria.getValue()+"%");
        }
        return null;
    }
}


