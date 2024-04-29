package com.example.clearsolutiontesttask.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.clearsolutiontesttask.entity.User;
import com.example.clearsolutiontesttask.service.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserService userService;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                .build();
    }

    @BeforeEach
    void setUp() {
        userService.getUsers().clear();
    }

    @Test
    @DisplayName("Success create new valid User")
    void testAddNewUserSuccess() throws Exception {
        var jsonRequest =
                objectMapper.writeValueAsString(createDefaultValidUser());
        mockMvc.perform(post("/users/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated());

        assertEquals(1, userService.getUsers().size());
    }

    @Test
    @DisplayName("Attempt to create a new user with invalid data - Bad Request")
    void testAddNewUserBadRequest() throws Exception {
        var jsonRequest = objectMapper.writeValueAsString(createInvalidUser());
        mockMvc.perform(post("/users/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
        assertEquals(0, userService.getUsers().size());
    }

    @Test
    @DisplayName("Success delete user from list")
    void testDeleteUserSuccess() throws Exception {
        userService.getUsers().add(createDefaultValidUser());
        mockMvc.perform(delete("/users/delete/1"))
                .andExpect(status().isNoContent());

        assertEquals(0, userService.getUsers().size());
    }

    @Test
    @DisplayName("Delete non-existing user from list")
    void testDeleteUserNotFound() throws Exception {
        mockMvc.perform(delete("/users/delete/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateUserSuccess() throws Exception {
        var user = createDefaultValidUser();
        userService.getUsers().add(user);
        user.setEmail("newEmail@gmail.com");
        user.setFirstName("New name"); // old fields are same
        var jsonRequest = objectMapper.writeValueAsString(user);
        mockMvc.perform(put("/users/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());
        User updatedUser = userService.getUsers().get(0);

        assertEquals("newEmail@gmail.com", updatedUser.getEmail());
        assertEquals("New name", updatedUser.getFirstName());

    }

    @Test
    void testUpdateUserBadRequest() throws Exception {
        var user = createDefaultValidUser();
        userService.getUsers().add(user);
        var updateRequest = new User().setEmail("ValidMail@gmail.com")
                .setFirstName("Homer")
                .setAddress("Street 123");

        var jsonRequest = objectMapper.writeValueAsString(updateRequest);
        // fail because not all fields were passed in the request
        mockMvc.perform(put("/users/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateUserFieldSuccess() throws Exception {
        var user = createDefaultValidUser();
        userService.getUsers().add(user);
        var newAddress = new User().setAddress("new Address");
        var jsonRequest = objectMapper.writeValueAsString(newAddress);

        mockMvc.perform(patch("/users/updateField/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());

        User updatedUser = userService.getUsers().get(0);

        assertEquals("new Address", updatedUser.getAddress());
    }

    @Test
    void testFindUsersByAgeBetween_ValidDates_Success() throws Exception {
        var user = createDefaultValidUser();
        userService.getUsers().add(user);

        var secondUser = new User();
        secondUser.setBirthDate(LocalDate.of(1994, 10, 12))
                .setFirstName("Peter")
                .setEmail("petGriff@gmail.com")
                .setLastName("Griffin")
                .setId(2)
                .setAddress("Address 1")
                .setPhoneNumber("999 99 99 99");

        userService.getUsers().add(secondUser);

        var thirdUser = createDefaultValidUser();
        thirdUser.setBirthDate(LocalDate.of(2000,2,12));
        userService.getUsers().add(thirdUser);

        MvcResult result = mockMvc.perform(get("/users/age")
                        .param("fromDate", "1990-01-01")
                        .param("toDate", "1995-12-31")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName")
                        .value("John"))
                .andExpect(jsonPath("$[0].email")
                        .value("johnDoe@gmail.com"))
                .andReturn();

        List<User> actualUserList = objectMapper.readValue(result.getResponse()
                .getContentAsString(), new TypeReference<>() {
        });
        assertEquals(2, actualUserList.size());
    }

    private User createDefaultValidUser() {
        return new User()
                .setId(1)
                .setFirstName("John")
                .setLastName("Doe")
                .setBirthDate(LocalDate.of(1990, 1, 2))
                .setEmail("johnDoe@gmail.com")
                .setAddress("123 Main.st 1")
                .setPhoneNumber("123 33 44 55");
    }

    private User createInvalidUser() {
        return new User()
                .setFirstName("User")
                .setLastName("Surname")
                .setBirthDate(LocalDate.of(1991, 3, 3))
                .setEmail("johnDo")
                .setAddress("123 Main.st 1")
                .setPhoneNumber("123 33 44 55");
    }
}
