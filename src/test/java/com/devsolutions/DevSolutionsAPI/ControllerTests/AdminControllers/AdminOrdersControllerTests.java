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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AdminOrdersControllerTests {
    private final MockMvc mockMvc;

    private final String baseUrl = "http://localhost:8080/api/order";
    private final String token = "Bearer " + JwtUtil.generateToken("orderuseradmin", UserRole.USER);
    private final String tokenAdmin = "Bearer " + JwtUtil.generateToken("orderAdmin", UserRole.USER);

    @Autowired
    public AdminOrdersControllerTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    @Order(1)
    @Disabled
    public void shouldCreateNewAdmin() throws Exception {
        JSONObject jsonObject = new JSONObject()
                .put("firstname", "order")
                .put("lastname", "admin")
                .put("username", "orderAdmin")
                .put("password", "password")
                .put("email", "order@admin.com");

        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"));

    }

    @Test
    @Order(2)
    @Disabled
    public void shouldCreateNewUserAndPlaceOrder() throws Exception {
        JSONObject jsonObject = new JSONObject()
                .put("firstname", "order")
                .put("lastname", "user")
                .put("username", "orderuseradmin")
                .put("password", "password")
                .put("email", "order@user.com");

        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"));

        JSONObject order = new JSONObject()
                .put("productId","1")
                .put("price", 199)
                .put("notes", "")
                .put("paymentMethod", "VISA")
                .put("billingAddress", "Address");

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(order.toString())
                        .header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    @Disabled
    public void shouldGetAllOrders() throws Exception {
        JSONObject jsonObject = new JSONObject()
                .put("productId","1")
                .put("price", 199)
                .put("notes", "")
                .put("paymentMethod", "VISA")
                .put("billingAddress", "Address");

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString())
                        .header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    @Disabled
    public void shouldUpdateAnOrder() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/all")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(jsonPath("$[0].price", Matchers.is(199.0)))
                .andExpect(jsonPath("$[0].user.username", Matchers.containsString("orderuser")))
                .andExpect(jsonPath("$[0].products.id", Matchers.is(1)))
                .andExpect(jsonPath("$[0].paymentMethod", Matchers.containsString("VISA")))
                .andExpect(jsonPath("$[0].orderStatus", Matchers.containsString("NOT_STARTED")));
    }

    @Test
    @Order(4)
    @Disabled
    public void shouldCancelAnOrder() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/1")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", Matchers.is(1)))
                .andExpect(jsonPath("price", Matchers.is(199.0)))
                .andExpect(jsonPath("user.username", Matchers.containsString("orderuser")))
                .andExpect(jsonPath("products.id", Matchers.is(1)))
                .andExpect(jsonPath("paymentMethod", Matchers.containsString("VISA")))
                .andExpect(jsonPath("orderStatus", Matchers.containsString("NOT_STARTED")));
    }

    @Test
    @Order(5)
    @Disabled
    public void shouldFailToGetAllOrders() throws Exception {
        JSONObject jsonObject = new JSONObject()
                .put("productId","1000")
                .put("price", 199)
                .put("notes", "")
                .put("paymentMethod", "VISA")
                .put("billingAddress", "Address");

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString())
                        .header("Authorization", token))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Order(6)
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
    @Order(7)
    @Disabled
    public void shouldFailToUpdateNonExistingOrder() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/all"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Order(8)
    @Disabled
    public void shouldFailToCancelOrderWhenUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/1"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Order(9)
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

    @Test
    @Order(10)
    @Disabled
    public void shouldFailToGetSpecificOrderFromAnotherUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/1")
                        .header("Authorization", tokenAdmin))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Order(11)
    @Disabled
    public void shouldFailToGetNonExistingOrder() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/100")
                        .header("Authorization", token))
                .andExpect(status().is4xxClientError());
    }
}