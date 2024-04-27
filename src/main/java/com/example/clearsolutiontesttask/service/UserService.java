package com.example.clearsolutiontesttask.service;

import com.example.clearsolutiontesttask.entity.User;
import com.example.clearsolutiontesttask.exception.RegistrationException;
import com.example.clearsolutiontesttask.exception.UserNotFoundException;
import com.example.clearsolutiontesttask.validator.UserAgeValidator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserAgeValidator ageValidator;
    private final List<User> users = new ArrayList<>();
    private int nextId = 1;

    public void addUser(User user) throws RegistrationException {
        if (ageValidator.isUserOldEnough(user.birthDate())) {
            user.setId(nextId++);
            users.add(user);
        } else {
            throw new RegistrationException("The user must be older "
                    + ageValidator.minimumAge());
        }
    }

    public void updateUser(User user, int id) throws RegistrationException{
        if (ageValidator.isUserOldEnough(user.birthDate())) {
            user.setId(id);
            users.set(id - 1, user);
        } else {
            throw new RegistrationException("The user must be older "
                    + ageValidator.minimumAge());
        }
    }

    public void deleteUser(int id) throws UserNotFoundException {
        if (userExistById(id)) {
            users.remove(id - 1);
        } else {
            throw new UserNotFoundException("User with id " + (id)
                    + " does`t exist.");
        }
    }

    public List<User> findAllByBirthDateBetween(LocalDate fromDate, LocalDate toDate) {
        if (fromDate.isAfter(toDate)) {
            throw new IllegalArgumentException("Date 'from' must be before date 'to'");
        }

        return users.stream()
                .filter(user -> user.birthDate().isAfter(fromDate)
                        && user.birthDate().isBefore(toDate.plusDays(1)))
                .collect(Collectors.toList());
    }

    public Optional<User> findById(int id) {
        return Optional.ofNullable(users.get(id - 1));
    }

    public void updateUserField(User updateUserFieldRequest, int id) {

    }

    private boolean userExistById(int id) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).id()  == id) {
                return true;
            }
        }
        return false;
    }
}
