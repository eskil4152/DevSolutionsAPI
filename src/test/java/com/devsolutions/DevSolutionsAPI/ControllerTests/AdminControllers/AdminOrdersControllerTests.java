package com.devsolutions.DevSolutionsAPI.ControllerTests.AdminControllers;

import com.devsolutions.DevSolutionsAPI.Entities.Orders;
import com.devsolutions.DevSolutionsAPI.Enums.UserRole;
import com.devsolutions.DevSolutionsAPI.Security.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.checkerframework.checker.units.qual.C;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AdminOrdersControllerTests {
    private final MockMvc mockMvc;

    private final String baseUrl = "http://localhost:8080/api/admin/order";

    private final Cookie adminCookie = new Cookie("Authentication", JwtUtil.generateToken("orderAdmin", UserRole.ADMIN));
    private final Cookie userCookie = new Cookie("Authentication", JwtUtil.generateToken("orderUserAdmin", UserRole.USER));

    @Autowired
    public AdminOrdersControllerTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    @Order(1)
    public void shouldCreateNewUserAndPlaceOrder() throws Exception {
        JSONObject jsonObject = new JSONObject()
                .put("firstname", "order")
                .put("lastname", "user")
                .put("username", "orderUserAdmin")
                .put("password", "password")
                .put("email", "order@user.com");

        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("Authentication"));

        JSONObject order = new JSONObject()
                .put("productId","1")
                .put("price", 199)
                .put("notes", "")
                .put("paymentMethod", "VISA")
                .put("billingAddress", "Address");

        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/api/order/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(order.toString())
                        .cookie(userCookie))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @Order(2)
    public void shouldGetAllOrders() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(adminCookie))
                .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    public void shouldUpdateAnOrder() throws Exception {
        JSONObject order = new JSONObject()
                .put("orderId", 1)
                .put("orderStatus", "COMPLETED")
                .put("developerFeedback", "Good");

        mockMvc.perform(MockMvcRequestBuilders.put(baseUrl + "/update")
                        .cookie(adminCookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(order.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.price", Matchers.is(199.0)))
                .andExpect(jsonPath("$.orderStatus", Matchers.containsString("COMPLETED")))
                .andExpect(jsonPath("$.developerFeedback", Matchers.containsString("Good")));
    }

    @Test
    @Order(5)
    public void shouldFailToCancelOrderWhenUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(baseUrl + "/cancel/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Order(6)
    public void shouldFailToUpdateOrderWhenUnauthorized() throws Exception {
        JSONObject order = new JSONObject()
                .put("orderId", 1)
                .put("orderStatus", "COMPLETED")
                .put("developerFeedback", "Good, but not too good");

        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/update")
                        .content(order.toString()))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Order(7)
    @Disabled
    // TODO Fix constraint with users
    public void shouldCancelAnOrder() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(baseUrl + "/cancel/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(adminCookie))
                .andExpect(status().isOk());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/all")
                        .cookie(adminCookie))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        assertThat(responseBody).doesNotContain("COMPLETED", "GOOD");
    }

    @Test
    @Order(8)
    public void shouldFailToGetAllOrders() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Order(9)
    public void shouldFailToUpdateNonExistingOrder() throws Exception {
        JSONObject order = new JSONObject()
                .put("orderId", 1000)
                .put("orderStatus", "COMPLETED")
                .put("developerFeedback", "Good");

        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/update")
                        .cookie(adminCookie)
                        .content(order.toString()))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Order(10)
    public void shouldFailToCancelNonExistingOrder() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(baseUrl + "/cancel/1000")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(adminCookie))
                .andExpect(status().is4xxClientError());
    }
}
