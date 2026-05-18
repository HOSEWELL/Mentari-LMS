package app.framework.annotation;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MentariTableColumn {

    String label();

    boolean hidden() default false;
}