package app.framework.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({
        ElementType.TYPE,
        ElementType.METHOD
})
public @interface ProtectedRoute {

    boolean authRequired() default true;

    String[] roles() default {};
}