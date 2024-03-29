package com.devsolutions.DevSolutionsAPI.ControllerTests;

import com.devsolutions.DevSolutionsAPI.Enums.UserRole;
import com.devsolutions.DevSolutionsAPI.Security.JwtUtil;
import jakarta.servlet.http.Cookie;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTests {
    private final MockMvc mockMvc;

    @Autowired
    public UserControllerTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    String baseUrl = "http://localhost:8080/api";

    @Test
    @Order(1)
    public void shouldRegister() throws Exception {
        JSONObject jsonObject = new JSONObject()
                .put("firstname", "testfirst")
                .put("lastname", "testlast")
                .put("username", "testuser")
                .put("password", "testpass")
                .put("email", "test@pass.com");

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("Authentication"));
    }

    @Test
    @Order(2)
    public void shouldFailToRegister() throws Exception {
        JSONObject jsonObject = new JSONObject()
                .put("firstname", "testfirst")
                .put("lastname", "testlast")
                .put("username", "testuser")
                .put("password", "testpass")
                .put("email", "test@pass.com");

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Order(3)
    public void shouldLogInAndGetUserInfo() throws Exception {
        Cookie cookie = new Cookie("Authentication", JwtUtil.generateToken("testuser", UserRole.USER));

        JSONObject jsonObject = new JSONObject()
                .put("username", "testuser")
                .put("password", "testpass");

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("Authentication"))
                .andReturn();

        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/user")
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(cookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", Matchers.containsString("testuser")));
    }

    @Test
    @Order(4)
    public void shouldFailToLogIn() throws Exception {
        JSONObject jsonObject = new JSONObject()
                .put("username", "testuser")
                .put("password", "wrongpass");

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Order(5)
    public void shouldFailToGetUserInfoWhenNotLoggedIn() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/user"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Order(6)
    public void shouldFailToGetUserInfoWhenTokenIsInvalid() throws Exception {
        String token = JwtUtil.generateToken("fakeuser", UserRole.USER);

        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/user")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().is4xxClientError());
    }
}
