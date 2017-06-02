package fr.zhj2074.backoffice.api.users;


import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorFactoryImpl;
import org.springframework.validation.beanvalidation.OptionalValidatorFactoryBean;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorFactory;

@ControllerAdvice
public class ControllerTestAdvice {

    public UniqueEmailConstraintValidator uniqueEmailConstraintValidator;

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        OptionalValidatorFactoryBean validator = new OptionalValidatorFactoryBean();
        validator.setConstraintValidatorFactory(new ConstraintValidatorFactory() {

            private ConstraintValidatorFactoryImpl factory = new ConstraintValidatorFactoryImpl();

            @Override
            public <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key) {
                if (key == UniqueEmailConstraintValidator.class) {
                    return (T) uniqueEmailConstraintValidator;
                }

                return factory.getInstance(key);
            }

            @Override
            public void releaseInstance(ConstraintValidator<?, ?> instance) {
            }
        });

        validator.afterPropertiesSet();
        binder.setValidator(validator);
    }
}
