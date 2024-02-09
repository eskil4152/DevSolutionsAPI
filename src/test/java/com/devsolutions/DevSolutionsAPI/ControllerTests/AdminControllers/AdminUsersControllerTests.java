package com.devsolutions.DevSolutionsAPI.ControllerTests.AdminControllers;

import com.devsolutions.DevSolutionsAPI.CreateTestUser;
import com.devsolutions.DevSolutionsAPI.Enums.UserRole;
import com.devsolutions.DevSolutionsAPI.Security.JwtFilter;
import com.devsolutions.DevSolutionsAPI.Security.JwtUtil;
import org.apache.el.stream.Optional;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AdminUsersControllerTests {
    private final MockMvc mockMvc;
    private CreateTestUser createTestUser;

    private final String token = JwtUtil.generateToken("Admin", UserRole.ADMIN);

    @Autowired
    public AdminUsersControllerTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    private final String baseUrl = "http://localhost:8080/api/admin";

    @Test
    @Order(1)
    public void shouldMakeTestUser() throws Exception {
        createTestUser.registerTestUser();
    }

    @Test
    @Order(2)
    public void shouldGetAllUsers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/users")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username", Matchers.containsString("testuser")));
    }

    @Test
    @Order(3)
    public void shouldPromoteUserToMod() throws Exception {
        JSONObject data = new JSONObject()
                .put("id", 1)
                .put("username", "testuser")
                .put("change", "PROMOTE");

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/roleChange")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(data.toString()))
                .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    public void shouldGetAllMods() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/mods")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username", Matchers.containsString("testuser")));
    }

    @Test
    @Order(5)
    public void shouldDemoteModToUser() throws Exception {
        JSONObject data = new JSONObject()
                .put("id", 1)
                .put("username", "testuser")
                .put("change", "DEMOTE");

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/roleChange")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data.toString()))
                .andExpect(status().isOk());
    }

    @Test
    @Order(6)
    public void shouldGetNoMods() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/mods")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());
    }
}
