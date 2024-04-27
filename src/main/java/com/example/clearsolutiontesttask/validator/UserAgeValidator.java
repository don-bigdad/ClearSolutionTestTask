package com.example.clearsolutiontesttask.validator;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserAgeValidator {
    @Value("${user.age.minimum}")
    private int minimumAge;

    public boolean isUserOldEnough(LocalDate birthDate) {
        var currentDate = LocalDate.now();
        var minimumBirthDate = currentDate.minusYears(minimumAge);
        return birthDate.isBefore(minimumBirthDate);
    }

    public int minimumAge() {
        return minimumAge;
    }
}
