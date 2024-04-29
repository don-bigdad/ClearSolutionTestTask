package com.example.clearsolutiontesttask.validator;

import java.time.LocalDate;
import java.time.Period;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Validator component for validating user age.
 */
@Component
public final class UserAgeValidator {
    /**
     * The minimum age allowed for users.
     */
    @Value("${user.age.minimum}")
    private int minimumAge;


    /**
     * Checks if the user is old enough based on their birth date.
     *
     * @param birthDate The birth date of the user to validate.
     * @return true if the user is old enough, false otherwise.
     */
    public boolean isUserOldEnough(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears() >= minimumAge;
    }
}
