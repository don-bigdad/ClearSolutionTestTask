package com.example.clearsolutiontesttask.dto;

import jakarta.validation.constraints.Email;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO representing a request to update user fields.
 */
@Getter
@Setter
public class UpdateFieldRequest {
    @Email
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String phoneNumber;
    private String address;
}
