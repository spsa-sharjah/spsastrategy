package com.spsa.strategy.enumeration;

public enum UserLevelEnum {
	AUTHORITY, DEPARTMENT, SECTION;
	
    public static boolean contains(String value) {
        for (UserLevelEnum enumValue : UserLevelEnum.values()) {
            if (enumValue.name().equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
}