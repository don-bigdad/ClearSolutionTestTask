package com.example.clearsolutiontesttask.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.example.clearsolutiontesttask.dto.UpdateFieldRequest;
import com.example.clearsolutiontesttask.entity.User;
import com.example.clearsolutiontesttask.exception.ValidationException;
import com.example.clearsolutiontesttask.mapper.UserMapper;
import com.example.clearsolutiontesttask.mapper.impl.UserMapperImpl;
import com.example.clearsolutiontesttask.validator.UserAgeValidator;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserAgeValidator ageValidator;

    @Spy
    private UserMapper bookMapper = new UserMapperImpl();

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Test adding a new user")
    void testAddUser() throws ValidationException {
        // Arrange
        User user = getUser();

        when(ageValidator.isUserOldEnough(any(LocalDate.class))).thenReturn(true);

        // Act
        userService.addUser(user);

        // Assert
        assertEquals(1, userService.getUsers().size());
    }

    @Test
    @DisplayName("Test updating an existing user")
    void testUpdateUser() throws ValidationException, EntityNotFoundException {
        // Arrange
        User user = getUser();
        when(ageValidator.isUserOldEnough(any(LocalDate.class))).thenReturn(true);

        // Adding the user to the list
        userService.getUsers().add(user);

        // Creating a new user with updated details
        User updatedUser = new User()
                .setFirstName("Jane")
                .setLastName("Doe")
                .setBirthDate(LocalDate.of(2000, 2, 2))
                .setEmail("newMail@gmail.com")
                .setAddress("Some street")
                .setPhoneNumber("123 33 44 55");

        // Act
        userService.updateUser(updatedUser, 1);

        // Assert
        assertEquals("Jane", userService.getUsers().get(0).getFirstName());
        assertEquals(LocalDate.of(2000, 2, 2),
                userService.getUsers().get(0).getBirthDate());
    }

    @Test
    @DisplayName("Test deleting an existing user")
    void testDeleteUser() throws EntityNotFoundException {
        // Arrange
        User user = getUser();

        // Adding the user to the list
        userService.getUsers().add(user);

        // Act
        userService.deleteUser(1);

        // Assert
        assertEquals(0, userService.getUsers().size());
    }

    @Test
    @DisplayName("Test finding users by birth date range")
    void testFindAllByBirthDateBetween() {
        // Arrange
        User user1 = new User();
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setBirthDate(LocalDate.of(1990, 10, 2));

        User user2 = new User();
        user2.setFirstName("Jane");
        user2.setLastName("Doe");
        user2.setBirthDate(LocalDate.of(1995, 6, 15));

        // Adding users to the list
        userService.getUsers().add(user1);
        userService.getUsers().add(user2);

        // Act
        List<User> usersInRange = userService.findAllByBirthDateBetween(
                LocalDate.of(1990, 1, 1),
                LocalDate.of(1995, 12, 31)
        );

        // Assert
        assertEquals(2, usersInRange.size());
    }

    @Test
    @DisplayName("Test updating specific fields of a user")
    void testUpdateUserField() throws ValidationException,
            EntityNotFoundException {
        // Arrange
        User user = getUser();

        // Adding the user to the list
        userService.getUsers().add(user);

        var updateFieldRequest = new UpdateFieldRequest();
        updateFieldRequest.setFirstName("Jane");
        when(ageValidator.isUserOldEnough(any(LocalDate.class))).thenReturn(true);

        // Act
        userService.updateUserField(updateFieldRequest, 1);

        // Assert
        assertEquals("Jane", userService.getUsers().get(0).getFirstName());
    }

    @Test
    @DisplayName("Test adding user with invalid birth date")
    void testAddUser_InvalidBirthDate() {
        // Arrange
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setBirthDate(LocalDate.now().plusDays(1)); // Future birth date

        // Mocking age validation to throw an exception
        when(ageValidator.isUserOldEnough(any(LocalDate.class))).thenReturn(false);

        // Act & Assert
        assertThrows(ValidationException.class,
                () -> userService.addUser(user));
    }

    private User getUser() {
        return new User()
                .setId(1)
                .setFirstName("John")
                .setLastName("Doe")
                .setBirthDate(LocalDate.of(1990, 1, 1));
    }
}
