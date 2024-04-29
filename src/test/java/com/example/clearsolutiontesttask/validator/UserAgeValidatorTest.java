package com.example.clearsolutiontesttask.validator;

import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserAgeValidatorTest {
    @Autowired
    private UserAgeValidator ageValidator;

    @Test
    @DisplayName("User is old enough")
    void userIsOldEnough() {
        LocalDate birthDate = LocalDate.of(2002, 11, 12);
        Assertions.assertTrue(ageValidator.isUserOldEnough(birthDate));
    }

    @Test
    @DisplayName("User is not old enough")
    void userIsNotOldEnough() {
        LocalDate birthDate = LocalDate.of(2012, 1, 25);
        Assertions.assertFalse(ageValidator.isUserOldEnough(birthDate));
    }
}
