package com.example.clearsolutiontesttask.service;

import com.example.clearsolutiontesttask.dto.UpdateFieldRequest;
import com.example.clearsolutiontesttask.entity.User;
import com.example.clearsolutiontesttask.exception.ValidationException;
import com.example.clearsolutiontesttask.mapper.UserMapper;
import com.example.clearsolutiontesttask.validator.UserAgeValidator;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Service class for managing user-related operations.
 */
@Component
@RequiredArgsConstructor
public class UserService {
    private final UserAgeValidator ageValidator;
    private final List<User> users = new ArrayList<>();
    private int nextId = 1;
    private final UserMapper userMapper;

    /**
     * Adds a new user.
     *
     * @param request The user data to add.
     * @throws ValidationException if there is a validation error.
     */
    public void addUser(User request) throws ValidationException {
        var user = ageVerification(request);
        user.setId(nextId++);
        users.add(user);
    }

    /**
     * Updates all field of an existing user.
     *
     * @param request The updated user data.
     * @param id      The ID of the user to update.
     * @throws EntityNotFoundException if the user with the specified ID is
     * not found.
     * @throws ValidationException     if there is a validation error.
     */
    public void updateUser(User request, int id) throws EntityNotFoundException,
            ValidationException {
        getUserById(id);
        var user = ageVerification(request);
        user.setId(id);
        users.set(id - 1, user);
    }

    /**
     * Deletes a user by ID.
     *
     * @param id The ID of the user to delete.
     * @throws EntityNotFoundException if the user with the specified ID is
     * not found.
     */
    public void deleteUser(int id) throws EntityNotFoundException {
        var user = getUserById(id);
        users.remove(user);
    }

    /**
     * Finds users with birth dates within the specified range.
     *
     * @param fromDate The start date of the range.
     * @param toDate   The end date of the range.
     * @return A list of users within the specified date range.
     */
    public List<User> findAllByBirthDateBetween(LocalDate fromDate,
                                                LocalDate toDate) {
        return users.stream()
                .filter(user -> user.birthDate().isAfter(fromDate) && user.birthDate().isBefore(toDate.plusDays(1)))
                .collect(Collectors.toList());
    }

    /**
     * Updates specific fields of a user.
     *
     * @param updateRequest The fields to update.
     * @param id            The ID of the user to update.
     * @throws ValidationException     If the user does not pass the age or
     * specified an invalid email.
     * @throws EntityNotFoundException if the user with the specified ID is
     * not found.
     */
    public void updateUserField(UpdateFieldRequest updateRequest, int id) throws
            ValidationException {
        var userById = getUserById(id);
        var updatedUser = userMapper.updateUserFromRequest(updateRequest,
                userById);
        ageVerification(updatedUser);
        users.set(id - 1, updatedUser);
    }

    private User getUserById(int id) {
        for (User user : users) {
            if (user.id() == id) {
                return user;
            }
        }
        throw new EntityNotFoundException("User with id: " + id + " does not " +
                "exist.");
    }

    private User ageVerification(User user) throws ValidationException {
        if (ageValidator.isUserOldEnough(user.birthDate())) {
            return user;
        } else {
            throw new ValidationException("User is not old enough");
        }
    }
}
