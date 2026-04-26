package app.util.validation;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@ValidatorQualifier(ValidatorQualifier.ValidationChoice.STUDENT)
public class ValidateStudent implements Validate<String> {

    @Override
    public boolean name(String studentName) {
        if (studentName == null || studentName.trim().isEmpty()) {
            return false;
        }

        System.out.println("VALIDATION >>> Checking Student Name for blocklist...");
        return !studentName.trim().equalsIgnoreCase("Mike Bavon");
    }
}