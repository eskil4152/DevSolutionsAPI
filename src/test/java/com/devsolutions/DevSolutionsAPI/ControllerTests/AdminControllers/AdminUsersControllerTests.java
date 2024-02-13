package com.devsolutions.DevSolutionsAPI.ControllerTests.AdminControllers;

import com.devsolutions.DevSolutionsAPI.Enums.UserRole;
import com.devsolutions.DevSolutionsAPI.Security.JwtUtil;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AdminUsersControllerTests {
    private final MockMvc mockMvc;

    private final String token = JwtUtil.generateToken("Admin", UserRole.ADMIN);

    @Autowired
    public AdminUsersControllerTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    private final String baseUrl = "http://localhost:8080/api/admin";

    @Test
    @Order(1)
    public void shouldMakeTestUser() throws Exception {
        JSONObject jsonObject = new JSONObject()
                .put("firstname", "firstname")
                .put("lastname", "lastname")
                .put("username", "usernames")
                .put("password", "password")
                .put("email", "test@pass.com");

        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"));
    }

    @Test
    @Order(2)
    public void shouldPromoteUserToMod() throws Exception {
        JSONObject data = new JSONObject()
                .put("username", "usernames")
                .put("change", "PROMOTE");

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/roleChange")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data.toString()))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    public void shouldGetAllMods() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/mods")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..username", Matchers.hasItem("usernames")));
    }

    @Test
    @Order(4)
    public void shouldDemoteModToUser() throws Exception {
        JSONObject data = new JSONObject()
                .put("username", "usernames")
                .put("change", "DEMOTE");

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/roleChange")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data.toString()))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/users")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..username", Matchers.hasItem("usernames")));
    }

    @Test
    @Order(5)
    public void shouldNotGetUsersWhenUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/users"))
                .andExpect(status().is4xxClientError());
    }
}
