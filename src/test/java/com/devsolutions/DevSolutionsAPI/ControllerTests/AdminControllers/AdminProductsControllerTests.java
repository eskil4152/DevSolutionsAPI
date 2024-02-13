package com.devsolutions.DevSolutionsAPI.ControllerTests.AdminControllers;

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

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AdminProductsControllerTests {
    private final MockMvc mockMvc;

    Cookie adminCookie = new Cookie("Authentication", JwtUtil.generateToken("Admin", UserRole.ADMIN));
    Cookie userCookie = new Cookie("Authentication", JwtUtil.generateToken("User", UserRole.USER));

    @Autowired
    public AdminProductsControllerTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    String baseUrl = "http://localhost:8080/api/admin/products";

    @Test
    @Order(1)
    public void shouldFailAsUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/all")
                        .cookie(userCookie))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Order(2)
    public void shouldCreateNewProduct() throws Exception {
        JSONObject object = new JSONObject()
                .put("productName", "Product x")
                .put("description", "Desc")
                .put("price", 100);

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(object.toString())
                        .cookie(adminCookie))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    public void shouldGetAllProducts() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/all")
                    .cookie(adminCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", Matchers.containsString("Product One")))
                .andExpect(jsonPath("$[1].name", Matchers.containsString("Product Two")))
                .andExpect(jsonPath("$[2].name", Matchers.containsString("Product Three")))
                .andExpect(jsonPath("$[3].name", Matchers.containsString("Product Four")))
                .andExpect(jsonPath("$[4].name", Matchers.containsString("Product Five")))
                .andExpect(jsonPath("$[5].name", Matchers.containsString("Product x")));
    }

    @Test
    @Order(4)
    @Disabled
    public void shouldUpdateProduct() throws Exception {
        JSONObject object = new JSONObject()
                .put("productName", "Product x")
                .put("description", "Desc")
                .put("price", 100);

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(object.toString())
                        .cookie(adminCookie))
                .andExpect(status().isOk());
    }

    @Test
    @Order(5)
    public void shouldDeleteProduct() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(baseUrl + "/delete/6")
                .contentType(MediaType.APPLICATION_JSON)
                        .cookie(adminCookie))
                .andExpect(status().isOk());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/all")
                        .cookie(adminCookie))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        assertThat(responseBody).doesNotContain("Product x");
    }

    @Test
    @Order(6)
    @Disabled
    public void shouldFailToUpdate() throws Exception {

    }

    @Test
    @Order(7)
    public void shouldFailToDelete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(baseUrl + "/delete/6")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(adminCookie))
                .andExpect(status().is4xxClientError());
    }
}
