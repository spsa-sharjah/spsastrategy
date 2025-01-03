# SPSA Strategy

## Introduction
- This project will be an internal tool for the SPSA team, designed to help employees manage departments and section goals aligned with the defined strategic goals of SPSA.
- The objective of this project is to evaluate whether each department and section has achieved its defined goals by tracking evidence and either accepting or rejecting it.

## Project Structure
- User Roles in the Project
  - Super Admin: Managed by the development team, responsible for managing the system's overall functionality and user access.
  - Strategic Admin: Responsible for defining SPSA strategic goals and monitoring employee progress toward these goals.
  - Department Manager: Sets department goals based on the strategic goals, and monitor employees progress toward these goals.
    - Section Manager: Sets section goals based on the department goals, for their respective sections within the department, and monitor employees progress toward these goals.
    - Employees: Assigned under departments, contributing to the achievement of section and departmental goals.

## Questions
- If a department includes sections, should employees be required to submit their evidence under the specific section goals, or should they have the option to submit evidence directly under the department goals?
- Do goals always require a deadline?
- I noticed a reason of deviation and solution provided; which user should fill these fields?
  
  
## Tricky Logic
- There is relation difference between the Authority with the Department team and Department with the Section team
	- Authority - Department 
		- Authority can specify which department to see the created goal
	- Department - Section
		- Department can specify which section to see the created goal
		- Section should by default see their parent department goals + previous restricted condition if exist
		
		
		