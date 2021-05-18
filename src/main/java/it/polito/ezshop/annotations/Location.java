package it.polito.ezshop.annotations;

import it.polito.ezshop.util.LocationValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(PARAMETER)
@Constraint(validatedBy = LocationValidator.class)
public @interface Location {
    String message() default "Invalid location";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
