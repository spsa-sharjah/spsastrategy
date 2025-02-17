package com.spsa.strategy.builder.response;

import java.util.List;

import com.spsa.strategy.model.Authoritygoals;
import com.spsa.strategy.model.Departmentgoals;

public class GoalsTree {

	private Authoritygoals authoritygoal;
	
	private List<Departmentgoals> departmentgoalslist;

	public GoalsTree() {
		super();
	}

	public GoalsTree(Authoritygoals authoritygoal, List<Departmentgoals> departmentgoalslist) {
		super();
		this.authoritygoal = authoritygoal;
		this.departmentgoalslist = departmentgoalslist;
	}

	public Authoritygoals getAuthoritygoal() {
		return authoritygoal;
	}

	public void setAuthoritygoal(Authoritygoals authoritygoal) {
		this.authoritygoal = authoritygoal;
	}

	public List<Departmentgoals> getDepartmentgoalslist() {
		return departmentgoalslist;
	}

	public void setDepartmentgoalslist(List<Departmentgoals> departmentgoalslist) {
		this.departmentgoalslist = departmentgoalslist;
	}
}
