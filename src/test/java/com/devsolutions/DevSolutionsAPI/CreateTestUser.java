package com.devsolutions.DevSolutionsAPI;

import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class CreateTestUser {
    private boolean hasRun = false;
    private final MockMvc mockMvc;

    public CreateTestUser(MockMvc mockMvc){
        this.mockMvc = mockMvc;
    }

    public void registerTestUser() throws Exception {
        if (hasRun)
            return;

        JSONObject user = new JSONObject()
                .put("firstname", "test")
                .put("lastname", "user")
                .put("username", "testuser")
                .put("email", "test@user.com")
                .put("password", "password");

        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(user.toString()));

        hasRun = true;
    }
}
