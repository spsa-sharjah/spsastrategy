package com.spsa.strategy.enumeration;

public enum UserRoleEnum {
	MANAGER, EMPLOYEE;
	
    public static boolean contains(String value) {
        for (UserRoleEnum enumValue : UserRoleEnum.values()) {
            if (enumValue.name().equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
}