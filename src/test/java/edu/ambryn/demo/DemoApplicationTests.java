package edu.ambryn.demo;

import edu.ambryn.demo.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                             .apply(springSecurity())
                             .build();
    }

    @Test
    void creationIdUtilisateurNull() {
        User user = new User();
        assertNull(user.getId());
    }

    @Test
    void callRootUrl_OK() throws Exception {
        mvc.perform(get("/"))
           .andExpect(status().isOk());
    }

    @Test
    void callRootUrl_messageOK() throws Exception {
        mvc.perform(get("/"))
           .andExpect(content().string("Le serveur fonctionne bien"));
    }

    @Test
    void userNotLoggedInCallUsers_403() throws Exception {
        mvc.perform(get("/users"))
           .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void userIsLoggedInCallUsers_200OK() throws Exception {
        mvc.perform(get("/users/1"))
           .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void userLoggedInButNotAdminCallUsers_403() throws Exception {
        mvc.perform(get("/admin/users"))
           .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"USER", "ADMIN"})
    void userIsLoggedInAdminCallUsers_200OK() throws Exception {
        mvc.perform(get("/admin/users"))
           .andExpect(status().isOk());
    }
}
