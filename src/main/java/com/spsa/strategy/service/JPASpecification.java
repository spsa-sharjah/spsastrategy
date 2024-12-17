package com.spsa.strategy.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.spsa.strategy.model.Authoritygoals;
import com.spsa.strategy.model.Departmentgoals;

import jakarta.persistence.criteria.Predicate;

public class JPASpecification {
	
	public static Specification<Authoritygoals> returnAuthoritygoalSpecification(String search, String sortColumn, boolean descending, String username) {
	  return (root, query, criteriaBuilder) -> {
	      List<Predicate> andPredicates = new ArrayList<>(); // For AND conditions
	
	      if (descending) 
	          query.orderBy(criteriaBuilder.desc(root.get(sortColumn)));
	      else 
	          query.orderBy(criteriaBuilder.asc(root.get(sortColumn)));
	      
	      if (username != null)
		      andPredicates.add(criteriaBuilder.equal(root.get("username"), username));
	      
	      if (search != null) {
	          String searchPattern =  "%" + search + "%";
	          // Combine OR predicates into one OR condition
	          Predicate orCondition = criteriaBuilder.or(
	                  criteriaBuilder.like(root.get("username"), searchPattern),
	                  criteriaBuilder.like(root.get("goal"), searchPattern)
	              );
	
	          // Add the OR condition to the list of AND predicates
	          andPredicates.add(orCondition);
	      }
	
	      // Combine all AND predicates with an AND condition
	      return criteriaBuilder.and(andPredicates.toArray(new Predicate[0]));
	  };
	}

	public static Specification<Departmentgoals> returnDepartmentgoalSpecification(String search, String sortColumn,
			boolean descending, String goalid) {
		 return (root, query, criteriaBuilder) -> {
		      List<Predicate> andPredicates = new ArrayList<>(); // For AND conditions
		
		      if (descending) 
		          query.orderBy(criteriaBuilder.desc(root.get(sortColumn)));
		      else 
		          query.orderBy(criteriaBuilder.asc(root.get(sortColumn)));
		      
		      andPredicates.add(criteriaBuilder.equal(root.get("authgoalid"), goalid));
		      
		      if (search != null) {
		          String searchPattern =  "%" + search + "%";
		          // Combine OR predicates into one OR condition
		          Predicate orCondition = criteriaBuilder.or(
		                  criteriaBuilder.like(root.get("username"), searchPattern),
		                  criteriaBuilder.like(root.get("goal"), searchPattern)
		              );
		
		          // Add the OR condition to the list of AND predicates
		          andPredicates.add(orCondition);
		      }
		
		      // Combine all AND predicates with an AND condition
		      return criteriaBuilder.and(andPredicates.toArray(new Predicate[0]));
		  };
	}
}