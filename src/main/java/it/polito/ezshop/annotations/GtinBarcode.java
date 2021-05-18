package it.polito.ezshop.annotations;

import it.polito.ezshop.util.GtinBarcodeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(PARAMETER)
@Constraint(validatedBy = GtinBarcodeValidator.class)
public @interface GtinBarcode {
    String message() default "Invalid GTIN barcode";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
