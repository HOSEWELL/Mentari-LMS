package app.framework.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Action {

    String value();

    String label() default "";

    boolean showLink() default true;

    String[] roles() default {
            "ADMIN"
    };
}