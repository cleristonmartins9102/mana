package mana.mana33.validators;

import mana.mana33.features.contracts.Validate;
import java.util.regex.Pattern;

public class EmailValidator implements Validate<String> {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    @Override
    public void validate(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }

        if (!(input instanceof String)) {
            throw new IllegalArgumentException("Input must be a String type");
        }

        if (input.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }

        if (!EMAIL_PATTERN.matcher(input).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }
}
