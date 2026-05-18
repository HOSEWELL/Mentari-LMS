package app.framework.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MentariTableView {

    String title() default "";

    String addAction() default "";

    String deleteAction() default "";
}