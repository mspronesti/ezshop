package it.polito.ezshop.utils;

import it.polito.ezshop.annotations.GtinBarcode;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

public class GtinBarcodeValidator implements ConstraintValidator<GtinBarcode, String> {
    @Override
    public void initialize(GtinBarcode constraintAnnotation) {

    }

    /**
     * Checks whether a provided string of digits corresponds to a valid
     * GTIN bar code
     *
     * @param object to check
     * @return true if gtin, false otherwise
     */
    @Override
    public boolean isValid(String object, ConstraintValidatorContext constraintContext) {
        if (!Pattern.matches("^\\d{12,14}$", object)) {
            return false;
        }
        AtomicInteger index = new AtomicInteger();
        // create string from input excluding last digit
        String code = object.substring(0, object.length() - 1);
        // extracting last digit
        int checkDigit = object.charAt(object.length() - 1) ^ '0';
        // fill with appropriate number of initial zeros
        code = String.format("%0" + (17 - code.length()) + "d%s", 0, code);
        // compute sum of the digits after processing
        int sum = code.chars()
                .map(p -> p ^ '0') // char to int
                .map(p -> index.getAndIncrement() % 2 == 0 ? p * 3 : p)
                .sum();
        return (sum + 9) / 10 * 10 - sum == checkDigit;
    }
}
