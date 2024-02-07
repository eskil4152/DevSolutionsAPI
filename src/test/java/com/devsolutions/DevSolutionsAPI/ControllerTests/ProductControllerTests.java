package com.devsolutions.DevSolutionsAPI.ControllerTests;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
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
public class ProductControllerTests {
    private final MockMvc mockMvc;

    @Autowired
    public ProductControllerTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    String baseUrl = "http://localhost:8080/api/products";

    @Test
    public void shouldGetAllProducts() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name", Matchers.containsString("Product One")))
                .andExpect(jsonPath("$[1].name", Matchers.containsString("Product Two")));
    }

    @Test
    public void shouldGetFirstProduct() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", Matchers.containsString("Product One")));
    }

    @Test
    // Add custom exceptions for errors which throws
    public void shouldReturnInvalidProductRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/40"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(Matchers.containsString("Product with ID 40 not found")));
    }

    @Test
    public void shouldReturn404() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/nonexistent"))
                .andExpect(status().is4xxClientError());
    }
}
