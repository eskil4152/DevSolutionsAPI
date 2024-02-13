package com.devsolutions.DevSolutionsAPI.ControllerTests;

import com.devsolutions.DevSolutionsAPI.Entities.Orders;
import com.devsolutions.DevSolutionsAPI.Enums.UserRole;
import com.devsolutions.DevSolutionsAPI.Security.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class OrderControllerTests {
    private final MockMvc mockMvc;

    private static Long orderId;
    private final String baseUrl = "http://localhost:8080/api/order";

    private final Cookie cookie = new Cookie("Authentication", JwtUtil.generateToken("orderuser", UserRole.USER));
    private final Cookie cookieTwo = new Cookie("Authentication", JwtUtil.generateToken("orderusertwo", UserRole.USER));

    @Autowired
    public OrderControllerTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    @Order(1)
    public void shouldCreateNewUser() throws Exception {
        JSONObject jsonObject = new JSONObject()
                .put("firstname", "order")
                .put("lastname", "user")
                .put("username", "orderuser")
                .put("password", "password")
                .put("email", "order@user.com");

        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("Authentication"));
    }

    @Test
    @Order(2)
    public void shouldCreateNewOrder() throws Exception {
        JSONObject jsonObject = new JSONObject()
                .put("productId","1")
                .put("price", 199)
                .put("notes", "")
                .put("paymentMethod", "VISA")
                .put("billingAddress", "Address");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString())
                        .cookie(cookie))
                .andExpect(status().isOk())
                .andReturn();

        Orders order = new ObjectMapper().readValue(result.getResponse().getContentAsString(), Orders.class);
        orderId = order.getId();
    }

    @Test
    @Order(3)
    public void shouldGetAllOrdersForUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/all")
                        .cookie(cookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", Matchers.is(orderId.intValue())))
                .andExpect(jsonPath("$[0].price", Matchers.is(199.0)))
                .andExpect(jsonPath("$[0].user.username", Matchers.containsString("orderuser")))
                .andExpect(jsonPath("$[0].products.id", Matchers.is(1)))
                .andExpect(jsonPath("$[0].paymentMethod", Matchers.containsString("VISA")))
                .andExpect(jsonPath("$[0].orderStatus", Matchers.containsString("NOT_STARTED")));
    }

    @Test
    @Order(4)
    public void shouldGetSpecificOrderFromUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/" + orderId.intValue())
                        .cookie(cookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", Matchers.is(orderId.intValue())))
                .andExpect(jsonPath("price", Matchers.is(199.0)))
                .andExpect(jsonPath("user.username", Matchers.containsString("orderuser")))
                .andExpect(jsonPath("products.id", Matchers.is(1)))
                .andExpect(jsonPath("paymentMethod", Matchers.containsString("VISA")))
                .andExpect(jsonPath("orderStatus", Matchers.containsString("NOT_STARTED")));
    }

    @Test
    @Order(5)
    public void shouldFailToCreateNewOrderForNotExistingProduct() throws Exception {
        JSONObject jsonObject = new JSONObject()
                .put("productId","1000")
                .put("price", 199)
                .put("notes", "")
                .put("paymentMethod", "VISA")
                .put("billingAddress", "Address");

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString())
                        .cookie(cookie))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Order(6)
    public void shouldFailToCreateNewOrderWhenUnauthorized() throws Exception {
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
    @Order(7)
    public void shouldFailToGetAllOrdersFromUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/all"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Order(8)
    public void shouldFailToGetSpecificOrderFromNotLoggedInUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/1"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Order(9)
    public void shouldRegisterNewUser() throws Exception {
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
                .andExpect(cookie().exists("Authentication"));
    }

    @Test
    @Order(10)
    public void shouldFailToGetSpecificOrderFromAnotherUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/1")
                        .cookie(cookieTwo))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Order(11)
    public void shouldFailToGetNonExistingOrder() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/100")
                        .cookie(cookie))
                .andExpect(status().is4xxClientError());
    }

}
