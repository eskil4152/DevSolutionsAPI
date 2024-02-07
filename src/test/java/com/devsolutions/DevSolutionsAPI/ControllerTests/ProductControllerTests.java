package com.devsolutions.DevSolutionsAPI.ControllerTests;

import com.devsolutions.DevSolutionsAPI.Entities.Products;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.ResultActions;
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
    @Disabled
    public void shouldCreateNewItem() throws Exception {
        Products newProduct = new Products("Name", "Desc", 10.0);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newProduct.toString()));

        resultActions.andExpect(status().isOk());
    }

    @Test
    public void shouldGetAllItems() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name", Matchers.containsString("Product One")))
                .andExpect(jsonPath("$[1].name", Matchers.containsString("Product Two")));
    }

    @Test
    public void shouldGetFirstItem() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", Matchers.containsString("Product One")));
    }
}
