package com.spsa.strategy.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.spsa.strategy.model.Authoritygoals;
import com.spsa.strategy.model.Departmentgoals;
import com.spsa.strategy.model.Restrictedgoalsuserlevel;
import com.spsa.strategy.model.Sectiongoals;

import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

public class JPASpecification {
	
	public static Specification<Authoritygoals> returnAuthoritygoalSpecification(String search, String sortColumn, boolean descending, String username, String currrentuserrole) {
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
	      
	      // Query to check that goalId in Authoritygoals is not in restrictedgoals
			Subquery<String> subquery = query.subquery(String.class);  // Subquery to select goalIds from restrictedgoals
			Root<Restrictedgoalsuserlevel> restrictedGoalsRoot = subquery.from(Restrictedgoalsuserlevel.class);  // From restrictedgoals table
			subquery.select(restrictedGoalsRoot.get("goalid"));  // Selecting goalId from restrictedgoals

			// Add the condition where userrole in restrictedgoals should match the provided userrole
	        Predicate userroleCondition = criteriaBuilder.equal(restrictedGoalsRoot.get("userrole"), currrentuserrole);
	        subquery.where(userroleCondition);  // Adding userrole filter to the subquery
			
			// Predicate to exclude goals in restrictedgoals
			Predicate notInRestrictedGoals = criteriaBuilder.not(criteriaBuilder.in(root.get("id")).value(subquery));
			
			// Add this condition to the AND predicates list
			andPredicates.add(notInRestrictedGoals);
	        
	      // Combine all AND predicates with an AND condition
	      return criteriaBuilder.and(andPredicates.toArray(new Predicate[0]));
	  };
	}

	public static Specification<Departmentgoals> returnDepartmentgoalSpecification(String search, String sortColumn,
			boolean descending, String goalid, String currrentuserrole) {
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

		      // Query to check that goalId in Authoritygoals is not in restrictedgoals
				Subquery<String> subquery = query.subquery(String.class);  // Subquery to select goalIds from restrictedgoals
				Root<Restrictedgoalsuserlevel> restrictedGoalsRoot = subquery.from(Restrictedgoalsuserlevel.class);  // From restrictedgoals table
				subquery.select(restrictedGoalsRoot.get("goalid"));  // Selecting goalId from restrictedgoals
				
				// Add the condition where userrole in restrictedgoals should match the provided userrole
		        Predicate userroleCondition = criteriaBuilder.equal(restrictedGoalsRoot.get("userrole"), currrentuserrole);
		        subquery.where(userroleCondition);  // Adding userrole filter to the subquery
				
				// Predicate to exclude goals in restrictedgoals
				Predicate notInRestrictedGoals = criteriaBuilder.not(criteriaBuilder.in(root.get("id")).value(subquery));
				
				// Add this condition to the AND predicates list
				andPredicates.add(notInRestrictedGoals);
		
		      // Combine all AND predicates with an AND condition
		      return criteriaBuilder.and(andPredicates.toArray(new Predicate[0]));
		  };
	}

	public static Specification<Sectiongoals> returnSectiongoalSpecification(String search, String sortColumn, Boolean descending, String goalid) {

		 return (root, query, criteriaBuilder) -> {
		      List<Predicate> andPredicates = new ArrayList<>(); // For AND conditions
		
		      if (descending) 
		          query.orderBy(criteriaBuilder.desc(root.get(sortColumn)));
		      else 
		          query.orderBy(criteriaBuilder.asc(root.get(sortColumn)));
		      
		      andPredicates.add(criteriaBuilder.equal(root.get("depgoalid"), goalid));
		      
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