package it.polito.ezshop.annotations;

import it.polito.ezshop.utils.DiscountRateValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(PARAMETER)
@Constraint(validatedBy = DiscountRateValidator.class)
public @interface DiscountRate {
    String message() default "A discount rate must be greater or equal to 0 and less than 1";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
