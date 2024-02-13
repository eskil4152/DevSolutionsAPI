package com.devsolutions.DevSolutionsAPI.ControllerTests;

import com.devsolutions.DevSolutionsAPI.Enums.UserRole;
import com.devsolutions.DevSolutionsAPI.Security.JwtUtil;
import jakarta.servlet.http.Cookie;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OwnerControllerTests {
    private final MockMvc mockMvc;

    private final String baseUrl = "http://localhost:8080/api/admin";

    private final Cookie ownerCookie = new Cookie("Authentication", JwtUtil.generateToken("owner", UserRole.OWNER));
    private final Cookie adminCookie = new Cookie("Authentication", JwtUtil.generateToken("admin", UserRole.ADMIN));

    @Autowired
    public OwnerControllerTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    @Order(1)
    public void shouldMakeTestOwnerAndAdminAndUser() throws Exception {
        JSONObject adminO = new JSONObject()
                .put("firstname", "firstname")
                .put("lastname", "lastname")
                .put("username", "admin")
                .put("password", "password")
                .put("email", "test@pass.com");

        JSONObject ownerO = new JSONObject()
                .put("firstname", "firstname")
                .put("lastname", "lastname")
                .put("username", "owner")
                .put("password", "password")
                .put("email", "test@pass.com");

        JSONObject userO = new JSONObject()
                .put("firstname", "firstname")
                .put("lastname", "lastname")
                .put("username", "motionUser")
                .put("password", "password")
                .put("email", "test@pass.com");

        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ownerO.toString()))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("Authentication"));

        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(adminO.toString()))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("Authentication"));

        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userO.toString()))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("Authentication"));
    }

    @Test
    @Order(2)
    public void ownerShouldPromoteUserToMod() throws Exception {
        JSONObject data = new JSONObject()
                .put("username", "motionUser")
                .put("change", "PROMOTE");

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/roleChange")
                        .cookie(ownerCookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data.toString()))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/mods")
                        .cookie(ownerCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..username", Matchers.contains("motionUser")));
    }

    @Test
    @Order(3)
    public void adminShouldFailToPromoteModToAdmin() throws Exception {
        JSONObject data = new JSONObject()
                .put("username", "motionUser")
                .put("change", "PROMOTE");

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/roleChange")
                        .cookie(adminCookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data.toString()))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Order(4)
    public void ownerShouldPromoteModToAdmin() throws Exception {
        JSONObject data = new JSONObject()
                .put("username", "motionUser")
                .put("change", "PROMOTE");

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/roleChange")
                        .cookie(ownerCookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data.toString()))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/api/owner/admins")
                        .cookie(ownerCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..username", Matchers.contains("motionUser")));
    }

    @Test
    @Order(5)
    public void adminShouldFailToDemoteAdminToMod() throws Exception {
        JSONObject data = new JSONObject()
                .put("username", "motionUser")
                .put("change", "DEMOTE");

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/roleChange")
                        .cookie(adminCookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data.toString()))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Order(6)
    public void ownerShouldDemoteAdminToMod() throws Exception {
        JSONObject data = new JSONObject()
                .put("username", "motionUser")
                .put("change", "DEMOTE");

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/roleChange")
                        .cookie(ownerCookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data.toString()))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/mods")
                        .cookie(ownerCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..username", Matchers.contains("motionUser")));
    }
}
