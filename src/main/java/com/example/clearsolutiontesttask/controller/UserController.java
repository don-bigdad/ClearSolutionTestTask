package com.example.clearsolutiontesttask.controller;

import com.example.clearsolutiontesttask.dto.UpdateFieldRequest;
import com.example.clearsolutiontesttask.entity.User;
import com.example.clearsolutiontesttask.exception.ValidationException;
import com.example.clearsolutiontesttask.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for managing user-related operations.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/users")
public class UserController {
    private final UserService userService;

    /**
     * Adds a new user.
     *
     * @param createUserRequest The user data to create.
     * @throws ValidationException If the user does not pass the age or
     * specified an invalid email.
     */
    @PostMapping(value = "/add")
    @ResponseStatus(HttpStatus.CREATED)
    public void addNewUser(@RequestBody @Valid User createUserRequest)
            throws ValidationException {
        userService.addUser(createUserRequest);
    }

    /**
     * Deletes a user by ID.
     *
     * @param id The ID of the user to delete.
     * @throws EntityNotFoundException if the user with the specified ID is
     * not found.
     */
    @DeleteMapping(value = "/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable @Positive int id) throws EntityNotFoundException {
        userService.deleteUser(id);
    }

    /**
     * Updates a user's information.
     *
     * @param updateUserRequest The updated user data.
     * @param id                The ID of the user to update.
     * @throws EntityNotFoundException if the user with the specified ID is
     * not found.
     * @throws ValidationException     If the user does not pass the age
     * or specified an invalid email.
     */
    @PutMapping(value = "/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateUser(@RequestBody @Valid User updateUserRequest,
                           @PathVariable @Positive int id) throws EntityNotFoundException,
            ValidationException {
        userService.updateUser(updateUserRequest, id);
    }

    /**
     * Updates specific fields of a user.
     *
     * @param fields The fields to update.
     * @param id     The ID of the user to update.
     * @throws EntityNotFoundException if the user with the specified ID is
     * not found.
     * @throws ValidationException     If the user does not pass the age or
     * specified an invalid email.
     */
    @PatchMapping(value = "/updateField/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateUserField(@RequestBody @Valid UpdateFieldRequest fields,
                                @PathVariable @Positive int id) throws EntityNotFoundException, ValidationException {
        userService.updateUserField(fields, id);
    }

    /**
     * Retrieves users within a specified age range.
     *
     * @param fromDate The start date of the age range.
     * @param toDate   The end date of the age range.
     * @return A list of users within the specified age range.
     */
    @GetMapping(value = "/age")
    @ResponseStatus(HttpStatus.OK)
    public List<User> findUsersByAgeBetween(@RequestParam("fromDate")
                                            @DateTimeFormat(iso =
                                                    DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                            @RequestParam("toDate")
                                            @DateTimeFormat(iso =
                                                    DateTimeFormat.ISO.DATE) LocalDate toDate) {
        return userService.findAllByBirthDateBetween(fromDate, toDate);
    }
}
