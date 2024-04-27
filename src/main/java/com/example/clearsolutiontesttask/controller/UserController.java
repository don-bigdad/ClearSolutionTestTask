package com.example.clearsolutiontesttask.controller;

import com.example.clearsolutiontesttask.entity.User;
import com.example.clearsolutiontesttask.exception.RegistrationException;
import com.example.clearsolutiontesttask.exception.UserNotFoundException;
import com.example.clearsolutiontesttask.service.UserService;
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

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/users")
public class UserController {
    private final UserService userService;

    @PostMapping(value = "/add")
    @ResponseStatus(HttpStatus.CREATED)
    public void addNewUser(@RequestBody @Valid
                           User createUserRequest) throws RegistrationException {
        userService.addUser(createUserRequest);
    }

    @DeleteMapping(value = "/delete/{id}")
    public void deleteUser(@PathVariable @Positive int id)
            throws UserNotFoundException {
        userService.deleteUser(id);
    }

    @PutMapping(value = "/update/{id}")
    public void updateUser(@RequestBody @Valid User updateUserRequest,
                           @PathVariable @Positive int id)
            throws RegistrationException {
        userService.updateUser(updateUserRequest, id);
    }

    @PatchMapping(value = "/updateField/{id}")
    public void updateUserField(@RequestBody @Valid User updateUserFieldRequest,
                                @PathVariable @Positive int id)
            throws RegistrationException {
        userService.updateUserField(updateUserFieldRequest, id);
    }


    @GetMapping(value = "/age")
    public List<User> findUsersByAgeBetween(@RequestParam("fromDate")
                                            @DateTimeFormat(iso =
                                                    DateTimeFormat.ISO.DATE)
                                            LocalDate fromDate,
                                            @RequestParam("toDate")
                                            @DateTimeFormat(iso =
                                                    DateTimeFormat.ISO.DATE)
                                            LocalDate toDate) {
        return userService.findAllByBirthDateBetween(fromDate, toDate);
    }
}
