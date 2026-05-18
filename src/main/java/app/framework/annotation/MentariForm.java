package app.framework.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MentariForm {

    String label() default "Register";

    String method() default "POST";

    String actionUrl();
}