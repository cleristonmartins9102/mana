package mana.mana33.validators;

import mana.mana33.features.contracts.Validate;

public class StringValidator implements Validate<String> {

    @Override
    public void validate(String input) {
        if (input == null) {
            throw new IllegalArgumentException("String cannot be null");
        }

        if (!(input instanceof String)) {
            throw new IllegalArgumentException("Input must be a String type");
        }

        if (input.trim().isEmpty()) {
            throw new IllegalArgumentException("String cannot be empty");
        }
    }
}
