package app.framework.annotation;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MentariSelect {

    Class<?> entity();

    String labelField();
}