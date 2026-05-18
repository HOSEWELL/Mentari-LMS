package app.framework.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MentariFormField {

    String label();

    String placeholder() default "";

    String type() default "text";

    boolean required() default true;

    boolean hidden() default false;
}