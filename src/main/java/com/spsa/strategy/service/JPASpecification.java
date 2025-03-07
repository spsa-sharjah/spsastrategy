package com.spsa.strategy.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.spsa.strategy.enumeration.GoalStatus;
import com.spsa.strategy.model.Authoritygoals;
import com.spsa.strategy.model.Departmentgoals;
import com.spsa.strategy.model.Evidence;
import com.spsa.strategy.model.EvidenceReply;
import com.spsa.strategy.model.Restrictedgoalsuserlevel;
import com.spsa.strategy.model.Sectiongoals;

import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

public class JPASpecification {
	
	public static Specification<Authoritygoals> returnAuthoritygoalSpecification(String search, String sortColumn, boolean descending, String username, String currrentuserrole, String year, String team, boolean showApprovedOnly) {
	  return (root, query, criteriaBuilder) -> {
	      List<Predicate> andPredicates = new ArrayList<>(); // For AND conditions
	
	      if (descending) 
	          query.orderBy(criteriaBuilder.desc(root.get(sortColumn)));
	      else 
	          query.orderBy(criteriaBuilder.asc(root.get(sortColumn)));
	      
	      if (username != null)
		      andPredicates.add(criteriaBuilder.equal(root.get("username"), username));
	      
	      if (year != null)
		      andPredicates.add(criteriaBuilder.equal(root.get("year"), year));
	      
	      if (team != null)
		      andPredicates.add(criteriaBuilder.equal(root.get("team"), team));
	      
	      if (showApprovedOnly) {
	    	  andPredicates.add(criteriaBuilder.notEqual(root.get("status"), GoalStatus.New.name()));
	    	  andPredicates.add(criteriaBuilder.notEqual(root.get("status"), GoalStatus.PartiallyEndorsed.name()));
	      }
	      
	      if (search != null) {
	          String searchPattern =  "%" + search + "%";
	          // Combine OR predicates into one OR condition
	          Predicate orCondition = criteriaBuilder.or(
	                  criteriaBuilder.like(root.get("username"), searchPattern),
	                  criteriaBuilder.like(root.get("goal"), searchPattern),
	                  criteriaBuilder.like(root.get("year"), searchPattern),
	                  criteriaBuilder.like(root.get("team"), searchPattern)
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
			boolean descending, String goalid, String currrentuserrole, String parentrole, boolean showApprovedOnly) {
		 return (root, query, criteriaBuilder) -> {
		      List<Predicate> andPredicates = new ArrayList<>(); // For AND conditions
		
		      if (descending) 
		          query.orderBy(criteriaBuilder.desc(root.get(sortColumn)));
		      else 
		          query.orderBy(criteriaBuilder.asc(root.get(sortColumn)));
		      
		      if (goalid != null)
		    	  andPredicates.add(criteriaBuilder.equal(root.get("authgoalid"), goalid));

		      if (parentrole != null)
		    	  andPredicates.add(criteriaBuilder.equal(root.get("userrole"), parentrole));

		      if (showApprovedOnly) {
		    	  andPredicates.add(criteriaBuilder.notEqual(root.get("status"), GoalStatus.New.name()));
		    	  andPredicates.add(criteriaBuilder.notEqual(root.get("status"), GoalStatus.PartiallyEndorsed.name()));
		      }
		      
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

	public static Specification<Sectiongoals> returnSectiongoalSpecification(String search, String sortColumn, Boolean descending, String goalid, String parentrole) {

		 return (root, query, criteriaBuilder) -> {
		      List<Predicate> andPredicates = new ArrayList<>(); // For AND conditions
		
		      if (descending) 
		          query.orderBy(criteriaBuilder.desc(root.get(sortColumn)));
		      else 
		          query.orderBy(criteriaBuilder.asc(root.get(sortColumn)));

		      if (goalid != null)
		    	  andPredicates.add(criteriaBuilder.equal(root.get("depgoalid"), goalid));

		      if (parentrole != null)
		    	  andPredicates.add(criteriaBuilder.equal(root.get("userrole"), parentrole));
		      
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

	public static Specification<Evidence> returnGoalevidenceSpecification(String search, String sortColumn,
			Boolean descending, String goalid, String user_role, String currentuser) {

		 return (root, query, criteriaBuilder) -> {
		      List<Predicate> andPredicates = new ArrayList<>(); // For AND conditions
		
		      if (descending) 
		          query.orderBy(criteriaBuilder.desc(root.get(sortColumn)));
		      else 
		          query.orderBy(criteriaBuilder.asc(root.get(sortColumn)));

		      if (goalid != null)
		    	  andPredicates.add(criteriaBuilder.equal(root.get("goalid"), goalid));

		      if (currentuser != null)
		    	  andPredicates.add(criteriaBuilder.equal(root.get("username"), currentuser));
		      
		      if (search != null) {
		          String searchPattern =  "%" + search + "%";
		          // Combine OR predicates into one OR condition
		          Predicate orCondition = criteriaBuilder.or(
		                  criteriaBuilder.like(root.get("username"), searchPattern),
		                  criteriaBuilder.like(root.get("comment"), searchPattern)
		              );
		
		          // Add the OR condition to the list of AND predicates
		          andPredicates.add(orCondition);
		      }
		
		      // Combine all AND predicates with an AND condition
		      return criteriaBuilder.and(andPredicates.toArray(new Predicate[0]));
		  };
	}

	public static Specification<EvidenceReply> returnEvidenceReplySpecification(String search, String sortColumn,
			Boolean descending, Long evidenceid) {

		 return (root, query, criteriaBuilder) -> {
		      List<Predicate> andPredicates = new ArrayList<>(); // For AND conditions
		
		      if (descending) 
		          query.orderBy(criteriaBuilder.desc(root.get(sortColumn)));
		      else 
		          query.orderBy(criteriaBuilder.asc(root.get(sortColumn)));

	    	  andPredicates.add(criteriaBuilder.equal(root.get("evidenceid"), evidenceid));

		      if (search != null) {
		          String searchPattern =  "%" + search + "%";
		          // Combine OR predicates into one OR condition
		          Predicate orCondition = criteriaBuilder.or(
		                  criteriaBuilder.like(root.get("username"), searchPattern),
		                  criteriaBuilder.like(root.get("comment"), searchPattern)
		              );
		
		          // Add the OR condition to the list of AND predicates
		          andPredicates.add(orCondition);
		      }
		
		      // Combine all AND predicates with an AND condition
		      return criteriaBuilder.and(andPredicates.toArray(new Predicate[0]));
		  };
	}
}