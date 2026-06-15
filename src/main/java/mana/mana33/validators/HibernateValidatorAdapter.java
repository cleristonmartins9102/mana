package mana.mana33.validators;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import mana.mana33.features.contracts.Validate;

import java.util.Set;
import java.util.stream.Collectors;

public class HibernateValidatorAdapter<T> implements Validate<T> {

    private final Validator validator;

    public HibernateValidatorAdapter(Validator validator) {
        this.validator = validator;
    }

    @Override
    public void validate(T input) {
        if (input == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }

        Set<ConstraintViolation<T>> violations = validator.validate(input);

        if (!violations.isEmpty()) {
            String errorMessage = violations.stream()
                    .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                    .collect(Collectors.joining(", "));
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
