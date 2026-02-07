package com.nsteuerberg.gametracker.shared.exceptions;

import lombok.Getter;

@Getter
public class EntityConflictException extends RuntimeException {
    private final String entityName;
    private final String fieldName;
    private final Object fieldValue;
    public EntityConflictException(String entityName, String fieldName, Object fieldValue) {
        super(String.format("%s con %s '%s' ya existe.", entityName, fieldName, fieldValue));
        this.entityName = entityName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}
