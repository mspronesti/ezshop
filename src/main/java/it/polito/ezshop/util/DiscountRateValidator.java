package it.polito.ezshop.util;

import it.polito.ezshop.annotations.DiscountRate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DiscountRateValidator implements ConstraintValidator<DiscountRate, Double> {
    @Override
    public void initialize(DiscountRate constraintAnnotation) {

    }

    @Override
    public boolean isValid(Double object, ConstraintValidatorContext constraintContext) {
        return object >= 0d && object < 1;
    }
}
