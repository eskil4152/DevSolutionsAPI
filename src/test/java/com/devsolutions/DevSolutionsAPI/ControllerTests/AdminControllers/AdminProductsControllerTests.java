package com.devsolutions.DevSolutionsAPI.ControllerTests.AdminControllers;

import com.devsolutions.DevSolutionsAPI.Controllers.AdminControllers.AdminProductsController;
import com.devsolutions.DevSolutionsAPI.Entities.Users;
import com.devsolutions.DevSolutionsAPI.Enums.UserRole;
import com.devsolutions.DevSolutionsAPI.Security.JwtUtil;
import com.devsolutions.DevSolutionsAPI.Tools.CheckJwt;
import jakarta.servlet.http.HttpServletRequest;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AdminProductsControllerTests {
    private final MockMvc mockMvc;

    private String authToken = "Bearer " + JwtUtil.generateToken("Admin", UserRole.ADMIN);

    @Autowired
    public AdminProductsControllerTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    String baseUrl = "http://localhost:8080/api/admin/products";

    @Test
    @Order(1)
    public void shouldFailAsUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/all"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Order(2)
    @Disabled
    public void shouldMakeAdminUser() throws Exception {
        JSONObject jsonObject = new JSONObject()
                .put("firstname", "Admin")
                .put("lastname", "User")
                .put("username", "Admin")
                .put("password", "adminpass")
                .put("email", "test@pass.com");

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"))
                .andReturn();

        authToken = mvcResult.getResponse().getHeader("Authorization");
    }

    @Test
    @Order(3)
    @Disabled
    public void shouldCreateNewProduct() throws Exception {
        JSONObject object = new JSONObject()
                .put("productName", "Product x")
                .put("description", "Desc")
                .put("price", 100);

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(object.toString())
                        .header("Authorization", authToken))
                .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    @Disabled
    public void shouldGetAllProducts() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/all")
                    .header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[5].name", Matchers.containsString("Product x")));
    }

    @Test
    @Order(5)
    @Disabled
    public void shouldUpdateProduct() throws Exception {

    }

    @Test
    @Order(6)
    @Disabled
    public void shouldDeleteProduct() throws Exception {

    }

    @Test
    @Order(7)
    @Disabled
    public void shouldFailToUpdate() throws Exception {

    }

    @Test
    @Order(8)
    @Disabled
    public void shouldFailToDelete() throws Exception {

    }
}
