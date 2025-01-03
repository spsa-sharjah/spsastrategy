package com.spsa.strategy.enumeration;

public enum PositionEnum {
	MANAGER, EMPLOYEE;
	
    public static boolean contains(String value) {
        for (PositionEnum enumValue : PositionEnum.values()) {
            if (enumValue.name().equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
}