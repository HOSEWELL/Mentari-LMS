package app.utility.validation;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

@ApplicationScoped
@Named("Student")
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