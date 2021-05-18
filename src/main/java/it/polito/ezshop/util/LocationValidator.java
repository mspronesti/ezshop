package it.polito.ezshop.util;

import it.polito.ezshop.annotations.Location;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

public class LocationValidator implements ConstraintValidator<Location, String> {
    @Override
    public void initialize(Location constraintAnnotation) {

    }

    /**
     * Checks whether a provided location string corresponds to a valid
     * location code
     *
     * @param object to check
     * @return true if valid, false otherwise
     */
    @Override
    public boolean isValid(String object, ConstraintValidatorContext constraintContext) {
        if (object != null && !object.isEmpty()) {
            return Pattern.matches("^\\d+-[A-Za-z]-\\d+$", object);
        }
        return true;
    }
}
