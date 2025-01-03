package com.spsa.strategy.enumeration;

public enum LevelEnum {
	AUTHORITY, DEPARTMENT, SECTION;
	
    public static boolean contains(String value) {
        for (LevelEnum enumValue : LevelEnum.values()) {
            if (enumValue.name().equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
}