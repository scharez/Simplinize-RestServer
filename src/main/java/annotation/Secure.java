package annotation;

import entity.Role;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Secure {

    Role[] value() default Role.EVERYONE;
}
