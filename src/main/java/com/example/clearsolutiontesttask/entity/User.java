package com.example.clearsolutiontesttask.entity;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;
import lombok.Data;

/**
 * Entity class representing a user.
 */
@Data
public class User {
    private int id;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @Past(message = "Birth date must be earlier than current date")
    private LocalDate birthDate;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String address;

    public LocalDate birthDate() {
        return birthDate;
    }

    public int id() {
        return id;
    }
}
