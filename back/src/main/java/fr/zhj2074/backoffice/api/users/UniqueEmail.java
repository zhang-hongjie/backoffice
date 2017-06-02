package fr.zhj2074.backoffice.api.users;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueEmailConstraintValidator.class)
@Documented
public @interface UniqueEmail {

    String message() default "must be unique";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
