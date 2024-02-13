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
    //TODO Expand
    public void shouldGetAllOrders() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(adminCookie))
                .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    @Disabled
    public void shouldUpdateAnOrder() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/all")
                        .cookie(adminCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(jsonPath("$[0].price", Matchers.is(199.0)))
                .andExpect(jsonPath("$[0].user.username", Matchers.containsString("orderuser")))
                .andExpect(jsonPath("$[0].products.id", Matchers.is(1)))
                .andExpect(jsonPath("$[0].paymentMethod", Matchers.containsString("VISA")))
                .andExpect(jsonPath("$[0].orderStatus", Matchers.containsString("NOT_STARTED")));
    }

    @Test
    @Order(5)
    @Disabled
    public void shouldCancelAnOrder() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/1")
                        .cookie(adminCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", Matchers.is(1)))
                .andExpect(jsonPath("price", Matchers.is(199.0)))
                .andExpect(jsonPath("user.username", Matchers.containsString("orderuser")))
                .andExpect(jsonPath("products.id", Matchers.is(1)))
                .andExpect(jsonPath("paymentMethod", Matchers.containsString("VISA")))
                .andExpect(jsonPath("orderStatus", Matchers.containsString("NOT_STARTED")));
    }

    @Test
    @Order(6)
    public void shouldFailToGetAllOrders() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Order(7)
    @Disabled
    public void shouldFailToUpdateOrderWhenUnauthorized() throws Exception {
        JSONObject jsonObject = new JSONObject()
                .put("productId","1")
                .put("price", 199)
                .put("notes", "")
                .put("paymentMethod", "VISA")
                .put("billingAddress", "Address");

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Order(8)
    @Disabled
    public void shouldFailToUpdateNonExistingOrder() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/all"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Order(9)
    @Disabled
    public void shouldFailToCancelOrderWhenUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/1"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Order(10)
    @Disabled
    public void shouldFailToCancelNonExistingOrder() throws Exception {
        JSONObject jsonObject = new JSONObject()
                .put("firstname", "order")
                .put("lastname", "user")
                .put("username", "orderusertwo")
                .put("password", "password")
                .put("email", "order@user.com");

        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"));
    }
}
