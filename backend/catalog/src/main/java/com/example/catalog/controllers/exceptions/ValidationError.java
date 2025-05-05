package com.example.catalog.controllers.exceptions;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class ValidationError extends StandardError {
    private final List<FieldMessage> errors = new ArrayList<>();

    public List<FieldMessage> getErrors() {
        return errors;
    }

    public void addError(String field, String message) {
        errors.add(new FieldMessage(field, message));
    }
}
