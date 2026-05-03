package app.utility.validation;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

@ApplicationScoped
@Named("Course")
public class ValidateCourse implements Validate<String> {

    @Override
    public boolean name(String courseName) {
        if (courseName == null || courseName.trim().isEmpty()) {
            return false;
        }

        System.out.println("VALIDATION >>> Checking Course Name length...");
        return courseName.trim().length() > 5;
    }
}