package app.util.validation;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@ValidatorQualifier(ValidatorQualifier.ValidationChoice.COURSE)
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